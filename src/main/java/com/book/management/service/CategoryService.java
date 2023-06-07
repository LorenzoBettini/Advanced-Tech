package com.book.management.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.book.management.entity.Category;
import com.book.management.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	public Category findById(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
	}

	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}
	
	
}