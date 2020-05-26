package com.catalog.controllers.params;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateParams extends ProductUpdateParams {

	@NotNull
	@Min(1)
	private Integer categoryId;
}
