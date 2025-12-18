package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class PaintingNotFoundException extends RuntimeException {

	public PaintingNotFoundException(String mesaj) {
		super(mesaj);
		}
}