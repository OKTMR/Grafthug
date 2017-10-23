package oktmr.grafthug;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DataStoreManager {

    public HashMap<Integer, TripletPair> subjects;
    public HashMap<Integer, TripletPair> predicates;
    public HashMap<Integer, TripletPair> objects;
    public HashMap<IntegerPair, TripletPair> subjectPredicate;
    public HashMap<IntegerPair, TripletPair> subjectObject;
    public HashMap<IntegerPair, TripletPair> predicateObject;

    public HashSet<Triplet> triplets;

    public HashMap<String, Integer> valueIndexes;
    public ArrayList<String> values;

    public DataStoreManager() {
        subjects = new HashMap<>();
        predicates = new HashMap<>();
        objects = new HashMap<>();
        subjectPredicate = new HashMap<>();
        subjectObject = new HashMap<>();
        predicateObject = new HashMap<>();

        triplets = new HashSet<>();

        valueIndexes = new HashMap<>();
        values = new ArrayList<>();
    }

    public int add(String value) {
        if (!valueIndexes.containsKey(value)) {
            int index = values.size();
            valueIndexes.put(value, index);
            values.add(value);
            return index;
        }

        return valueIndexes.get(value);
    }

    public void add(Resource subject, URI predicate, Value value) {
        // indexing elements;
        int subjectIndex = add(subject.stringValue());
        int predicateIndex = add(predicate.stringValue());
        int valueIndex = add(value.stringValue());

        // creating triplet pairs
        Triplet triplet = new Triplet(subjectIndex, predicateIndex, valueIndex);
        triplets.add(triplet);

        System.out.println(triplet.toString());

        // generating lists
        linkSubject(triplet);
        linkPredicate(triplet);
        linkObject(triplet);

        linkSubjectPredicate(triplet);
        linkSubjectObject(triplet);
        linkPredicateObject(triplet);

    }

    public void optimize() {
        // resizing to exact number of values to avoid redondand storage
        subjects = new HashMap<>(subjects);
        predicates = new HashMap<>(predicates);
        objects = new HashMap<>(objects);
        subjectPredicate = new HashMap<>(subjectPredicate);
        subjectObject = new HashMap<>(subjectObject);
        predicateObject = new HashMap<>(predicateObject);

        triplets = new HashSet<>(triplets);
        valueIndexes = new HashMap<>(valueIndexes);
        values = new ArrayList<>(values);
    }

    private void linkSubject(Triplet triplet) {
        TripletPair subjectPair = subjects.computeIfAbsent(triplet.getSubject(), integer -> new TripletPair());

        if (subjectPair.first == null) {
            subjectPair.first = triplet;
            subjectPair.last = triplet;
        } else {
            subjectPair.last.getSubjectLink().setNext(triplet);
            triplet.getSubjectLink().setPrevious(subjectPair.last);
            subjectPair.last = triplet;
        }
    }

    private void linkPredicate(Triplet triplet) {
        TripletPair predicatePair = predicates.computeIfAbsent(triplet.getPredicate(), integer -> new TripletPair());

        if (predicatePair.first == null) {
            predicatePair.first = triplet;
            predicatePair.last = triplet;
        } else {
            predicatePair.last.getPredicateLink().setNext(triplet);
            triplet.getPredicateLink().setPrevious(predicatePair.last);
            predicatePair.last = triplet;
        }
    }

    private void linkObject(Triplet triplet) {
        TripletPair objectPair = objects.computeIfAbsent(triplet.getObject(), integer -> new TripletPair());

        if (objectPair.first == null) {
            objectPair.first = triplet;
            objectPair.last = triplet;
        } else {
            objectPair.last.getObjectLink().setNext(triplet);
            triplet.getObjectLink().setPrevious(objectPair.last);
            objectPair.last = triplet;
        }
    }

    private void linkSubjectPredicate(Triplet triplet) {
        TripletPair subjectPredicatePair = subjectPredicate
                .computeIfAbsent(new IntegerPair(triplet.getSubject(), triplet.getPredicate()),
                                 integerPair -> new TripletPair());
        if (subjectPredicatePair.first == null) {
            subjectPredicatePair.first = triplet;
            subjectPredicatePair.last = triplet;
        } else {
            subjectPredicatePair.last.getSubjectPredicateLink().setNext(triplet);
            triplet.getSubjectPredicateLink().setPrevious(subjectPredicatePair.last);
            subjectPredicatePair.last = triplet;
        }
    }

    private void linkSubjectObject(Triplet triplet) {
        TripletPair subjectObjectPair = subjectObject
                .computeIfAbsent(new IntegerPair(triplet.getSubject(), triplet.getObject()),
                                 integerPair -> new TripletPair());
        if (subjectObjectPair.first == null) {
            subjectObjectPair.first = triplet;
            subjectObjectPair.last = triplet;
        } else {
            subjectObjectPair.last.getSubjectObjectLink().setNext(triplet);
            triplet.getSubjectObjectLink().setPrevious(subjectObjectPair.last);
            subjectObjectPair.last = triplet;
        }
    }

    private void linkPredicateObject(Triplet triplet) {
        TripletPair predicateObjectPair = predicateObject
                .computeIfAbsent(new IntegerPair(triplet.getPredicate(), triplet.getObject()),
                                 integerPair -> new TripletPair());
        if (predicateObjectPair.first == null) {
            predicateObjectPair.first = triplet;
            predicateObjectPair.last = triplet;
        } else {
            predicateObjectPair.last.getPredicateObjectLink().setNext(triplet);
            triplet.getPredicateObjectLink().setPrevious(predicateObjectPair.last);
            predicateObjectPair.last = triplet;
        }
    }

    public final class TripletPair {

        public Triplet first = null;
        public Triplet last = null;
    }

    private final class IntegerPair {
        public final int first;
        public final int last;

        public IntegerPair(int first, int last) {
            this.first = first;
            this.last = last;
        }

        @Override
        public int hashCode() {
            return (first + ";" + last).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof IntegerPair)) return false;

            IntegerPair i = (IntegerPair) obj;

            return i.first == first && i.last == last;
        }
    }
}
