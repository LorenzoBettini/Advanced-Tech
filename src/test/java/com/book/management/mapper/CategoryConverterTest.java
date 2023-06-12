package com.book.management.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.book.management.entity.Category;
import com.book.management.model.CategoryDTO;

class CategoryConverterTest {
	@Test
	void toDTO_ShouldConvertCategoryToCategoryDTO() {
		// Arrange
		Category category = new Category();
		category.setId(1L);
		category.setName("Test Category");

		// Act
		CategoryDTO categoryDTO = CategoryConverter.toDTO(category);

		// Assert
		Assertions.assertEquals(category.getId(), categoryDTO.getId());
		Assertions.assertEquals(category.getName(), categoryDTO.getName());
	}

	@Test
	void toEntity_ShouldConvertCategoryDTOToCategory() {
		// Arrange
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(1L);
		categoryDTO.setName("Test Category");

		// Act
		Category category = CategoryConverter.toEntity(categoryDTO);

		// Assert
		Assertions.assertEquals(categoryDTO.getId(), category.getId());
		Assertions.assertEquals(categoryDTO.getName(), category.getName());
	}
}
