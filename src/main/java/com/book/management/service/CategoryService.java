package com.book.management.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.management.entity.CategoryEntity;
import com.book.management.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryEntity getCategoryById(Integer id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        throw new NoSuchElementException("Category not found");
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryEntity saveCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public CategoryEntity updateCategory(Integer id, CategoryEntity category) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            category.setId(id);
            return categoryRepository.save(category);
        }
        throw new NoSuchElementException("Category not found");
    }

    public void deleteCategory(Integer id) {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Category not found");
        }
    }
}

