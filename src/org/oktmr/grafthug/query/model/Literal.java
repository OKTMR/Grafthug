package org.oktmr.grafthug.query.model;


public class Literal implements Value {
    private String value;

    public Literal(String value) {
        this.value = value;
    }

    /**
     * Remove " from the beginning and end
     *
     * @param name given String to clean
     * @return cleaned result
     */
    public static String cleanQuotes(String name) {
        if (name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"')
            return name.substring(1, name.length() - 1);

        return name;
    }

    public String stringValue() {
        return value;
    }
}
