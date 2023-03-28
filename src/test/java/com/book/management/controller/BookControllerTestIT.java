package com.book.management.controller;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.book.management.entity.BookEntity;
import com.book.management.model.BookDto;
import com.book.management.repository.BookRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BookControllerTestIT {

	@Container
	private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:5.7").withDatabaseName("mydb")
			.withUsername("root").withPassword("password");

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void testGetBooks() {
		// Given
		BookEntity book1 = new BookEntity(1, "Book 1", "Author 1", 100);
		BookEntity book2 = new BookEntity(2, "Book 2", "Author 2", 200);
		bookRepository.save(book1);
		bookRepository.save(book2);

		// When
		ResponseEntity<List<BookDto>> response = restTemplate.exchange("/books", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<BookDto>>() {
				});

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).hasSize(2);
		assertThat(response.getBody().get(0)).isEqualTo(new BookDto(1, "Book 1", "Author 1", 100));
		assertThat(response.getBody().get(1)).isEqualTo(new BookDto(2, "Book 2", "Author 2", 200));
	}

	@Test
	public void testGetBook() {
		// Given
		BookEntity book = new BookEntity(1, "Book 1", "Author 1", 100);
		book = bookRepository.save(book);

		// When
		ResponseEntity<BookDto> response = restTemplate.getForEntity("/books/" + book.getId(), BookDto.class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(new BookDto(1, "Book 1", "Author 1", 100));
	}

	@Test
	public void testSaveBook() {
		// Given
		BookDto bookDto = new BookDto(1, "Book 1", "Author 1", 100);

		// When
		ResponseEntity<BookDto> response = restTemplate.postForEntity("/books", bookDto, BookDto.class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getId()).isNotNull();

		Optional<BookEntity> savedBook = bookRepository.findById(response.getBody().getId());
		assertThat(savedBook).isPresent();
		assertThat(savedBook.get().getName()).isEqualTo(bookDto.getName());
		assertThat(savedBook.get().getAuthor()).isEqualTo(bookDto.getAuthor());
	}

	@Test
	public void testUpdateBook() {
		// Given
		BookEntity book = new BookEntity(1, "Book 1", "Author 1", 100);
		book = bookRepository.save(book);

		BookDto updatedBookDto = new BookDto(1, "Book 1", "Author 1", 100);
		updatedBookDto.setName("New Title");
		updatedBookDto.setAuthor("New Author");

		// When
		ResponseEntity<BookDto> response = restTemplate.exchange("/books/" + book.getId(), HttpMethod.PUT,
				new HttpEntity<>(updatedBookDto), BookDto.class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		Optional<BookEntity> updatedBook = bookRepository.findById(book.getId());
		assertThat(updatedBook).isPresent();
		assertThat(updatedBook.get().getName()).isEqualTo(updatedBookDto.getName());
		assertThat(updatedBook.get().getAuthor()).isEqualTo(updatedBookDto.getAuthor());
	}
	
	@Test
	public void testDeleteBook() {
	    // Given
		BookEntity book = new BookEntity(1, "Book 1", "Author 1", 100);
	    book = bookRepository.save(book);

	    // When
	    ResponseEntity<Boolean> response = restTemplate.exchange("/books/" + book.getId(), HttpMethod.DELETE, null,
	            Boolean.class);

	    // Then
	    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	    assertThat(response.getBody()).isEqualTo(true);

	    Optional<BookEntity> deletedBook = bookRepository.findById(book.getId());
	    assertThat(deletedBook).isEmpty();
	}
}
