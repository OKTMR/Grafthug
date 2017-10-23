package oktmr.grafthug.query;

import java.util.HashMap;

public class QueryParser {
  public static Query parse(String queryString) {
    Query query = new Query();

    extractPrefixes(queryString.toLowerCase().split("select")[0]);
    return query;
  }

  private static HashMap<String, String> extractPrefixes(String prefixString) {
    HashMap<String, String> prefixes = new HashMap<>();
    String[] splitted = prefixString.split("prefix");
    System.out.println(splitted);
    return prefixes;
  }
}
