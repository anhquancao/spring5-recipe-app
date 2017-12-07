package vn.colorme.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.colorme.spring5recipeapp.domain.Recipe;
import vn.colorme.spring5recipeapp.domain.User;
import vn.colorme.spring5recipeapp.services.RecipeService;
import vn.colorme.spring5recipeapp.services.UserService;

import java.util.List;

@Slf4j
@Controller
public class IndexController {

    private RecipeService recipeService;

    UserService userService;

    public IndexController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Pageable topNine = new PageRequest(0, 9);
        List<Recipe> recipes = recipeService.listAllByPage(topNine).getContent();
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", user);
        return "index";
    }
}
