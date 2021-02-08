package example.cli;

import example.api.Example;
import example.domain.NotAPizzaId;
import example.domain.Pizza;
import example.domain.PizzaRepository;
import example.domain.ValidationException;
import example.domain.recipe.NoSuchRecipe;
import example.domain.recipe.RecipeRepository;

public class Main {

  public static void main(String[] args) {
    Example example = new Example(new Example.Injector() {
      @Override
      public PizzaRepository getPizzaRepository() {
        return null;
      }

      @Override
      public RecipeRepository getRecipeRepository() {
        return null;
      }
    });

    Pizza.Builder builder = new Pizza.Builder();
    builder.setDiameter(28.3);
    try {
      example.createPizza(builder);
    } catch (ValidationException e) {
      throw new RuntimeException(e);
    }

    try {
      example.getChefRecipe(Pizza.parseId(args[1]));

    } catch (NoSuchRecipe e) {

    } catch (NotAPizzaId e) {
    }
  }

}
