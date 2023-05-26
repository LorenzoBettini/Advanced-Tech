package com.book.management.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.model.CategoryDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CategoryControllerTestIT {
	@Container
	private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest").withDatabaseName("mydb")
			.withUsername("root").withPassword("password");

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeAll
	static void beforeAll() {
		mysql.start();
	}

	@AfterAll
	static void afterAll() {
		mysql.stop();
	}

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
