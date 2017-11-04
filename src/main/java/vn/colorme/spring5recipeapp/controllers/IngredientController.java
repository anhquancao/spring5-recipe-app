package vn.colorme.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.colorme.spring5recipeapp.commands.IngredientCommand;
import vn.colorme.spring5recipeapp.commands.RecipeCommand;
import vn.colorme.spring5recipeapp.commands.UnitOfMeasureCommand;
import vn.colorme.spring5recipeapp.services.IngredientService;
import vn.colorme.spring5recipeapp.services.RecipeService;
import vn.colorme.spring5recipeapp.services.UnitOfMeasureService;

@Slf4j
@Controller
public class IngredientController {

    RecipeService recipeService;
    IngredientService ingredientService;
    UnitOfMeasureService unitOfMeasureService;


    public IngredientController(
            RecipeService recipeService,
            IngredientService ingredientService,
            UnitOfMeasureService unitOfMeasureService
    ) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.findRecipeCommandById(Long.valueOf(recipeId)));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showIngredient(
            @PathVariable String recipeId,
            @PathVariable String id,
            Model model) {
        IngredientCommand ingredientCommand =
                ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id));
        model.addAttribute("ingredient", ingredientCommand);
        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(
            @PathVariable String recipeId,
            @PathVariable String id,
            Model model
    ) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        model.addAttribute("uomList", unitOfMeasureService.listAllCommands());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved receipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeService.findRecipeCommandById(Long.valueOf(recipeId));

        if (recipeCommand == null) {
            throw new RuntimeException("Recipe is not existed");
        }
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());

        model.addAttribute("ingredient", ingredientCommand);

        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasureService.listAllCommands());

        return "recipe/ingredient/ingredientform";
    }

}
