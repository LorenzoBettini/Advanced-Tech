package com.book.management.mapper;

import com.book.management.entity.BookEntity;
import com.book.management.entity.CategoryEntity;
import com.book.management.model.BookDto;

public class BeanMapper {

	private BeanMapper() {

	}

	public static BookDto mapToDto(BookEntity bookEntity) {
		BookDto bookDto = new BookDto();
		bookDto.setId(bookEntity.getId());
		bookDto.setName(bookEntity.getName());
		bookDto.setAuthor(bookEntity.getAuthor());
		bookDto.setPrice(bookEntity.getPrice());

		if (bookEntity.getCategory() != null) {
			bookDto.setId(bookEntity.getCategory().getId());
			bookDto.setName(bookEntity.getCategory().getName());
		}
		return bookDto;
	}

	public static BookEntity mapToEntity(BookDto bookDto) {
		BookEntity bookEntity = new BookEntity();
		bookEntity.setId(bookDto.getId());
		bookEntity.setName(bookDto.getName());
		bookEntity.setAuthor(bookDto.getAuthor());

		if (bookDto.getId() != null) {
			CategoryEntity categoryEntity = new CategoryEntity();
			categoryEntity.setId(bookDto.getId());
			categoryEntity.setName(bookDto.getName());
			bookEntity.setCategory(categoryEntity);
		}
		return bookEntity;
	}
}
