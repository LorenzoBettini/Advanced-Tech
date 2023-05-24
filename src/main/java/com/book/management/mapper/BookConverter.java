package com.book.management.mapper;

import com.book.management.entity.Book;
import com.book.management.entity.Category;
import com.book.management.model.BookDTO;
import com.book.management.model.CategoryDTO;

public class BookConverter {

	public static BookDTO toDTO(Book book) {
		BookDTO bookDTO = new BookDTO();
		bookDTO.setId(book.getId());
		bookDTO.setName(book.getName());
		bookDTO.setAuthor(book.getAuthor());
		bookDTO.setPrice(book.getPrice());

		Category category = book.getCategory();
		if (category != null) {
			bookDTO.setCategory(CategoryConverter.toDTO(category));
		}

		return bookDTO;
	}

	public static Book toEntity(BookDTO bookDTO) {
		Book book = new Book();
		book.setId(bookDTO.getId());
		book.setName(bookDTO.getName());
		book.setAuthor(bookDTO.getAuthor());
		book.setPrice(bookDTO.getPrice());

		CategoryDTO categoryDTO = bookDTO.getCategory();
		if (categoryDTO != null) {
			book.setCategory(CategoryConverter.toEntity(categoryDTO));
		}

		return book;
	}
}
