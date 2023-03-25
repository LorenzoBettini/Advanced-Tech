package com.book.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.management.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {

}