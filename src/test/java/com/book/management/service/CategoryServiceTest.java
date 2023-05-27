package com.book.management.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.book.management.entity.Category;
import com.book.management.repository.CategoryRepository;

class CategoryServiceTest {
	@Mock
	private CategoryRepository categoryRepository;
	private CategoryService categoryService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		categoryService = new CategoryService(categoryRepository);
	}

	@Test
	void saveCategory_ShouldReturnSavedCategory() {
		// Arrange
		Category categoryToSave = new Category();
		categoryToSave.setName("Test Category");

		Category savedCategory = new Category();
		savedCategory.setId(1L);
		savedCategory.setName("Test Category");

		Mockito.when(categoryRepository.save(categoryToSave)).thenReturn(savedCategory);

		// Act
		Category result = categoryService.save(categoryToSave);

		// Assert
		Assertions.assertEquals(savedCategory, result);
	}

	@Test
	void findById_ExistingCategoryId_ShouldReturnCategory() {
		// Arrange
		Long categoryId = 1L;
		Category existingCategory = new Category();
		existingCategory.setId(categoryId);
		existingCategory.setName("Test Category");

		Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

		// Act
		Category result = categoryService.findById(categoryId);

		// Assert
		Assertions.assertEquals(existingCategory, result);
	}

	@Test
	void findById_NonExistingCategoryId_ShouldThrowNoSuchElementException() {
		// Arrange
		Long nonExistingCategoryId = 1L;

		Mockito.when(categoryRepository.findById(nonExistingCategoryId)).thenReturn(Optional.empty());

		// Act and Assert
		Assertions.assertThrows(NoSuchElementException.class, () -> categoryService.findById(nonExistingCategoryId));
	}

	@Test
	void deleteById_ExistingCategoryId_ShouldCallDeleteByIdInRepository() {
		// Arrange
		Long categoryId = 1L;

		// Act
		categoryService.deleteById(categoryId);

		// Assert
		Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(categoryId);
	}

}
