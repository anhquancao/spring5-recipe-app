package vn.colorme.spring5recipeapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import vn.colorme.spring5recipeapp.domain.User;
import vn.colorme.spring5recipeapp.services.UserService;

import javax.validation.Valid;

@Controller
public class LoginController {
    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String createNewUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        User userExist = userService.findUserByEmail(user.getEmail());
        if (userExist != null) {
            bindingResult.rejectValue("email", "error.user",
                    "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            userService.saveUser(user);
            return "redirect:/";

        }
    }


    @GetMapping("/403")
    public String error403() {
        return "403";
    }

}
