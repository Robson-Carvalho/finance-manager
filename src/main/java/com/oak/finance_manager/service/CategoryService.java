package com.oak.finance_manager.service;

import com.oak.finance_manager.domain.category.Category;
import com.oak.finance_manager.domain.user.User;
import com.oak.finance_manager.dto.category.CategoryCreateDTO;
import com.oak.finance_manager.dto.category.CategoryUpdateDTO;
import com.oak.finance_manager.exceptions.category.CategoryNotFoundException;
import com.oak.finance_manager.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository repository){
        this.categoryRepository = repository;
    }

    public Category getCategoryById(String id){
        Optional<Category> category= this.categoryRepository.findById(UUID.fromString(id));
        return category.orElse(null);
    }

    public List<Category> getCategoryByUser(User user){
        List<Category> all = categoryRepository.findAll();
        List<Category> result = new ArrayList<>();

        for (Category category : all) {
            if (category.getUser().getId().equals(user.getId())) {
                result.add(category);
            }
        }

        return result;
    }

    public String createCategory(CategoryCreateDTO dto, User user) {
        Category category = new Category();
        category.setName(dto.categoryName());
        category.setUser(user);

        return categoryRepository.save(category).getId().toString();
    }

    public String updateCategory(CategoryUpdateDTO categoryUpdateDTO, String categoryId) {
        Optional<Category> category = categoryRepository.findById(UUID.fromString(categoryId));

        if(category.isEmpty()){
            throw new CategoryNotFoundException();
        }

        category.get().setName(categoryUpdateDTO.categoryName());

        return categoryRepository.save(category.get()).getId().toString();
    }

    public String deleteCategory(String categoryId) {
        Optional<Category> category = categoryRepository.findById(UUID.fromString(categoryId));

        if(category.isEmpty()){
            throw new CategoryNotFoundException();
        }

        categoryRepository.deleteById(category.get().getId());

        return category.get().getId().toString();
    }
}
