package com.oak.finance_manager.repositories;

import com.oak.finance_manager.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
