package com.ecommers.repositorys;

import com.ecommers.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>
{
    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    List<Category> findByParentIsNull();

    List<Category> findByParentId(UUID parentId);

    boolean existsByParentIdAndSlug(UUID parentId, String slug);

    boolean existsByParentIsNullAndSlug(String slug);
}
