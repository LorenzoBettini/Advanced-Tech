package com.book.management.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.model.BookDto;
import com.book.management.model.CategoryDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BookControllerTestIT {

	@Container
	private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7").withDatabaseName("mydb")
			.withUsername("root").withPassword("password");

	@Autowired
	private TestRestTemplate restTemplate;

	private BookDto book;
	private CategoryDto category;

	@Before
	public void setup() {
		category = new CategoryDto();
		category.setName("Test Category");

		book = new BookDto();
		book.setName("Test Book");
		book.setAuthor("Test Author");
		book.setPrice(19);
		book.setCategory(category);
	}

	@Test
	public void testSaveBook() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<BookDto> request = new HttpEntity<>(book, headers);

		ResponseEntity<BookDto> response = restTemplate.postForEntity("/books", request, BookDto.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getId());
		assertEquals(book.getName(), response.getBody().getName());
		assertEquals(book.getAuthor(), response.getBody().getAuthor());
		assertEquals(book.getPrice(), response.getBody().getPrice());
		assertNotNull(response.getBody().getCategory());
		assertEquals(category.getName(), response.getBody().getCategory().getName());
	}
	@Test
	public void testGetBook() {
		ResponseEntity<BookDto> savedBookResponse = restTemplate.postForEntity("/books", book, BookDto.class);
		Integer savedBookId = savedBookResponse.getBody().getId();
		assertNotNull(savedBookId);

		ResponseEntity<BookDto> response = restTemplate.getForEntity("/books/" + savedBookId, BookDto.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(savedBookId, response.getBody().getId());
		assertEquals(book.getName(), response.getBody().getName());
		assertEquals(book.getAuthor(), response.getBody().getAuthor());
		assertEquals(book.getPrice(), response.getBody().getPrice());
		assertNotNull(response.getBody().getCategory());
		assertEquals(category.getName(), response.getBody().getCategory().getName());
	}

	@Test
	public void testGetBooksByCategory() {
		ResponseEntity<BookDto> savedBookResponse = restTemplate.postForEntity("/books", book, BookDto.class);
		Integer savedBookId = savedBookResponse.getBody().getId();
		assertNotNull(savedBookId);

		ResponseEntity<BookDto[]> response = restTemplate.getForEntity("/books/category/" + category.getId(),
				BookDto[].class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().length);
		assertEquals(savedBookId, response.getBody()[0].getId());
		assertEquals(book.getName(), response.getBody()[0].getName());
		assertEquals(book.getAuthor(), response.getBody()[0].getAuthor());
		assertEquals(book.getPrice(), response.getBody()[0].getPrice());
		assertNotNull(response.getBody()[0].getCategory());
		assertEquals(category.getId(), response.getBody()[0].getCategory().getId());
		assertEquals(category.getName(), response.getBody()[0].getCategory().getName());
	}

	@Test
	public void testUpdateBook() {
		ResponseEntity<BookDto> savedBookResponse = restTemplate.postForEntity("/books", book, BookDto.class);
		Integer savedBookId = savedBookResponse.getBody().getId();
		assertNotNull(savedBookId);

		BookDto updatedBook = savedBookResponse.getBody();
		updatedBook.setName("Updated Book");
		updatedBook.setAuthor("Updated Author");
		updatedBook.setPrice(2999);

		restTemplate.put("/books/" + savedBookId, updatedBook);

		ResponseEntity<BookDto> response = restTemplate.getForEntity("/books/" + savedBookId, BookDto.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(savedBookId, response.getBody().getId());
		assertEquals(updatedBook.getName(), response.getBody().getName());
		assertEquals(updatedBook.getAuthor(), response.getBody().getAuthor());
		assertEquals(updatedBook.getPrice(), response.getBody().getPrice());
		assertNotNull(response.getBody().getCategory());
		assertEquals(category.getId(), response.getBody().getCategory().getId());
		assertEquals(category.getName(), response.getBody().getCategory().getName());
	}

	@Test
	public void testDeleteBook() {
		ResponseEntity<BookDto> savedBookResponse = restTemplate.postForEntity("/books", book, BookDto.class);
		Integer savedBookId = savedBookResponse.getBody().getId();
		assertNotNull(savedBookId);

		// Delete the book
		restTemplate.delete("/books/" + savedBookId);

		// Verify that the book is deleted
		ResponseEntity<BookDto> response = restTemplate.getForEntity("/books/" + savedBookId, BookDto.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}
}
