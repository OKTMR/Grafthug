package oktmr.grafthug;

public class Triplet {
    //subject link;
    private final TripletLink subjectLink = new TripletLink();
    //predicate link;
    private final TripletLink predicateLink = new TripletLink();
    //object link;
    private final TripletLink objectLink = new TripletLink();
    //sp link
    private final TripletLink subjectPredicateLink = new TripletLink();
    //so link
    private final TripletLink subjectObjectLink = new TripletLink();
    //po link
    private final TripletLink predicateObjectLink = new TripletLink();

    private final int subject;
    private final int predicate;
    private final int object;

    public Triplet(int subject, int predicate, int object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public int getSubject() {
        return subject;
    }

    public int getPredicate() {
        return predicate;
    }

    public int getObject() {
        return object;
    }

    public TripletLink getSubjectLink() {
        return subjectLink;
    }

    public TripletLink getPredicateLink() {
        return predicateLink;
    }

    public TripletLink getObjectLink() {
        return objectLink;
    }

    public TripletLink getSubjectPredicateLink() {
        return subjectPredicateLink;
    }

    public TripletLink getSubjectObjectLink() {
        return subjectObjectLink;
    }

    public TripletLink getPredicateObjectLink() {
        return predicateObjectLink;
    }

    @Override
    public String toString() {
        return subject + ";" + predicate + ";" + object;
    }

    @Override
    public int hashCode() {
        return (subject + ";" + predicate + ";" + object).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Triplet))
            return false;

        Triplet t = (Triplet) o;

        return t.subject == subject && t.predicate == predicate && t.object == object;
    }

    public final class TripletLink {

        private Triplet previous;
        private Triplet next;

        public Triplet getPrevious() {
            return previous;
        }

        public void setPrevious(Triplet previous) {
            this.previous = previous;
        }

        public Triplet getNext() {
            return next;
        }

        public void setNext(Triplet next) {
            this.next = next;
        }

    }
}
