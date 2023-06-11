package com.book.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.book.management.entity.Book;
import com.book.management.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	private Book book;

	@BeforeEach
	public void setup() {
		// Initialize test data
		book = new Book();
		book.setId(1);
		book.setName("Sample Book");
		book.setAuthor("Fahad Nadeem");
		book.setPrice(20);
	}

	@Test
	void save_ShouldReturnSavedBook() {
		// Arrange
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		// Act
		Book savedBook = bookService.save(book);

		// Assert
		verify(bookRepository, times(1)).save(any(Book.class));
		assertEquals("Sample Book", savedBook.getName());
	}

	@Test
	void findById_WithValidId_ShouldReturnBook() {
		// Arrange
		when(bookRepository.findById(1)).thenReturn(Optional.of(book));

		// Act
		Book foundBook = bookService.findById(1);

		// Assert
		verify(bookRepository, times(1)).findById(1);
		assertEquals("Sample Book", foundBook.getName());
	}

	@Test
	void findById_WithInvalidId_ShouldThrowException() {
		// Arrange
		when(bookRepository.findById(2)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(NoSuchElementException.class, () -> bookService.findById(2));
	}

	@Test
	void deleteById_ShouldCallRepositoryDeleteById() {
		// Act
		bookService.deleteById(1);

		// Assert
		verify(bookRepository, times(1)).deleteById(1);
	}
}
