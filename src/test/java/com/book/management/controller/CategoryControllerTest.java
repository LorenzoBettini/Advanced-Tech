package com.book.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.book.management.entity.Category;
import com.book.management.model.CategoryDTO;
import com.book.management.service.CategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private CategoryController categoryController;

	private CategoryDTO categoryDTO;
	private Category category;

	@BeforeEach
	public void setup() {
		// Initialize test data
		categoryDTO = new CategoryDTO();
		categoryDTO.setId(1L);
		categoryDTO.setName("Sample Category");

		category = new Category();
		category.setId(1L);
		category.setName("Sample Category");
	}

	@Test
	void saveCategory_ShouldReturnSavedCategory() {
		// Arrange
		when(categoryService.save(any(Category.class))).thenReturn(category);

		// Act
		ResponseEntity<CategoryDTO> response = categoryController.saveCategory(categoryDTO);

		// Assert
		verify(categoryService, times(1)).save(any(Category.class));
		assert response.getStatusCode() == HttpStatus.OK;
		assert response.getBody() != null;
		assert response.getBody().getName().equals("Sample Category");

	}

	@Test
	void getCategoryById_ShouldReturnCategory() {
		// Arrange
		when(categoryService.findById(1L)).thenReturn(category);

		// Act
		ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(1L);

		// Assert
		verify(categoryService, times(1)).findById(1L);
		assert response.getStatusCode() == HttpStatus.OK;
		assert response.getBody() != null;
		assert response.getBody().getName().equals("Sample Category");

	}

	@Test
	void updateCategory_ShouldReturnUpdatedCategory() {
		// Arrange
		when(categoryService.findById(1L)).thenReturn(category);
		when(categoryService.save(any(Category.class))).thenReturn(category);

		// Act
		ResponseEntity<CategoryDTO> response = categoryController.updateCategory(1L, categoryDTO);

		// Assert
		verify(categoryService, times(1)).findById(1L);
		verify(categoryService, times(1)).save(any(Category.class));
		assert response.getStatusCode() == HttpStatus.OK;
		assert response.getBody() != null;
		assert response.getBody().getName().equals("Sample Category");

	}

	@Test
	void deleteCategory_ShouldReturnNoContent() {
		// Act
		ResponseEntity<Void> response = categoryController.deleteCategory(1L);

		// Assert
		verify(categoryService, times(1)).deleteById(1L);
		assert response.getStatusCode() == HttpStatus.NO_CONTENT;

	}
}
