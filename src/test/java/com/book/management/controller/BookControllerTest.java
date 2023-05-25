package com.book.management.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import com.book.management.entity.Category;
import com.book.management.mapper.CategoryConverter;
import com.book.management.model.BookDTO;
import com.book.management.model.CategoryDTO;
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
        bookDTO.setAuthor("Fahad");
        bookDTO.setPrice(9);

        book = new Book();
        book.setId(1);
        book.setName("Sample Book");
        book.setAuthor("Fahad");
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

    }

  

    @Test
    void updateBook_ExistingBookId_ShouldReturnUpdatedBook() {
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
        BookDTO updatedBookDTO = response.getBody();
        assertEquals(bookDTO.getName(), updatedBookDTO.getName());
        assertEquals(bookDTO.getAuthor(), updatedBookDTO.getAuthor());
        assertEquals(bookDTO.getPrice(), updatedBookDTO.getPrice());

        verify(bookService, times(1)).findById(bookId);
        verify(bookService, times(1)).save(any(Book.class));
        verify(bookService).findById(eq(bookId));
        verify(bookService).save(any(Book.class));

        // Add assertions to check if the existing book object has been updated
        assertEquals(bookDTO.getName(), book.getName());
        assertEquals(bookDTO.getAuthor(), book.getAuthor());
        assertEquals(bookDTO.getPrice(), book.getPrice());

        // Check the category condition
        if (bookDTO.getCategory() != null) {
            assertNotNull(book.getCategory());

            // Convert the categoryDTO and assert the category ID
            CategoryDTO categoryDTO1 = bookDTO.getCategory();
            Category category = CategoryConverter.toEntity(categoryDTO1);
            assertEquals(category.getId(), book.getCategory().getId());
        } else {
            assertNull(book.getCategory());
        }
    }



    @Test
    public void deleteBook_ShouldReturnNoContent() {
        // Act
        ResponseEntity<Void> response = bookController.deleteBook(1);

        // Assert
        verify(bookService, times(1)).deleteById(1);
        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
     
    }
}

