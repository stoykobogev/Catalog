package com.catalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catalog.controllers.params.ProductParams;
import com.catalog.dtos.ProductDto;
import com.catalog.entities.Product;
import com.catalog.exceptions.NoSuchEntityException;
import com.catalog.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageService imageService;
	
	
	@Transactional(readOnly = true)
	public List<ProductDto> getAllProductsByCategory(int categoryId) {
		
		List<Product> products = this.productRepository.findByCategoryIdOrderByNameAsc(categoryId);
		List<ProductDto> dtos = products.stream()
				.map(product -> this.modelMapper.map(product, ProductDto.class))
				.collect(Collectors.toList());
		
		return dtos;
	}
	
	@Transactional(readOnly = true)
	public byte[] getProductImage(int productId) {
		
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new NoSuchEntityException(Product.class, productId));
		
		return product.getImage().getBytes();
	}
	
	@Transactional
	public int createProduct(ProductParams params) {
		
		Product product = new Product();
		product.setName(params.getName());
		product.setPrice(params.getPrice());
		
		this.productRepository.save(product);
		
		return product.getId();
	}
	
	@Transactional
	public void updateProduct(ProductParams params, int productId) {
		
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new NoSuchEntityException(Product.class, productId));
		
		product.setName(params.getName());
		product.setPrice(params.getPrice());
	}
	
	@Transactional
	public void deleteProduct(int productId) {
		
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new NoSuchEntityException(Product.class, productId));
		
		deleteProduct(product);
	}
	
	@Transactional
	public void deleteProduct(Product product) {
		
		this.productRepository.delete(product);
		
		this.imageService.deleteImage(product.getId(), product.getImage().getCode());
	}
}







