package com.oak.finance_manager.controller;

import com.oak.finance_manager.domain.category.Category;
import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.category.CategoryCreateDTO;
import com.oak.finance_manager.dto.category.CategoryUpdateDTO;
import com.oak.finance_manager.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
       Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllByUser(@AuthenticationPrincipal User user) {
        List<Category> categories = categoryService.getCategoryByUser(user);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryCreateDTO dto, @AuthenticationPrincipal User user) {
        String createdId = categoryService.createCategory(dto, user);
        return ResponseEntity.ok(createdId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@RequestBody @Valid CategoryUpdateDTO dto, @PathVariable String id) {
        String updatedId = categoryService.updateCategory(dto, id);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        String deletedId = categoryService.deleteCategory(id);
        return ResponseEntity.ok(deletedId);
    }
}
