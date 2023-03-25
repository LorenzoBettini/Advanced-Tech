package com.book.management.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.management.entity.BookEntity;
import com.book.management.mapper.BeanMapper;
import com.book.management.model.BookDto;
import com.book.management.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<BookDto> getBooks() {
	    List<BookEntity> books = bookRepository.findAll();
	    return books.stream()
	                .map(BeanMapper::mapToDto)
	                .collect(Collectors.toList());
	}

	public BookDto getBook(Integer id) {
		BookEntity book = bookRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("There is no Book Found with " + id));
		return BeanMapper.mapToDto(book);
	}

	public BookDto saveBook(BookDto book) {
		BookEntity newBook = BeanMapper.mapToEntity(book);
		return BeanMapper.mapToDto(bookRepository.save(newBook));
	}

	public BookDto updateBook(Integer id, BookDto book) {
		if (!bookRepository.existsById(id)) {
			throw new NoSuchElementException("Please insert correct Id " + id);
		}
		BookEntity updatedBook = bookRepository.save(BeanMapper.mapToEntity(book));
		return BeanMapper.mapToDto(bookRepository.save(updatedBook));
	}

	public void deleteBook(Integer id) {
		if (!bookRepository.existsById(id)) {
			throw new NoSuchElementException("Book you selected is not Present with " + id);
		}
		bookRepository.deleteById(id);
	}
}