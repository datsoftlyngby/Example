package example.api;

import example.domain.NoSuchPizza;
import example.domain.Pizza;
import example.domain.PizzaRepository;
import example.domain.ValidationException;
import example.domain.recipe.*;

public class Example {

  private final RecipeFactory recipeFactory;
  private final PizzaRepository pizzaRepository;
  private final RecipeRepository recipeRepository;

  public Example(Injector injector) {
    this.recipeFactory = injector.getRecipeFactory();
    this.pizzaRepository = injector.getPizzaRepository();
    this.recipeRepository = injector.getRecipeRepository();
  }

  public ChefRecipe getChefRecipe(Pizza.Id id) throws NoSuchRecipe {
    Recipe r = recipeRepository.findFromPizza(id);
    return recipeFactory.createChefRecipe(r);
  }

  public CustomerRecipe getCustomerRecipe(Pizza.Id id) throws NoSuchPizza, ValidationException {
    Pizza p = pizzaRepository.find(id);
    Pizza.Validated v = p.validated();
    return recipeFactory.createCustomerRecipe(p);
  }

  public Pizza.Id createPizza(Pizza.Builder builder) throws ValidationException {
    Pizza.Validated validated = builder.toPizza().validated();
    return pizzaRepository.create(validated);
  }

  public static abstract class Injector {
    public RecipeFactory getRecipeFactory() {
      return new RecipeFactory();
    }
    public abstract PizzaRepository getPizzaRepository();
    public abstract RecipeRepository getRecipeRepository();
  }
}
