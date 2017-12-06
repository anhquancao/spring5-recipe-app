package vn.colorme.spring5recipeapp.services;

import vn.colorme.spring5recipeapp.commands.CategoryCommand;

import java.util.List;

public interface CategoryService {
    List<CategoryCommand> findAllCategorieCommands();
}
