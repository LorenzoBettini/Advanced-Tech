package com.book.management.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.book.management.entity.Book;
import com.book.management.repository.BookRepository;

@Service
public class BookService {
	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Book save(Book book) {
		return bookRepository.save(book);
	}

	public Book findById(Integer id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Book not found with id: " + id));
	}

	public void deleteById(Integer id) {
		bookRepository.deleteById(id);
	}
}
