package com.book.management.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BookDTO {
	private Integer id;

	@NotBlank
	private String name;

	@NotBlank
	private String author;

	@NotNull
	private Integer price;

	private CategoryDTO category; 

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public BookDTO(Integer id, @NotBlank String name, @NotBlank String author, @NotNull Integer price) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.price = price;
	}

	public BookDTO() {
		// TODO Auto-generated constructor stub
	}

}
