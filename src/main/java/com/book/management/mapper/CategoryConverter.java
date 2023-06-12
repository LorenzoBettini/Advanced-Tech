package com.book.management.mapper;

import org.springframework.stereotype.Component;

import com.book.management.entity.Category;
import com.book.management.model.CategoryDTO;

@Component
public class CategoryConverter {

	private CategoryConverter() {
		// Private constructor to prevent instantiation of this class
	}

	public static CategoryDTO toDTO(Category category) {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(category.getId());
		categoryDTO.setName(category.getName());
		return categoryDTO;
	}

	public static Category toEntity(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setId(categoryDTO.getId());
		category.setName(categoryDTO.getName());
		return category;
	}
}
