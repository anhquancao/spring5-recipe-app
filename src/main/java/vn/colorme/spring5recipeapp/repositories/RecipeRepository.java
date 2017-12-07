package vn.colorme.spring5recipeapp.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import vn.colorme.spring5recipeapp.domain.Recipe;

public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {

}
