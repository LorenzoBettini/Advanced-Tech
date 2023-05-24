package com.book.management.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.management.entity.CategoryEntity;
import com.book.management.mapper.CategoryMapper;
import com.book.management.model.CategoryDto;
import com.book.management.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryService.getAllCategories();
        return categoryEntities.stream()
                .map(CategoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") Integer id) {
        try {
            CategoryEntity categoryEntity = categoryService.getCategoryById(id);
            CategoryDto categoryDto = CategoryMapper.mapToDto(categoryEntity);
            return ResponseEntity.ok(categoryDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryEntity categoryEntity = CategoryMapper.mapToEntity(categoryDto);
        CategoryEntity savedCategory = categoryService.saveCategory(categoryEntity);
        CategoryDto savedCategoryDto = CategoryMapper.mapToDto(savedCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") Integer id,
                                                      @RequestBody @Valid CategoryDto categoryDto) {
        try {
            CategoryEntity categoryEntity = CategoryMapper.mapToEntity(categoryDto);
            CategoryEntity updatedCategory = categoryService.updateCategory(id, categoryEntity);
            CategoryDto updatedCategoryDto = CategoryMapper.mapToDto(updatedCategory);
            return ResponseEntity.ok(updatedCategoryDto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Integer id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
