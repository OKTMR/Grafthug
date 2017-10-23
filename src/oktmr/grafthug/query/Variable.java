package oktmr.grafthug.query;

public class Variable {
  private int id;
  private String name;
  private boolean isReturned;

  public Variable(int id, String name, boolean isReturned) {
    this.id = id;
    this.name = name;
    this.isReturned = isReturned;
  }
}
