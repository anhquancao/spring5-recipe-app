package vn.colorme.spring5recipeapp.services;

import org.springframework.stereotype.Service;
import vn.colorme.spring5recipeapp.commands.CategoryCommand;
import vn.colorme.spring5recipeapp.converter.CategoryToCategoryCommand;
import vn.colorme.spring5recipeapp.domain.Category;
import vn.colorme.spring5recipeapp.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryToCategoryCommand categoryToCategoryCommand;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryCommand categoryToCategoryCommand) {
        this.categoryRepository = categoryRepository;
        this.categoryToCategoryCommand = categoryToCategoryCommand;
    }

    @Override
    public List<CategoryCommand> findAllCategorieCommands() {
        List<CategoryCommand> list = new ArrayList<>();
        categoryRepository.findAll().iterator().forEachRemaining(category -> list.add(categoryToCategoryCommand.convert(category)));
        return list;
    }

    @Override
    public Category findCategoryByDescription(String description) {
        return categoryRepository.findByDescription(description).get();
    }


}
