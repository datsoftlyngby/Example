package example.domain.recipe;

import example.domain.Pizza;

public interface RecipeRepository {
  Recipe findFromPizza(Pizza.Id id) throws NoSuchRecipe;
}
