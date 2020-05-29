package com.catalog.exceptions;

public class InvalidJsonWebToken extends RuntimeException {

	private static final long serialVersionUID = -3920798081682558937L;

	public InvalidJsonWebToken() {
		super("Invalid JWT");
	}
}
