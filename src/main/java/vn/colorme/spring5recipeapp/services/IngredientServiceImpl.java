package vn.colorme.spring5recipeapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.colorme.spring5recipeapp.commands.IngredientCommand;
import vn.colorme.spring5recipeapp.converter.IngredientCommandToIngredient;
import vn.colorme.spring5recipeapp.converter.IngredientToIngredientCommand;
import vn.colorme.spring5recipeapp.domain.Ingredient;
import vn.colorme.spring5recipeapp.domain.Recipe;
import vn.colorme.spring5recipeapp.repositories.IngredientRepository;
import vn.colorme.spring5recipeapp.repositories.RecipeRepository;
import vn.colorme.spring5recipeapp.repositories.UnitOfMeasureRepository;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private IngredientRepository ingredientRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;
    private RecipeRepository recipeRepository;
    private IngredientToIngredientCommand ingredientToIngredientCommand;
    private IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(
            IngredientRepository ingredientRepository,
            RecipeRepository recipeRepository,
            IngredientCommandToIngredient ingredientCommandToIngredient,
            UnitOfMeasureRepository unitOfMeasureRepository,
            IngredientToIngredientCommand ingredientToIngredientCommand
    ) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(Long.valueOf(recipeId));

        if (!recipeOptional.isPresent()) {
            log.error("recipe id is not found: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional =
                recipe.getIngredients().stream()
                        .filter(ingredient -> ingredient.getId().equals(ingredientId))
                        .map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
                        .findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            log.error("Ingredient id not found: " + ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

        if (!recipeOptional.isPresent()) {
            log.error("recipe not found for id: " + ingredientCommand.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId() == ingredientCommand.getId())
                    .findFirst();
            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUnitOfMeasure(
                        unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId())
                                .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))
                );
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                recipe.addIngredient(ingredient);
                ingredient.setRecipe(recipe);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            if (!savedIngredientOptional.isPresent()) {
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId()
                                .equals(ingredientCommand.getUnitOfMeasure().getId()))
                        .findFirst();
            }
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }
    }

    @Override
    public void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(Long.valueOf(recipeId));
        if (!recipeOptional.isPresent()) {
            log.debug("Recipe is not existed");
        } else {
            log.debug("Recipe found");
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(Long.valueOf(ingredientId)))
                    .findFirst();
            if (ingredientOptional.isPresent()) {
                log.debug("Ingredient Found");
                Ingredient ingredient = ingredientOptional.get();
                ingredient.setRecipe(null);
                recipe.getIngredients().remove(ingredient);
                recipeRepository.save(recipe);
                ingredientRepository.deleteById(Long.valueOf(ingredientId));

            } else {
                log.debug("Ingredient Not Found");
            }


        }
    }
}
