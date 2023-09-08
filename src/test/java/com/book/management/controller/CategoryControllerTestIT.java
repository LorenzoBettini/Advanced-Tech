package com.book.management.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.model.CategoryDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
	private TestRestTemplate restTemplate;

	@Test
	void saveCategory_ShouldReturnSavedCategory() {
		// Arrange
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setName("Test Category");

		String url = "http://localhost:" + port + "/categories";

		// Act
		ResponseEntity<CategoryDTO> response = restTemplate.postForEntity(url, categoryDTO, CategoryDTO.class);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		CategoryDTO savedCategoryDTO = response.getBody();
		assertEquals(categoryDTO.getName(), savedCategoryDTO.getName());
	}

	@Test
	void getCategoryById_ExistingCategoryId_ShouldReturnCategory() {
		// Arrange
		CategoryDTO expectedCategoryDTO = new CategoryDTO();
		expectedCategoryDTO.setId(1L);
		expectedCategoryDTO.setName("Test Category");

		String url = "http://localhost:" + port + "/categories/1";

		// Act
		ResponseEntity<CategoryDTO> response = restTemplate.getForEntity(url, CategoryDTO.class);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		CategoryDTO categoryDTO = response.getBody();
		assertEquals(expectedCategoryDTO.getId(), categoryDTO.getId());
		assertEquals(expectedCategoryDTO.getName(), categoryDTO.getName());
	}

}
