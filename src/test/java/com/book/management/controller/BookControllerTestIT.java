package com.book.management.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.model.BookDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class BookControllerTestIT {

	@Container
	public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.1.0").withUsername("bookman")
			.withPassword("bookman").withDatabaseName("bookman");

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void saveBook_ShouldReturnSavedBook() {
		// Arrange
		BookDTO bookDTO = new BookDTO();
		bookDTO.setName("Test Book");
		bookDTO.setAuthor("Fahad");
		bookDTO.setPrice(19);

		String url = "http://localhost:" + port + "/books";

		// Act
		ResponseEntity<BookDTO> response = restTemplate.postForEntity(url, bookDTO, BookDTO.class);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		BookDTO savedBookDTO = response.getBody();
		assertEquals(bookDTO.getName(), savedBookDTO.getName());
		assertEquals(bookDTO.getAuthor(), savedBookDTO.getAuthor());
		assertEquals(bookDTO.getPrice(), savedBookDTO.getPrice());
	}

}