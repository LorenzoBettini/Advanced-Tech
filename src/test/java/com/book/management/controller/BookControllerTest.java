package com.book.management.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.book.management.entity.Book;
import com.book.management.entity.Category;
import com.book.management.mapper.CategoryConverter;
import com.book.management.model.BookDTO;
import com.book.management.model.CategoryDTO;
import com.book.management.service.BookService;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
	@Mock
	private BookService bookService;

	@InjectMocks
	private BookController bookController;

	private BookDTO bookDTO;
	private Book book;

	@BeforeEach
	void setup() {
		// Initialize test data
		bookDTO = new BookDTO();
		bookDTO.setName("Sample Book");
		bookDTO.setAuthor("Fahad");
		bookDTO.setPrice(9);

		book = new Book();
		book.setId(1);
		book.setName("Sample Book");
		book.setAuthor("Fahad");
		book.setPrice(9);
	}

	@Test
	void saveBook_WithValidBookDTO_ShouldReturnSavedBook() {
		// Arrange
		when(bookService.save(any(Book.class))).thenReturn(book);

		// Act
		ResponseEntity<BookDTO> response = bookController.saveBook(bookDTO);

		// Assert
		verify(bookService, times(1)).save(any(Book.class));
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Sample Book", response.getBody().getName());
	}

	@Test
	void saveBook_WithNullBookDTO_ShouldReturnBadRequest() {
		// Arrange

		// Act
		ResponseEntity<BookDTO> response = bookController.saveBook(null);

		// Assert
		verify(bookService, never()).save(any(Book.class));
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void saveBook_WithIncompleteBookDTO_ShouldReturnBadRequest() {
		// Arrange
		bookDTO.setName("Sample Book");
		bookDTO.setAuthor(null);
		bookDTO.setPrice(9);

		// Act
		ResponseEntity<BookDTO> response = bookController.saveBook(bookDTO);

		// Assert
		verify(bookService, never()).save(any(Book.class));
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void getBookById_WithValidId_ShouldReturnBook() {
		// Arrange
		when(bookService.findById(1)).thenReturn(book);

		// Act
		ResponseEntity<BookDTO> response = bookController.getBookById(1);

		// Assert
		verify(bookService, times(1)).findById(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Sample Book", response.getBody().getName());
	}

	@Test
	void getBookById_WithInvalidId_ShouldReturnNotFound() {
		// Arrange
		when(bookService.findById(2)).thenReturn(null);

		// Act
		ResponseEntity<BookDTO> response = bookController.getBookById(2);

		// Assert
		verify(bookService, times(1)).findById(2);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void updateBook_WithExistingBookIdAndValidBookDTO_ShouldReturnUpdatedBook() {
		// Arrange
		Integer bookId = 1;
		when(bookService.findById(bookId)).thenReturn(book);
		when(bookService.save(any(Book.class))).thenReturn(book);

		// Set the category in the bookDTO
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(1L);
		categoryDTO.setName("Sample Category");
		bookDTO.setCategory(categoryDTO);

		// Act
		ResponseEntity<BookDTO> response = bookController.updateBook(bookId, bookDTO);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Sample Book", response.getBody().getName());

		assertEquals("Fahad", response.getBody().getAuthor());

		verify(bookService, times(1)).findById(bookId);
		verify(bookService, times(1)).save(any(Book.class));
		verify(bookService).findById(eq(bookId));
		verify(bookService).save(any(Book.class));

		// Add assertions to check if the existing book object has been updated
		assertEquals("Sample Book", book.getName());
		assertEquals("Fahad", book.getAuthor());

		// Check the category condition
		assertNotNull(book.getCategory());
		CategoryDTO categoryDTO1 = bookDTO.getCategory();
		Category category = CategoryConverter.toEntity(categoryDTO1);
		assertEquals(category.getId(), book.getCategory().getId());
	}

	@Test
	void updateBook_WithNonExistingBookId_ShouldReturnNotFound() {
		// Arrange
		Integer bookId = 2;
		when(bookService.findById(bookId)).thenReturn(null);

		// Act
		ResponseEntity<BookDTO> response = bookController.updateBook(bookId, bookDTO);

		// Assert
		verify(bookService, times(1)).findById(bookId);
		verify(bookService, never()).save(any(Book.class));
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void deleteBook_WithExistingBookId_ShouldReturnNoContent() {
		// Act
		ResponseEntity<Void> response = bookController.deleteBook(1);

		// Assert
		verify(bookService, times(1)).deleteById(1);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());
	}

}
