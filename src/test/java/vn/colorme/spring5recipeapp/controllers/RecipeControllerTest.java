package vn.colorme.spring5recipeapp.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.colorme.spring5recipeapp.commands.RecipeCommand;
import vn.colorme.spring5recipeapp.domain.Recipe;
import vn.colorme.spring5recipeapp.domain.User;
import vn.colorme.spring5recipeapp.exceptions.NotFoundException;
import vn.colorme.spring5recipeapp.services.RecipeService;
import vn.colorme.spring5recipeapp.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    RecipeController recipeController;

    MockMvc mockMvc;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @Mock
    UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setName("admin");
        Mockito.when(userService.findUserByEmail(any())).thenReturn(user);
    }

    @Test
    public void getRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);


        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        mockMvc.perform(get("/admin/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
        verify(recipeService, times(1)).getRecipeById(1L);
    }

    @Test
    public void testGetNewRecipeForm() throws Exception {
        RecipeCommand command = new RecipeCommand();

        mockMvc.perform(get("/admin/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testGetRecipeNotFound() throws Exception {

        when(recipeService.getRecipeById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/admin/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void testGetRecipeNumberFormat() throws Exception {

        mockMvc.perform(get("/admin/recipe/asd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(
                post("/admin/recipe")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("description", "string")
                        .param("directions", "string")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/recipe/2/show"));
    }

    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId(2L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/admin/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")

        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));
    }

    @Test
    public void testGetUpdateView() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when(recipeService.findRecipeCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/admin/recipe/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteAction() throws Exception {
        mockMvc.perform(get("/admin/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/recipes"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }
}



