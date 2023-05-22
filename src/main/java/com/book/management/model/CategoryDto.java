package com.book.management.model;

import java.util.List;

public class CategoryDto {
	   private Integer id;
	    private String name;
	    private List<BookDto> books;

	    // Constructors, getters, and setters

	    public CategoryDto() {
	    }

	    public CategoryDto(Integer id, String name, List<BookDto> books) {
	        this.id = id;
	        this.name = name;
	        this.books = books;
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

	    public List<BookDto> getBooks() {
	        return books;
	    }

	    public void setBooks(List<BookDto> books) {
	        this.books = books;
	    }
}
