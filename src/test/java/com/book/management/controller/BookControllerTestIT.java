package com.book.management.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.model.BookDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Testcontainers
class BookControllerTestIT {

//	@Container
//	private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest");

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

	@Test
	void updateBook_ExistingBookId_ShouldReturnUpdatedBook() {
		// Arrange
		BookDTO bookDTO = new BookDTO();
		bookDTO.setName("Updated Book");
		bookDTO.setAuthor("Fahad");
		bookDTO.setPrice(2999);

		String url = "http://localhost:" + port + "/books/1";

		// Act
		HttpEntity<BookDTO> requestEntity = new HttpEntity<>(bookDTO);
		ResponseEntity<BookDTO> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, BookDTO.class);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		BookDTO updatedBookDTO = response.getBody();
		assertEquals(bookDTO.getName(), updatedBookDTO.getName());
		assertEquals(bookDTO.getAuthor(), updatedBookDTO.getAuthor());
		assertEquals(bookDTO.getPrice(), updatedBookDTO.getPrice());
	}

}