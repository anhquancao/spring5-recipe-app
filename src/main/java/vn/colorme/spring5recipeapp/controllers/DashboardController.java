package vn.colorme.spring5recipeapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.colorme.spring5recipeapp.domain.User;
import vn.colorme.spring5recipeapp.services.RecipeService;
import vn.colorme.spring5recipeapp.services.UserService;

@Slf4j
@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;


    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal User activeUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        model.addAttribute("user", user);
        return "admin/index";
    }




}
