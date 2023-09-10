package com.book.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.entity.Category;
import com.book.management.model.CategoryDTO;
import com.book.management.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CategoryControllerTestIT {
	@Container
	public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.1.0").withUsername("FahadNadeem")
			.withPassword("Book123").withDatabaseName("bookmanagement");

	@LocalServerPort
	private int port;

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
		registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void saveCategory_ShouldReturnSavedCategory() throws Exception {
		// Arrange
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setName("Test Category");

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/categories").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(categoryDTO))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryDTO.getName()));
	}

	@Test
	void getCategoryById_ExistingCategoryId_ShouldReturnCategory() throws Exception {
		// Arrange
		Category category = new Category();
		category.setName("Test Category");
		category = categoryService.save(category);
		Long categoryId = category.getId();

		// Act & Assert
		mockMvc.perform(
				MockMvcRequestBuilders.get("/categories/{id}", categoryId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(categoryId))
				.andExpect(jsonPath("$.name").value(category.getName()));
	}
}
