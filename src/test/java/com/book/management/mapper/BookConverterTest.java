package com.book.management.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.book.management.entity.Book;
import com.book.management.entity.Category;
import com.book.management.model.BookDTO;
import com.book.management.model.CategoryDTO;

class BookConverterTest {
	@Test
	void toDTO_ShouldConvertBookToBookDTO() {
		// Arrange
		Book book = new Book();
		book.setId(1);
		book.setName("Test Book");
		book.setAuthor("John Doe");
		book.setPrice(19);
		Category category = new Category();
		category.setId(1L);
		category.setName("Fiction");
		book.setCategory(category);

		// Act
		BookDTO bookDTO = BookConverter.toDTO(book);

		// Assert
		Assertions.assertEquals(book.getId(), bookDTO.getId());
		Assertions.assertEquals(book.getName(), bookDTO.getName());
		Assertions.assertEquals(book.getAuthor(), bookDTO.getAuthor());
		Assertions.assertEquals(book.getPrice(), bookDTO.getPrice());
		Assertions.assertEquals(book.getCategory().getId(), bookDTO.getCategory().getId());
		Assertions.assertEquals(book.getCategory().getName(), bookDTO.getCategory().getName());
	}

	@Test
	void toEntity_ShouldConvertBookDTOToBook() {
		// Arrange
		BookDTO bookDTO = new BookDTO();
		bookDTO.setId(1);
		bookDTO.setName("Test Book");
		bookDTO.setAuthor("John Doe");
		bookDTO.setPrice(19);
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(1L);
		categoryDTO.setName("Fiction");
		bookDTO.setCategory(categoryDTO);

		// Act
		Book book = BookConverter.toEntity(bookDTO);

		// Assert
		Assertions.assertEquals(bookDTO.getId(), book.getId());
		Assertions.assertEquals(bookDTO.getName(), book.getName());
		Assertions.assertEquals(bookDTO.getAuthor(), book.getAuthor());
		Assertions.assertEquals(bookDTO.getPrice(), book.getPrice());
		Assertions.assertEquals(bookDTO.getCategory().getId(), book.getCategory().getId());
		Assertions.assertEquals(bookDTO.getCategory().getName(), book.getCategory().getName());
	}
}
