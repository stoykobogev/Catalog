package com.catalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catalog.dtos.CategoryDto;
import com.catalog.entities.Category;
import com.catalog.entities.Product;
import com.catalog.exceptions.CategoryNameAlreadyExistsException;
import com.catalog.exceptions.NoSuchEntityException;
import com.catalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;
	
	
	@Transactional(readOnly = true)
	public List<CategoryDto> getAllCategories() {
		
		List<Category> categories = this.categoryRepository.findAllByOrderByNameAsc();
		List<CategoryDto> dtos = categories.stream()
				.map(category -> this.modelMapper.map(category, CategoryDto.class))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	@Transactional
	public int createCategory(String name) {
		
		validateUniqueName(name);
		
		Category category = new Category();
		category.setName(name);
		
		this.categoryRepository.save(category);
		
		return category.getId();
	}
	
	@Transactional
	public void updateCategory(String name, int categoryId) {
		
		validateUniqueName(name);
		
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new NoSuchEntityException(Category.class, categoryId));
		
		category.setName(name);
	}
	
	@Transactional
	public void deleteCategory(int categoryId) {
		
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new NoSuchEntityException(Category.class, categoryId));
		
		for (Product product : category.getProducts()) {
			this.productService.deleteProduct(product);
		}
		
		this.categoryRepository.delete(category);
	}
	
	private void validateUniqueName(String name) {
		
		boolean nameAlreadyExists = this.categoryRepository.existsByName(name);
		
		if (nameAlreadyExists) {
			throw new CategoryNameAlreadyExistsException(name);
		}
	}
}
