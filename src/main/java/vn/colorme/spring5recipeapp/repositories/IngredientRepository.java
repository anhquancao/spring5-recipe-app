package vn.colorme.spring5recipeapp.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.colorme.spring5recipeapp.domain.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
}
