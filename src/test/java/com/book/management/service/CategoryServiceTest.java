package com.book.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.book.management.entity.CategoryEntity;
import com.book.management.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCategoryById_CategoryExists() {
        // Arrange
        Integer categoryId = 1;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        // Act
        CategoryEntity result = categoryService.getCategoryById(categoryId);

        // Assert
        Assertions.assertEquals(categoryEntity, result);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testGetCategoryById_CategoryNotExists() {
        // Arrange
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testGetAllCategories() {
        // Arrange
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        categoryEntities.add(new CategoryEntity());
        categoryEntities.add(new CategoryEntity());

        when(categoryRepository.findAll()).thenReturn(categoryEntities);

        // Act
        List<CategoryEntity> result = categoryService.getAllCategories();

        // Assert
        Assertions.assertEquals(categoryEntities, result);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testSaveCategory() {
        // Arrange
        CategoryEntity categoryEntity = new CategoryEntity();

        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);

        // Act
        CategoryEntity result = categoryService.saveCategory(categoryEntity);

        // Assert
        Assertions.assertEquals(categoryEntity, result);
        verify(categoryRepository, times(1)).save(categoryEntity);
    }

    @Test
    public void testUpdateCategory_CategoryExists() {
        // Arrange
        Integer categoryId = 1;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);

        // Act
        CategoryEntity result = categoryService.updateCategory(categoryId, categoryEntity);

        // Assert
        Assertions.assertEquals(categoryEntity, result);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(categoryEntity);
    }

    @Test
    public void testUpdateCategory_CategoryNotExists() {
        // Arrange
        Integer categoryId = 1;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            categoryService.updateCategory(categoryId, categoryEntity);
        });
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(0)).save(categoryEntity);
    }

    @Test
    public void testDeleteCategory_CategoryExists() {
        // Arrange
        Integer categoryId = 1;
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void testDeleteCategory_CategoryNotExists() {
        // Arrange
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(0)).deleteById(categoryId);
    }
}

