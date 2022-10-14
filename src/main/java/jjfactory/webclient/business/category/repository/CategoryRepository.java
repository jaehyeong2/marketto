package jjfactory.webclient.business.category.repository;

import jjfactory.webclient.business.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}
