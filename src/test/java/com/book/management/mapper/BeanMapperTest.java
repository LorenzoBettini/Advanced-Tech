package com.book.management.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.book.management.entity.BookEntity;
import com.book.management.model.BookDto;

@RunWith(MockitoJUnitRunner.class)
public class BeanMapperTest {

	@Test
	public void testConvertEntityToDto() {
		BookEntity bookEntity = new BookEntity();
		bookEntity.setId(1);
		bookEntity.setName("Book name");
		bookEntity.setAuthor("Author name");
		bookEntity.setPrice(10);

		BookDto bookDto = BeanMapper.mapToDto(bookEntity);

		Assert.assertEquals(bookEntity.getId(), bookDto.getId());
		Assert.assertEquals(bookEntity.getName(), bookDto.getName());
		Assert.assertEquals(bookEntity.getAuthor(), bookDto.getAuthor());
		Assert.assertEquals(bookEntity.getPrice(), bookDto.getPrice());
	}

	@Test
	public void testConvertDtoToEntity() {
		BookDto bookDto = new BookDto();
		bookDto.setId(1);
		bookDto.setName("Book name");
		bookDto.setAuthor("Author name");
		bookDto.setPrice(10);

		BookEntity bookEntity = BeanMapper.mapToEntity(bookDto);

		Assert.assertEquals(bookDto.getId(), bookEntity.getId());
		Assert.assertEquals(bookDto.getName(), bookEntity.getName());
		Assert.assertEquals(bookDto.getAuthor(), bookEntity.getAuthor());
		Assert.assertEquals(bookDto.getPrice(), bookEntity.getPrice());
	}

}
