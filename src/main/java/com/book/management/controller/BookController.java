package com.book.management.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.management.model.BookDto;
import com.book.management.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	public List<BookDto> getBooks() {
		return bookService.getBooks();
	}

	@GetMapping("/category/{categoryId}")
	public List<BookDto> getBooksByCategory(@PathVariable("categoryId") Integer categoryId) {
		return bookService.getBooksByCategory(categoryId);
	}

	@GetMapping("/{id}")
	public BookDto getBook(@PathVariable("id") Integer id) {
		return bookService.getBook(id);
	}

	@PostMapping
	public BookDto saveBook(@RequestBody @Valid BookDto book) {
		return bookService.saveBook(book);
	}

	@PutMapping("/{id}")
	public BookDto updateBook(@PathVariable("id") Integer id, @RequestBody @Valid BookDto book) {
		if (!id.equals(book.getId())) {
			throw new IllegalArgumentException(
					String.format("Id for url (%d) and dto (%d) must be the same", id, book.getId()));
		}
		return bookService.updateBook(id, book);
	}

	@DeleteMapping("/{id}")
	public Boolean deleteBook(@PathVariable("id") Integer id) {
		bookService.deleteBook(id);
		return true;
	}
}