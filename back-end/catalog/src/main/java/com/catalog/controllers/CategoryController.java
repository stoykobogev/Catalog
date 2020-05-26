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

import com.catalog.controllers.params.CategoryParams;
import com.catalog.dtos.CategoryDto;
import com.catalog.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;

	
	@GetMapping
	public List<CategoryDto> getAllCategories() {
		return this.categoryService.getAllCategories();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public int createCategory(@Valid @RequestBody CategoryParams params) {
		return this.categoryService.createCategory(params);
	}
	 
	@PutMapping("/{categoryId}")
	public void updateCategory(@Valid @RequestBody CategoryParams params, @PathVariable int categoryId) {
		this.categoryService.updateCategory(params, categoryId);
	}

	@DeleteMapping("/{categoryId}")
	public void deleteCategory(@PathVariable int categoryId) {
		this.categoryService.deleteCategory(categoryId);
	}
}
