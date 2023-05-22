package com.book.management.mapper;

import java.util.Collections;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.book.management.entity.BookEntity;
import com.book.management.entity.CategoryEntity;
import com.book.management.model.BookDto;
import com.book.management.model.CategoryDto;

@Component
public class CategoryMapper {

	public static CategoryDto mapToDto(CategoryEntity categoryEntity) {
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(categoryEntity.getId());
		categoryDto.setName(categoryEntity.getName());
		categoryDto.setBooks(mapBooksToDto(categoryEntity.getBooks()));
		return categoryDto;
	}

	public static CategoryEntity mapToEntity(CategoryDto categoryDto) {
		CategoryEntity categoryEntity = new CategoryEntity();
		categoryEntity.setId(categoryDto.getId());
		categoryEntity.setName(categoryDto.getName());
		categoryEntity.setBooks(mapBooksToEntity(categoryDto.getBooks()));
		return categoryEntity;
	}

	private static List<BookDto> mapBooksToDto(List<BookEntity> bookEntities) {
		if (bookEntities == null) {
			return Collections.emptyList();
		}
		return bookEntities.stream().map(BeanMapper::mapToDto).collect(Collectors.toList());
	}

	private static List<BookEntity> mapBooksToEntity(List<BookDto> bookDtos) {
		if (bookDtos == null) {
			return Collections.emptyList();
		}
		return bookDtos.stream().map(BeanMapper::mapToEntity).collect(Collectors.toList());
	}
}
