package vn.colorme.spring5recipeapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.colorme.spring5recipeapp.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
