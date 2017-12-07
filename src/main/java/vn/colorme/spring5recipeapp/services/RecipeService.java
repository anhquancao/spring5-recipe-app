package vn.colorme.spring5recipeapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.colorme.spring5recipeapp.commands.RecipeCommand;
import vn.colorme.spring5recipeapp.domain.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes();

    Recipe getRecipeById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    RecipeCommand findRecipeCommandById(Long id);

    void deleteById(Long id);

    Page<Recipe> listAllByPage(Pageable pageable);
}
