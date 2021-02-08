package example.domain;

public interface PizzaRepository {
  Pizza find(Pizza.Id id) throws NoSuchPizza;
  Pizza.Id create(Pizza.Validated validated);
}
