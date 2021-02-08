package example.domain;

public class Pizza {

  private final Id id;
  private Double diameter;

  public Pizza(Id id, Double diameter) {
    this.id = id;
    this.diameter = diameter;
  }

  public Id getId() {
    return id;
  }

  public Validated validated() throws ValidationException {
    return new Validated(id, diameter);
  }


  public static Id idFromInt(int id) {
    return new Id(id);
  }

  public static Id parseId(String str) throws NotAPizzaId {
    try {
      return new Id(Integer.parseInt(str));
    } catch (NumberFormatException e) {
      throw new NotAPizzaId(e);
    }
  }

  public static class Validated extends Pizza {
    public Validated(Id id, Double diameter) throws ValidationException {
      super(id, diameter);
      validate();
    }

    public void validate() throws ValidationException {
      if (getId() == null) {
        throw new ValidationException("Id should not be null");
      }
    }
  }

  public static class Builder {
    private Id id;
    private double diameter;

    public Id getId() {
      return id;
    }

    public void setId(Id id) {
      this.id = id;
    }

    public double getDiameter() {
      return diameter;
    }

    public void setDiameter(double diameter) {
      this.diameter = diameter;
    }

    public Pizza toPizza() {
      return new Pizza(id, diameter);
    }
  }


  public static class Id {
    private final int rep;
    private Id(int rep) {
      this.rep = rep;
    }
    public int getRep() {
      return rep;
    }
  }
}
