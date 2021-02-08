package example.domain.recipe;

import example.domain.Pizza;

public class RecipeFactory {

  public ChefRecipe createChefRecipe(Recipe r) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public CustomerRecipe createCustomerRecipe(Pizza p) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
