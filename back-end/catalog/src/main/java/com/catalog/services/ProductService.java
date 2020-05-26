package com.catalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catalog.controllers.params.ProductCreateParams;
import com.catalog.controllers.params.ProductUpdateParams;
import com.catalog.dtos.ProductDto;
import com.catalog.entities.Category;
import com.catalog.entities.Image;
import com.catalog.entities.Product;
import com.catalog.exceptions.NoSuchEntityException;
import com.catalog.repositories.CategoryRepository;
import com.catalog.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
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
	public int createProduct(ProductCreateParams params) {
		
		Category category = this.categoryRepository.findById(params.getCategoryId())
				.orElseThrow(() -> new NoSuchEntityException(Category.class, params.getCategoryId()));
		
		Product product = new Product();
		product.setName(params.getName());
		product.setPrice(params.getPrice());
		product.setCategory(category);
		
		if (params.getImage() != null) {
			Image image = this.imageService.getAndSaveImage(params.getImage());
			product.setImage(image);
		}
		
		product = this.productRepository.save(product);
		
		return product.getId();
	}
	
	@Transactional
	public void updateProduct(ProductUpdateParams params, int productId) {
		
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new NoSuchEntityException(Product.class, productId));
		
		product.setName(params.getName());
		product.setPrice(params.getPrice());
		
		if (params.getImage() != null) {
			Image image = this.imageService.getAndSaveImage(params.getImage());
			product.setImage(image);
		}
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







