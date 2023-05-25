package com.book.management.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.book.management.entity.Book;
import com.book.management.model.BookDTO;
import com.book.management.service.BookService;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO bookDTO;
    private Book book;

    @BeforeEach
    public void setup() {
        // Initialize test data
        bookDTO = new BookDTO();
        bookDTO.setName("Sample Book");
        bookDTO.setAuthor("John Doe");
        bookDTO.setPrice(9);

        book = new Book();
        book.setId(1);
        book.setName("Sample Book");
        book.setAuthor("John Doe");
        book.setPrice(9);
    }

    @Test
    public void saveBook_ShouldReturnSavedBook() {
        // Arrange
        when(bookService.save(any(Book.class))).thenReturn(book);

        // Act
        ResponseEntity<BookDTO> response = bookController.saveBook(bookDTO);

        // Assert
        verify(bookService, times(1)).save(any(Book.class));
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getName().equals("Sample Book");
        // Add more assertions as needed
    }

    @Test
    public void getBookById_ShouldReturnBook() {
        // Arrange
        when(bookService.findById(1)).thenReturn(book);

        // Act
        ResponseEntity<BookDTO> response = bookController.getBookById(1);

        // Assert
        verify(bookService, times(1)).findById(1);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getName().equals("Sample Book");
        // Add more assertions as needed
    }

    @Test
    public void updateBook_ShouldReturnUpdatedBook() {
        // Arrange
        when(bookService.findById(1)).thenReturn(book);
        when(bookService.save(any(Book.class))).thenReturn(book);

        // Act
        ResponseEntity<BookDTO> response = bookController.updateBook(1, bookDTO);

        // Assert
        verify(bookService, times(1)).findById(1);
        verify(bookService, times(1)).save(any(Book.class));
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getName().equals("Sample Book");
        // Add more assertions as needed
    }

    @Test
    public void deleteBook_ShouldReturnNoContent() {
        // Act
        ResponseEntity<Void> response = bookController.deleteBook(1);

        // Assert
        verify(bookService, times(1)).deleteById(1);
        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
        // Add more assertions as needed
    }
}

