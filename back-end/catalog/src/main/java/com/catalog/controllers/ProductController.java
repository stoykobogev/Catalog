package com.catalog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.controllers.params.ProductCreateParams;
import com.catalog.controllers.params.ProductUpdateParams;
import com.catalog.dtos.ProductDto;
import com.catalog.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	
	@GetMapping("/category/{categoryId}")
	public List<ProductDto> getAllProductsByCategory(@PathVariable int categoryId) {
		return this.productService.getAllProductsByCategory(categoryId);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public int createProduct(@Valid @RequestBody ProductCreateParams params) {
		return this.productService.createProduct(params);
	}
	
	@PutMapping("/{productId}")
	public void updateProduct(@Valid @RequestBody ProductUpdateParams params, @PathVariable int productId) {
		this.productService.updateProduct(params, productId);
	}
	
	@DeleteMapping("/{productId}")
	public void deleteProduct(@PathVariable int productId) {
		this.productService.deleteProduct(productId);
	}
	
	@GetMapping("/{productId}/image")
	public byte[] getProductImage(@PathVariable int productId) {
		return this.productService.getProductImage(productId);
	}
}
