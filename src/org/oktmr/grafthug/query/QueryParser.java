package org.oktmr.grafthug.query;

import org.oktmr.grafthug.query.exception.IncorrectConditionStructure;
import org.oktmr.grafthug.query.exception.IncorrectPrefixStructure;
import org.oktmr.grafthug.query.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QueryParser {

    /**
     * Parses the sparql query string
     *
     * @param queryString sparql String
     * @return A query object reflecting the sparql query
     * @throws IncorrectPrefixStructure if the prefixes are incorrect
     */
    public static Query parse(String queryString) throws IncorrectPrefixStructure, IncorrectConditionStructure {
        String[] splitted = queryString.split("(?i)\\s+select\\s+", 2);
        HashMap<String, String> prefixes = new HashMap<>();

        if (splitted.length > 1) {
            prefixes = extractPrefixes(splitted[0]);
        }

        splitted = splitted[1].split("(?i)\\s+where\\s*", 2);

        HashMap<Integer, Field> variables = extractVariables(splitted[0]);
        ArrayList<Condition> conditions = extractConditions(prefixes, cleanCurlyBrackets(splitted[1].trim
                ()));

        Query query = new Query();

        query.addPrefixes(prefixes);
        query.addVariables(variables);
        query.addConditions(conditions);

        return query;
    }

    /**
     * returns a cleaned (prefix -> uri) from a raw prefix string
     *
     * @param prefixString only a string containing sparql prefixes
     * @return a hashmap containing all the prefixes
     * @throws IncorrectPrefixStructure if the sparql query is incorrect
     */
    private static HashMap<String, String> extractPrefixes(String prefixString) throws IncorrectPrefixStructure {
        HashMap<String, String> prefixes = new HashMap<>();
        String[] splitted = prefixString.split("(?i)\\s*prefix\\s+", 0);

        for (int i = 0; i < splitted.length; ++i) {
            splitted[i] = splitted[i].trim();
        }

        splitted = Arrays.stream(splitted).filter((prefix) -> prefix.length() > 0).toArray(String[]::new);

        for (String prefix : splitted) {
            String[] prefixSplit = prefix.split(":", 2);

            if (prefixSplit.length != 2) {
                throw new IncorrectPrefixStructure();
            }

            prefixes.put(prefixSplit[0].trim(), URI.cleanAngleQuotes(prefixSplit[1].trim()));

        }

        return prefixes;
    }

    /**
     * returns a cleaned (name -> field) from a raw results string (between SELECT and WHERE)
     *
     * @param variableString raw string containing only the result variables
     * @return a hashmap containing all the variables
     */
    private static HashMap<Integer, Field> extractVariables(String variableString) {
        ArrayList<Field> fields = new ArrayList<>();
        int key = 1;
        String[] splitted = variableString.split("\\s+");

        for (String split : splitted) {
            fields.add(new Field(Field.getVariableName(split)));
        }
        HashMap<Integer, Field> variables = new HashMap<>();
        for(Field field : fields) {
        	variables.put(key, field);
        	key++;
        }
        return variables;
    }

    private static ArrayList<Condition> extractConditions(HashMap<String, String> prefixes, String conditionString)
            throws
            IncorrectConditionStructure {
        ArrayList<Condition> conditions = new ArrayList<>();

        String[] splittedConditions = conditionString.split("\\s+\\.\\s+|\\s+\\.|\\.\\s+");

        for (String splitCondition : splittedConditions) {
            String condSplit[] = splitCondition.split("\\s+");
            if (condSplit.length > 3) throw new IncorrectConditionStructure();

            String subject = condSplit[0];
            Field<Resource> subjectField;
            if (Field.isVariable(subject)) {
                subjectField = new Field<>(Field.getVariableName(subject));
            } else {
                String split[] = subject.split(":");
                if (split.length > 1) {
                    subjectField = new Field<>(new URI(prefixes.get(split[0]) + split[1]));
                } else if (subject.charAt(0) == '<') {
                    subjectField = new Field<>(new URI(URI.cleanAngleQuotes(subject)));
                } else {
                    throw new IncorrectConditionStructure();
                }
            }

            String predicate = condSplit[1];
            Field<URI> predicateField;
            if (Field.isVariable(predicate)) {
                predicateField = new Field<>(Field.getVariableName(predicate));
            } else {
                String split[] = predicate.split(":");
                if (split.length > 1) {
                    predicateField = new Field<>(new URI(prefixes.get(split[0]) + split[1]));
                } else if (predicate.charAt(0) == '<') {
                    predicateField = new Field<>(new URI(URI.cleanAngleQuotes(predicate)));
                } else {
                    throw new IncorrectConditionStructure();
                }
            }

            String object = condSplit[2];
            Field<Value> objectField;
            if (Field.isVariable(object)) {
                objectField = new Field<>(Field.getVariableName(object));
            } else {
                String split[] = object.split(":");
                if (split.length > 1) {
                    objectField = new Field<>(new URI(prefixes.get(split[0]) + split[1]));
                } else if (object.charAt(0) == '<') {
                    objectField = new Field<>(new URI(URI.cleanAngleQuotes(object)));
                } else {
                    objectField = new Field<>(new Literal(Literal.cleanQuotes(object)));
                }
            }


            conditions.add(new Condition(subjectField, predicateField, objectField));

        }

        return conditions;
    }

    public static String cleanCurlyBrackets(String rawString) {
        if (rawString.charAt(0) == '{' && rawString.charAt(rawString.length() - 1) == '}')
            return rawString.substring(1, rawString.length() - 1);

        return rawString;
    }

}
