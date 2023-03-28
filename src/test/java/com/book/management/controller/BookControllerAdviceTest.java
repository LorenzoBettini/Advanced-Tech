package com.book.management.controller;

import static org.mockito.Mockito.verify;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerAdviceTest {

	@InjectMocks
	private BookControllerAdvice bookControllerAdvice;

	@Mock
	private Logger logger;

	@Test
	public void testHandleNotFound() {
		// given
		NoSuchElementException exception = new NoSuchElementException();

		// when
		bookControllerAdvice.handleNotFound(exception);

		// then
		verify(logger).info("Not Found");
	}

	@Test
	public void testHandleBadRequest() {
		// given
		IllegalArgumentException exception = new IllegalArgumentException();

		// when
		bookControllerAdvice.handleBadRequest(exception);

		// then
		verify(logger).info("Bad Request");
	}
}