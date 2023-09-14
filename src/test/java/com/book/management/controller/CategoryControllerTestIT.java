package com.book.management.controller;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.book.management.model.CategoryDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = CategoryControllerTestIT.Initializer.class)
public class CategoryControllerTestIT {

	@ClassRule
	public static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.28"))
			.withDatabaseName("school").withUsername("test").withPassword("test");

	private Connection connection;

	@Before
	public void setup() throws SQLException {
		connection = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
//        createTestDatabase();
	}

	@After
	public void tearDown() throws SQLException {
		connection.close();
	}

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void saveCategory_ShouldReturnSavedCategory() {
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

//    private void createTestDatabase() throws SQLException {
//        Statement statement = connection.createStatement();
//        statement.execute("CREATE DATABASE IF NOT EXISTS categories");
//        statement.close();
//    }

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.datasource.url=" + mysql.getJdbcUrl(),
							"spring.datasource.username=" + mysql.getUsername(),
							"spring.datasource.password=" + mysql.getPassword())
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}
}
