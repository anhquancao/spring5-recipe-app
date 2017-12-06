package vn.colorme.spring5recipeapp.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import vn.colorme.spring5recipeapp.domain.Recipe;
import vn.colorme.spring5recipeapp.domain.User;
import vn.colorme.spring5recipeapp.services.RecipeService;
import vn.colorme.spring5recipeapp.services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class IndexControllerTest {

    IndexController indexController;

    @Mock
    UserService userService;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService, userService);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    public void index() throws Exception {


// Mockito.whens() for your authorization object


        // given
        List<Recipe> recipes = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipes.add(recipe);
        recipes.add(new Recipe());

        ArgumentCaptor<List<Recipe>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        User user = new User();
        user.setName("admin");

        // when
        Mockito.when(recipeService.getRecipes()).thenReturn(recipes);
        Mockito.when(userService.findUserByEmail(any())).thenReturn(user);

        // then
        String viewName = indexController.index(model);
        Assert.assertEquals("index", viewName);
        Mockito.verify(recipeService, Mockito.times(1)).getRecipes();
        Mockito.verify(model, Mockito.times(1)).addAttribute(Mockito.eq("recipes"), listArgumentCaptor.capture());
        List<Recipe> setInController = listArgumentCaptor.getValue();
        Assert.assertEquals(2, setInController.size());
    }

}