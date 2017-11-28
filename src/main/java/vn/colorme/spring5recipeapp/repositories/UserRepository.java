package vn.colorme.spring5recipeapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.colorme.spring5recipeapp.commands.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
