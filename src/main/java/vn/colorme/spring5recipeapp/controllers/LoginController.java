package vn.colorme.spring5recipeapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import vn.colorme.spring5recipeapp.services.UserService;

public class LoginController {
    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/403")
    public String error403() {
        return "403";
    }

}
