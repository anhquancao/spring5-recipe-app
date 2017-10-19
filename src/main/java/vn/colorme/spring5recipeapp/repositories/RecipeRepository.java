package vn.colorme.spring5recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.colorme.spring5recipeapp.domain.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
