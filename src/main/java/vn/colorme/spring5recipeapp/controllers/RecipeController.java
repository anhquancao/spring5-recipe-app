package vn.colorme.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.colorme.spring5recipeapp.commands.CategoryCommand;
import vn.colorme.spring5recipeapp.commands.RecipeCommand;
import vn.colorme.spring5recipeapp.domain.User;
import vn.colorme.spring5recipeapp.exceptions.NotFoundException;
import vn.colorme.spring5recipeapp.services.CategoryService;
import vn.colorme.spring5recipeapp.services.RecipeService;
import vn.colorme.spring5recipeapp.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class RecipeController {

    private RecipeService recipeService;
    private UserService userService;
    private CategoryService categoryService;


    public RecipeController(RecipeService recipeService,
                            UserService userService,
                            CategoryService categoryService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/recipe/{id}/show")
    public String getRecipe(@PathVariable String id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("recipe", recipeService.getRecipeById(new Long(id)));
        return "recipe/show";
    }

    @GetMapping("/admin/recipe/new")
    public String newRecipe(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<CategoryCommand> categories = categoryService.findAllCategorieCommands();

        model.addAttribute("user", user);
        model.addAttribute("categories", categories);
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @GetMapping("/admin/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        List<CategoryCommand> categories = categoryService.findAllCategorieCommands();
        model.addAttribute("categories", categories);
        model.addAttribute("user", user);
        model.addAttribute("recipe", recipeService.findRecipeCommandById(new Long(id)));
        return "recipe/recipeform";
    }

    @PostMapping("/admin/recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user", user);

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return "recipe/recipeform";
        }

        command.setUserId(user.getId());
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/admin/recipe/" + savedRecipeCommand.getId() + "/show";
    }

    @GetMapping("/admin/recipe/{id}/delete")
    public String deleteById(@PathVariable String id) {
        log.debug("Deleting id: " + id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/admin/recipes";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception e) {

        log.error("Handling not found exception");
        log.error(e.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", e);

        return modelAndView;
    }

}
