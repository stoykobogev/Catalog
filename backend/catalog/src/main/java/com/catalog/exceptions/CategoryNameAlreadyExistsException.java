package com.catalog.exceptions;

public class CategoryNameAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 5290696697139093410L;

	public CategoryNameAlreadyExistsException(String name) {
		super(String.format("Category with name: '%s' already exists", name));
	}
}
