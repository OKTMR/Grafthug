package org.oktmr.grafthug.query;

import java.util.Arrays;
import java.util.HashMap;

import org.oktmr.grafthug.query.exception.IncorrectPrefixStructure;
import org.oktmr.grafthug.query.model.URI;

public class QueryParser {
	
	/**
	 * Parses the sparql query string
	 * @param queryString sparql String
	 * @return A query object reflecting the sparql query
	 * @throws IncorrectPrefixStructure if the prefixes are incorrect
	 */
	public static Query parse(String queryString) throws IncorrectPrefixStructure {
		String queryLower = queryString.toLowerCase();
		String[] splitted = queryLower.split("select");
		HashMap<String, String> prefixes = new HashMap<>();

		if (splitted.length > 1) {
			prefixes = extractPrefixes(splitted[0]);
		}

		Query query = new Query();

		return query;
	}

	/**
	 * returns a cleaned (prefix -> uri) from a raw prefix string
	 * @param prefixString only a string containing sparql prefixes
	 * @return a hashmap containing all the prefixes
	 * @throws IncorrectPrefixStructure if the sparql query is incorrect
	 */
	private static HashMap<String, String> extractPrefixes(String prefixString) throws IncorrectPrefixStructure {
		HashMap<String, String> prefixes = new HashMap<>();
		String[] splitted = prefixString.split("prefix", 0);

		for (int i = 0; i < splitted.length; ++i) {
			splitted[i] = splitted[i].trim();
		}

		splitted = Arrays.stream(splitted).filter((prefix) -> prefix.length() > 0).toArray(String[]::new);

		for (String prefix : splitted) {
			String[] prefixSplit = prefix.split(":",2);

			if (prefixSplit.length != 2) {
				throw new IncorrectPrefixStructure();
			}

			prefixes.put(prefixSplit[0].trim(), URI.cleanAngleQuotes(prefixSplit[1].trim()));

		}

		return prefixes;
	}
}
