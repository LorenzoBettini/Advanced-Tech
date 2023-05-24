package com.book.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.management.entity.Book;
import com.book.management.entity.Category;
import com.book.management.mapper.BookConverter;
import com.book.management.mapper.CategoryConverter;
import com.book.management.model.BookDTO;
import com.book.management.model.CategoryDTO;
import com.book.management.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	public ResponseEntity<BookDTO> saveBook(@RequestBody BookDTO bookDTO) {
		Book book = BookConverter.toEntity(bookDTO);
		Book savedBook = bookService.save(book);
		BookDTO savedBookDTO = BookConverter.toDTO(savedBook);
		return ResponseEntity.ok(savedBookDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable Integer id) {
		Book book = bookService.findById(id);
		BookDTO bookDTO = BookConverter.toDTO(book);
		return ResponseEntity.ok(bookDTO);
	}

	@PutMapping("/{id}")
	public ResponseEntity<BookDTO> updateBook(@PathVariable Integer id, @RequestBody BookDTO bookDTO) {
		Book existingBook = bookService.findById(id);
		existingBook.setName(bookDTO.getName());
		existingBook.setAuthor(bookDTO.getAuthor());
		existingBook.setPrice(bookDTO.getPrice());

		CategoryDTO categoryDTO = bookDTO.getCategory();
		if (categoryDTO != null) {
			Category category = CategoryConverter.toEntity(categoryDTO);
			existingBook.setCategory(category);
		}

		Book updatedBook = bookService.save(existingBook);
		BookDTO updatedBookDTO = BookConverter.toDTO(updatedBook);
		return ResponseEntity.ok(updatedBookDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
		bookService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}