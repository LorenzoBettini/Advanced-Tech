package com.book.management.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.book.management.model.BookDto;
import com.book.management.service.BookService;
import static org.mockito.ArgumentMatchers.anyInt;
@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {
    @InjectMocks
    private BookController controller;

    @Mock
    private BookService service;

    private BookDto book;
    private Integer id;
    private NoSuchElementException exception;

    @Before
    public void beforeClass() {
        id = 1;
        book = new BookDto();
        exception = new NoSuchElementException("test exception");
    }


    @Test
    public void testGetBook() {
        when(service.getBook(any())).thenReturn(book);
        BookDto result = controller.getBook(id);
        verify(service).getBook(any());
        assertEquals(book, result);
    }

    @Test
    public void testGetBooksByCategory() {
        List<BookDto> books = Arrays.asList(new BookDto(), new BookDto());
        when(service.getBooksByCategory(anyInt())).thenReturn(books);
        List<BookDto> result = controller.getBooksByCategory(1);
        verify(service).getBooksByCategory(anyInt());
        assertEquals(books, result);
    }
    @Test
    public void testGetBookNotFound() {
        when(service.getBook(any())).thenThrow(exception);
        NoSuchElementException result = assertThrows(NoSuchElementException.class, () -> controller.getBook(1));
        verify(service).getBook(any());
        assertEquals(exception.getMessage(), result.getMessage());
    }

    @Test
    public void testSaveBook() {
        when(service.saveBook(any())).thenReturn(book);
        BookDto result = controller.saveBook(book);
        verify(service).saveBook(any());
        assertEquals(book, result);
    }

    @Test
    public void testUpdateBook() {
    	book.setId(id);
        when(service.updateBook(any(), any())).thenReturn(book);
        BookDto result = controller.updateBook(id, book);
        verify(service).updateBook(any(), any());
        assertEquals(book, result);
    }

    @Test
    public void testUpdateBookNotFound() {
    	book.setId(id);
        when(service.updateBook(any(), any())).thenThrow(exception);
        NoSuchElementException result = assertThrows(NoSuchElementException.class, () -> controller.updateBook(id, book));
        verify(service).updateBook(any(), any());
        assertEquals(exception.getMessage(), result.getMessage());
    }

    @Test
    public void testUpdateBookDifferentId() {
        book.setId(id + 1);
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> controller.updateBook(id, book));
        verify(service, never()).updateBook(any(), any());
        assertEquals(
        	String.format("Id for url (%d) and dto (%d) must be the same", id, book.getId()),
        	result.getMessage());
    }

    @Test
    public void testDeleteBook() {
        Boolean result = controller.deleteBook(id);
        verify(service).deleteBook(any());
        assertTrue(result);
    }

    @Test
    public void testDeleteBookNotFound() {
        doThrow(exception).when(service).deleteBook(any());
        NoSuchElementException result = assertThrows(NoSuchElementException.class, () -> controller.deleteBook(id));
        verify(service).deleteBook(any());
        assertEquals(exception.getMessage(), result.getMessage());
    }
}
