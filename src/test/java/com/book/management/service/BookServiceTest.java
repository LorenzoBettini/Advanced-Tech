package com.book.management.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.book.management.entity.BookEntity;
import com.book.management.mapper.BeanMapper;
import com.book.management.model.BookDto;
import com.book.management.repository.BookRepository;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

	@InjectMocks
	private BookService service;

	@Mock
	private BookRepository repository;

	@Mock
	private BeanMapper mapper;

	@Test
	public void testGetBook() {

		BookEntity testBook = new BookEntity();
		testBook.setId(1);
		testBook.setName("Test Book");
		testBook.setAuthor("Test Author");

		// Mock the book repository's findById method to return the test book
		when(repository.findById(testBook.getId())).thenReturn(Optional.of(testBook));

		// Call the getBook method with the test book's ID
		BookDto bookDto = service.getBook(1);

		// Verify that the returned book DTO is not null and has the correct title and
		// author
		assertNotNull(bookDto);
		assertEquals("Test Book", bookDto.getName());
		assertEquals("Test Author", bookDto.getAuthor());
	}

	@Test
	public void testGetBook_NotFound() {
		// Arrange
		Integer id = 1;
		when(repository.findById(id)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(NoSuchElementException.class, () -> service.getBook(id));
	}

	@Test
	public void saveBook_shouldReturnSavedBook() {
		// given
		BookDto bookDto = new BookDto(1, "Title", "Author", 1000);

		BookEntity savedBookEntity = new BookEntity();
		savedBookEntity.setId(1);
		savedBookEntity.setName("Title");
		savedBookEntity.setAuthor("Author");
		savedBookEntity.setPrice(1000);

		when(repository.save(any(BookEntity.class))).thenReturn(savedBookEntity);

		// when
		BookDto savedBookDto = service.saveBook(bookDto);

		// then
		assertEquals(savedBookDto.getId(), savedBookEntity.getId());
		assertEquals(savedBookDto.getName(), savedBookEntity.getName());
		assertEquals(savedBookDto.getAuthor(), savedBookEntity.getAuthor());
		assertEquals(savedBookDto.getPrice(), savedBookEntity.getPrice());
	}

	@Test
	public void testUpdateBook() {
		Integer id = 1;
		BookEntity bookEntity = new BookEntity();
		bookEntity.setId(id);
		bookEntity.setName("Test Book");
		bookEntity.setAuthor("Test Author");
		bookEntity.setPrice(1000);
		when(repository.existsById(id)).thenReturn(true);
		when(repository.save(any(BookEntity.class))).thenReturn(bookEntity);
		BookDto bookDto = new BookDto();
		bookDto.setId(1);
		bookDto.setName("Test Book");
		bookDto.setAuthor("Test Author");
		bookDto.setPrice(1000);
		BookDto updatedBook = service.updateBook(id, bookDto);
		assertNotNull(updatedBook);
		assertEquals(id, updatedBook.getId());
		assertEquals("Test Book", updatedBook.getName());
		assertEquals("Test Author", updatedBook.getAuthor());
		assertEquals(bookEntity.getPrice(), updatedBook.getPrice());
	}

	@Test(expected = NoSuchElementException.class)
	public void testUpdateBook_WhenNotExists() {
		// Arrange
		Integer id = 1;
		BookDto input = new BookDto();
		when(repository.existsById(id)).thenReturn(false);

		// Act
		service.updateBook(id, input);
	}

	@Test
	public void testDeleteBook_WhenExists() {
		// Arrange
		Integer id = 1;
		when(repository.existsById(id)).thenReturn(true);
		doNothing().when(repository).deleteById(id);
		// Act
		service.deleteBook(id);

		// Assert
		Mockito.verify(repository).deleteById(id);
		Mockito.verify(repository).existsById(id);
	}

	@Test(expected = NoSuchElementException.class)
	public void testDeleteBook_WhenNotExists() {
		// Arrange
		Integer id = 1;
		when(repository.existsById(id)).thenReturn(false);

		// Act
		service.deleteBook(id);

		// Act and Assert
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.deleteBook(id));
		assertEquals("Book you selected is not Present with " + id, exception.getMessage());
	}
}
