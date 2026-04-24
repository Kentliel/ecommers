package com.ecommers.repositorys;

import com.ecommers.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID>
{
    Optional<Brand> findByName(String name);

    boolean existsByName(String name);
}
