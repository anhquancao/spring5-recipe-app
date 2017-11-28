package vn.colorme.spring5recipeapp.services;

import vn.colorme.spring5recipeapp.domain.User;

public interface UserService {
    User findUserByEmail(String email);

    void saveUser(User user);
}
