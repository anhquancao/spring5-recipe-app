package vn.colorme.spring5recipeapp.services;

import vn.colorme.spring5recipeapp.commands.CategoryCommand;
import vn.colorme.spring5recipeapp.domain.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryCommand> findAllCategorieCommands();

    Category findCategoryByDescription(String description);
}
