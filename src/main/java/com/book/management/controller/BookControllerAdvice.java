package com.book.management.controller;

import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class BookControllerAdvice {
	private Logger logger;
	
	@PostConstruct
	public void logger() {
		logger = LoggerFactory.getLogger(this.getClass());
	}
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchElementException.class)
	public void handleNotFound(NoSuchElementException e) {
		logger.info("Not Found");
	}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleBadRequest(IllegalArgumentException e) {
        logger.info("Bad Request");
    }
}
