package vn.colorme.spring5recipeapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.colorme.spring5recipeapp.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByDescription(String description);
}
