package com.book.management.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.book.management.entity.CategoryEntity;
import com.book.management.mapper.CategoryMapper;
import com.book.management.model.CategoryDto;
import com.book.management.service.CategoryService;

@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerTest {

	@InjectMocks
	private CategoryController controller;

	@Mock
	private CategoryService service;

	private CategoryDto categoryDto;
	private Integer id;
	private NoSuchElementException exception;

	@Before
	public void setUp() {
		id = 1;
		categoryDto = new CategoryDto();
		exception = new NoSuchElementException("test exception");
	}

	@Test
	public void testGetAllCategories() {
		List<CategoryEntity> categoryEntities = Arrays.asList(new CategoryEntity(), new CategoryEntity());
		when(service.getAllCategories()).thenReturn(categoryEntities);

		List<CategoryDto> expectedCategories = categoryEntities.stream().map(CategoryMapper::mapToDto)
				.collect(Collectors.toList());

		List<CategoryDto> result = controller.getAllCategories();

		verify(service).getAllCategories();
		assertEquals(expectedCategories, result);
	}

	@Test
	public void testGetCategoryById_CategoryExists() {
		CategoryEntity categoryEntity = new CategoryEntity();
		when(service.getCategoryById(id)).thenReturn(categoryEntity);

		ResponseEntity<CategoryDto> response = controller.getCategoryById(id);

		verify(service).getCategoryById(id);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetCategoryById_CategoryNotFound() {
		when(service.getCategoryById(id)).thenThrow(exception);

		ResponseEntity<CategoryDto> response = controller.getCategoryById(id);

		verify(service).getCategoryById(id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testCreateCategory() {
		CategoryEntity categoryEntity = new CategoryEntity();
		when(service.saveCategory(any())).thenReturn(categoryEntity);

		ResponseEntity<CategoryDto> response = controller.createCategory(categoryDto);

		verify(service).saveCategory(any());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	public void testUpdateCategory_CategoryExists() {
		CategoryEntity categoryEntity = new CategoryEntity();
		when(service.updateCategory(anyInt(), any())).thenReturn(categoryEntity);

		ResponseEntity<CategoryDto> response = controller.updateCategory(id, categoryDto);

		verify(service).updateCategory(anyInt(), any());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	public void testUpdateCategory_CategoryNotFound() {
		when(service.updateCategory(anyInt(), any())).thenThrow(exception);

		ResponseEntity<CategoryDto> response = controller.updateCategory(id, categoryDto);

		verify(service).updateCategory(anyInt(), any());
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteCategory_CategoryExists() {
		ResponseEntity<Void> response = controller.deleteCategory(id);

		verify(service).deleteCategory(id);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testDeleteCategory_CategoryNotFound() {
		doThrow(exception).when(service).deleteCategory(id);

		ResponseEntity<Void> response = controller.deleteCategory(id);

		verify(service).deleteCategory(id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}
}
