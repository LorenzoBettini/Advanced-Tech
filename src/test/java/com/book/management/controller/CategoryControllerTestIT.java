package com.book.management.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.entity.Category;
import com.book.management.model.CategoryDTO;
import com.book.management.repository.CategoryRepository;
import com.book.management.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CategoryControllerTestIT {
	@Container
	public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26").withUsername("bookman")
			.withPassword("Passman").withDatabaseName("bookman").waitingFor(Wait.forListeningPort())
			.withEnv("MYSQL_ROOT_HOST", "%");

	@Autowired
	DataSource dataSource;

	@LocalServerPort
	private int port;

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
		registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
		System.out.println("************************************************");
		System.out.println(mySQLContainer.getJdbcUrl());
	}

	@AfterEach
	void tearDown() {
		if (dataSource instanceof HikariDataSource) {
			((HikariDataSource) dataSource).close();
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost:" + port;
		categoryRepository.deleteAll();
	}

	@BeforeAll
	static void beforeAll() {
		mySQLContainer.start();
	}

	@AfterAll
	static void afterAll() {
		mySQLContainer.stop();
	}

	@Test
	void test() {
		assertTrue(mySQLContainer.isRunning());
	}

	@Test
	void saveCategory_ShouldReturnSavedCategory() throws Exception {
		// Arrange
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setName("fahad");

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/categories").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(categoryDTO))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryDTO.getName()));
	}

	@Test
	void getCategoryById_ExistingCategoryId_ShouldReturnCategory() throws Exception {
		// Arrange
		Category category = new Category();
		category.setName("fahad");
		category = categoryService.save(category);
		Long categoryId = category.getId();

		// Act & Assert
		mockMvc.perform(
				MockMvcRequestBuilders.get("/categories/{id}", categoryId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(categoryId))
				.andExpect(jsonPath("$.name").value(category.getName()));
	}
//	 @Test
//	void testFindAll() {
//		 Category category = new Category();
//			category.setName("fahad");
//	 assertThat(categoryRepository.findAll()).containsAnyOf(category);
//	
//}

	@Test
	public void testFindAll() {
		// Arrange: Create test categories and save them to the repository
		Category category1 = new Category();
		category1.setName("Test Category 1");
		Category category2 = new Category();
		category2.setName("Test Category 2");

		categoryRepository.save(category1);
		categoryRepository.save(category2);

		// Act: Retrieve all categories from the repository
		List<Category> categories = categoryRepository.findAll();

		// Assert: Check if the retrieved categories match the expected categories
		assertThat(categories).extracting(Category::getName) // Extract the names for comparison
				.containsExactlyInAnyOrder("Test Category 1", "Test Category 2");
	}

//	@Test
//	public void testSaveCategory() throws Exception {
//		// Arrange: Create a CategoryDTO to save
//		CategoryDTO categoryDTO = new CategoryDTO();
//		categoryDTO.setName("Test Category");
//
//		// Act: Perform a POST request to save the category
//		mockMvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(categoryDTO))).andExpect(status().isOk())
//				.andExpect(jsonPath("$.name").value(categoryDTO.getName()));
//
//		// Assert: Check if the category was saved in the repository
//		List<Category> categories = categoryRepository.findAll();
//		assertThat(categories).hasSize(1);
//		assertThat(categories.get(0).getName()).isEqualTo(categoryDTO.getName());
//	}
//
//	@Test
//	public void testGetCategoryById() throws Exception {
//		// Arrange: Save a category to the repository
//		Category category = new Category();
//		category.setName("Test Category");
//		category = categoryRepository.save(category);
//
//		// Act: Perform a GET request to retrieve the category by ID
//		mockMvc.perform(get("/categories/{id}", category.getId()).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(category.getId()))
//				.andExpect(jsonPath("$.name").value(category.getName()));
//	}
//
//	@Test
//	public void testDeleteCategory() throws Exception {
//		// Arrange: Save a category to the repository
//		Category category = new Category();
//		category.setName("Test Category");
//		category = categoryRepository.save(category);
//
//		// Act: Perform a DELETE request to delete the category by ID
//		mockMvc.perform(delete("/categories/{id}", category.getId()).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isNoContent());
//
//		// Assert: Check if the category was deleted from the repository
//		assertThat(categoryRepository.findById(category.getId())).isEmpty();
//	}
}
