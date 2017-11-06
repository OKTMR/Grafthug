package org.oktmr.grafthug.query;

import org.oktmr.grafthug.query.model.Field;
import org.oktmr.grafthug.query.model.Resource;
import org.oktmr.grafthug.query.model.URI;
import org.oktmr.grafthug.query.model.Value;

public class Condition {
    private Field<Resource> subject;
    private Field<URI> predicate;
    private Field<Value> object;

    public Condition(Field<Resource> subject, Field<URI> predicate, Field<Value> object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;

    }

    public String toString() {
        return subject.stringValue() + " " + predicate.stringValue() + " " + object.stringValue();
    }
}
