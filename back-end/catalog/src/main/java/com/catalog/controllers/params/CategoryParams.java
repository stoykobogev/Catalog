package com.catalog.controllers.params;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryParams {

	@NotNull
	@Size(min = 1, max = 20)
	private String name;
}
