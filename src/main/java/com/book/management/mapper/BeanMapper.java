package com.book.management.mapper;

import com.book.management.entity.BookEntity;
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
		return bookDto;
	}

	public static BookEntity mapToEntity(BookDto bookDto) {
		BookEntity bookEntity = new BookEntity();
		bookEntity.setId(bookDto.getId());
		bookEntity.setName(bookDto.getName());
		bookEntity.setAuthor(bookDto.getAuthor());
		bookEntity.setPrice(bookDto.getPrice());
		return bookEntity;
	}

}
