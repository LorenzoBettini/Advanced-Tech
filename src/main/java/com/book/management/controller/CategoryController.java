package com.book.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.management.entity.Category;
import com.book.management.mapper.CategoryConverter;
import com.book.management.model.CategoryDTO;
import com.book.management.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping
	public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
		Category category = CategoryConverter.toEntity(categoryDTO);
		Category savedCategory = categoryService.save(category);
		CategoryDTO savedCategoryDTO = CategoryConverter.toDTO(savedCategory);
		return ResponseEntity.ok(savedCategoryDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
		Category category = categoryService.findById(id);
		CategoryDTO categoryDTO = CategoryConverter.toDTO(category);
		return ResponseEntity.ok(categoryDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
		Category existingCategory = categoryService.findById(id);
		existingCategory.setName(categoryDTO.getName());

		Category updatedCategory = categoryService.save(existingCategory);
		CategoryDTO updatedCategoryDTO = CategoryConverter.toDTO(updatedCategory);
		return ResponseEntity.ok(updatedCategoryDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}