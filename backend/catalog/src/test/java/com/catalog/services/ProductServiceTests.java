package com.catalog.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.catalog.controllers.params.ProductParams;
import com.catalog.dtos.ProductDto;
import com.catalog.entities.Image;
import com.catalog.entities.Product;
import com.catalog.exceptions.NoSuchEntityException;
import com.catalog.repositories.ProductRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTests extends AbstractJUnit4SpringContextTests {
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private ImageService imageService;
	
	@InjectMocks
	private ProductService productService;
	
	
	@Test
	public void testGetAllProducts() {
		
		int categoryId = 1;
		ProductDto productDto = mock(ProductDto.class);
		Product product1 = mock(Product.class);
		Product product2 = mock(Product.class);
		List<Product> products = Arrays.asList(product1, product2);
		
		when(this.productRepository.findByCategoryIdOrderByNameAsc(categoryId)).thenReturn(products);
		when(this.modelMapper.map(any(Product.class), eq(ProductDto.class))).thenReturn(productDto);
		
		this.productService.getAllProductsByCategory(categoryId);
		
		verify(this.productRepository).findByCategoryIdOrderByNameAsc(categoryId);
		verify(this.modelMapper).map(product1, ProductDto.class);
		verify(this.modelMapper).map(product2, ProductDto.class);
	}
	
	@Test
	public void testCreateProduct() {
		
		int productId = 7;
		String productName = "productName";
		BigDecimal productPrice = BigDecimal.ONE;
		
		ProductParams params = new ProductParams();
		params.setName(productName);
		params.setPrice(productPrice);
		
		when(this.productRepository.save(any(Product.class)))
		.thenAnswer(invocation -> {
	        Product product = (Product) invocation.getArgument(0);
	        product.setId(productId);
	        return null;
	    });
		
		int result = this.productService.createProduct(params);
		
		ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
		verify(this.productRepository).save(productCaptor.capture());
		
		Product productCapture = productCaptor.getValue();
		
		assertEquals(productId, result);
		assertEquals(productName, productCapture.getName());
		assertEquals(productPrice, productCapture.getPrice());
	}
	
	@Test
	public void testUpdateProduct() {
		
		int productId = 7;
		String productName = "productName";
		BigDecimal productPrice = BigDecimal.ONE;
		
		ProductParams params = new ProductParams();
		params.setName(productName);
		params.setPrice(productPrice);
		
		Product product = new Product();
		
		when(this.productRepository.findById(productId)).thenReturn(Optional.of(product));
		
		this.productService.updateProduct(params, productId);

		assertEquals(productName, product.getName());
		assertEquals(productPrice, product.getPrice());
	}
	
	@Test
	public void testUpdateProduct_invalidId_throwsException() {
		
		int productId = 7;
		ProductParams params = new ProductParams();
		
		when(this.productRepository.findById(productId)).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() -> {
			this.productService.updateProduct(params, productId);
		});
	}
	
	@Test
	public void testDeleteProduct() {
		
		int productId = 7;		
		Product product = new Product();
		product.setImage(new Image());
		
		when(this.productRepository.findById(productId)).thenReturn(Optional.of(product));
		
		this.productService.deleteProduct(productId);

		verify(this.productRepository).delete(product);
	}
	
	@Test
	public void testDeleteProduct_invalidId_throwsException() {
		
		int productId = 7;
		
		when(this.productRepository.findById(productId)).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() -> {
			this.productService.deleteProduct(productId);
		});
	}
}



