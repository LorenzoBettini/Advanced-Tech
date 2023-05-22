package com.book.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.management.entity.BookEntity;
import com.book.management.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

}
