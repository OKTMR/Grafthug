package org.oktmr.grafthug.query.model;

class URI implements Value {
  public int namespaceIdx;
  public String uri;

  public URI(String uri) {
    this.uri = uri;
    this.namespaceIdx = getLocalNameIndex(uri);
  }

  public String stringValue() {
    return uri;
  }

  public static int getLocalNameIndex(String uri) {
    int separatorIdx = uri.indexOf('#');

    if (separatorIdx < 0) {
      separatorIdx = uri.lastIndexOf('/');
    }

    if (separatorIdx < 0) {
      separatorIdx = uri.lastIndexOf(':');
    }

    if (separatorIdx < 0) {
      throw new IllegalArgumentException("No separator character founds in URI: " + uri);
    }

    return separatorIdx + 1;
  }

}
