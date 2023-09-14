package com.book.management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.book.management.model.CategoryDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = CategoryControllerTestIT.Initializer.class)
public class CategoryControllerTestIT {

    @Container
    public static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.28"))
            .withDatabaseName("school").withUsername("test").withPassword("test");

    private Connection connection;

    @BeforeEach
    public void setup() throws SQLException {
        connection = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
    }

    @AfterEach
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
        assertNotNull(savedCategoryDTO);
        assertEquals(categoryDTO.getName(), savedCategoryDTO.getName());
    }

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
