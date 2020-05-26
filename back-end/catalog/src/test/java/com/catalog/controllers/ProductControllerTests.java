package com.catalog.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.catalog.SqlPaths.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.catalog.constants.Roles;
import com.catalog.controllers.params.ProductCreateParams;
import com.catalog.controllers.params.ProductUpdateParams;
import com.catalog.dtos.ProductDto;
import com.catalog.entities.Image;
import com.catalog.entities.Product;
import com.catalog.repositories.ImageRepository;
import com.catalog.repositories.ProductRepository;

@Sql({ CATEGORIES, IMAGES, PRODUCTS })
public class ProductControllerTests extends AbstractControllerTest {
	
	private static final String BASE_URL = "/products";
	private static final String GET_PRODUCTS_URL = BASE_URL + "/category/";

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	private final int categoryId = 1;
	
	@Test
	@Transactional(readOnly = true)
	public void testGetAllProductsByCategory() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = get(GET_PRODUCTS_URL + categoryId);
		String jsonResult = super.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		List<ProductDto> result = super.parseJsonToList(jsonResult, ProductDto.class);
		
		ProductDto product1 = result.get(0);
		assertEquals("apple", product1.getName());
		assertTrue(BigDecimal.valueOf(0.50).setScale(2).equals(product1.getPrice()));
		
		ProductDto product2 = result.get(1);
		assertEquals("banana", product2.getName());
		assertTrue(BigDecimal.valueOf(0.40).setScale(2).equals(product2.getPrice()));
		
		testAuthorities(requestBuilder, Roles.ALL);
	}
	
	@Test
	@Transactional
	public void testCreateProduct() throws Exception {
		ProductCreateParams params = new ProductCreateParams();
		params.setName("prodcutName");
		params.setPrice(BigDecimal.valueOf(5));
		params.setCategoryId(this.categoryId);
		
		MockHttpServletRequestBuilder requestBuilder = post(BASE_URL);
		String productId = super.performWithBody(requestBuilder, params)
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		
		List<Product> products = this.productRepository.findByCategoryIdOrderByNameAsc(this.categoryId);
		
		assertEquals(3, products.size());
		
		Product newProduct = products.get(2);
		
		assertEquals(Integer.parseInt(productId), newProduct.getId());
		assertEquals(params.getName(), newProduct.getName());
		assertTrue(params.getPrice().equals(newProduct.getPrice()));
		
		testAuthorities(requestBuilder, Arrays.asList(Roles.ADMIN));
	}
	
	@Test
	@Transactional
	public void testCreateProduct_invalidParams_throwsBadRequest() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = post(BASE_URL);
		
		ProductCreateParams params = new ProductCreateParams();
		params.setCategoryId(this.categoryId);
		
		testInvalidParams(requestBuilder, params);
		
		// Null category ID
		params.setCategoryId(null);
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Invalid category ID
		params.setCategoryId(0);
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
	}
	
	@Test
	@Transactional
	public void testUpdateProduct() throws Exception {
		
		int productId = 1;	
		ProductCreateParams params = new ProductCreateParams();
		params.setName("prodcutName");
		params.setPrice(BigDecimal.valueOf(5));
		
		MockHttpServletRequestBuilder requestBuilder = put(String.format("%s/%s", BASE_URL, productId));
		super.performWithBody(requestBuilder, params).andExpect(status().isOk());
		
		Product product = this.productRepository.findById(productId).get();

		assertEquals(params.getName(), product.getName());
		assertTrue(params.getPrice().equals(product.getPrice()));
		
		testAuthorities(requestBuilder, Arrays.asList(Roles.ADMIN));
	}
	
	@Test
	@Transactional
	public void testUpdateProduct_invalidParams_throwsBadRequest() throws Exception {
		
		int productId = 1;
		String url = String.format("%s/%s", BASE_URL, productId);
		
		testInvalidParams(put(url), new ProductUpdateParams());
	}
	
	@Test
	@Transactional
	public void testDeleteProduct_imageNotDeleted() throws Exception {
		
		int productId = 1;
		
		Optional<Product> productOptional = this.productRepository.findById(productId);
		String imageCode = productOptional.get().getImage().getCode();
		
		MockHttpServletRequestBuilder requestBuilder = delete(String.format("%s/%s", BASE_URL, productId));
		super.perform(requestBuilder).andExpect(status().isOk());
		
		productOptional = this.productRepository.findById(productId);

		assertFalse(productOptional.isPresent());
		
		Optional<Image> imageOptional = this.imageRepository.findById(imageCode);

		assertTrue(imageOptional.isPresent());
		
		testAuthorities(requestBuilder, Arrays.asList(Roles.ADMIN));
	}
	
	@Test
	@Transactional
	public void testDeleteProduct_imageDeleted() throws Exception {
		
		int productId = 2;
		String url = String.format("%s/%s", BASE_URL, productId);
		
		Optional<Product> productOptional = this.productRepository.findById(productId);
		String imageCode = productOptional.get().getImage().getCode();
		
		super.perform(delete(url)).andExpect(status().isOk());
		
		productOptional = this.productRepository.findById(productId);

		assertFalse(productOptional.isPresent());
		
		Optional<Image> imageOptional = this.imageRepository.findById(imageCode);

		assertFalse(imageOptional.isPresent());
	}
	
	@Test
	@Transactional
	public void testGetProductImage() throws Exception {
		
		int productId = 1;
		
		Optional<Product> productOptional = this.productRepository.findById(productId);
		byte[] imageBytes = productOptional.get().getImage().getBytes();
		
		MockHttpServletRequestBuilder requestBuilder = get(String.format("%s/%s/image", BASE_URL, productId));
		byte[] result = super.perform(requestBuilder).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsByteArray();
		
		assertTrue(Arrays.equals(imageBytes, result));
		
		testAuthorities(requestBuilder, Roles.ALL);
	}
	
	private <T extends ProductUpdateParams> void testInvalidParams(MockHttpServletRequestBuilder requestBuilder, T params) throws Exception {
		String validName = "productName";
		BigDecimal validPrice = BigDecimal.valueOf(5); 

		params.setName(validName);
		
		// Big price fraction
		params.setPrice(BigDecimal.valueOf(0.0000001));
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Big price integer		
		params.setPrice(BigDecimal.valueOf(100000000000L));
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Null price		
		params.setPrice(null);
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Zero price		
		params.setPrice(BigDecimal.ZERO);
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Null name
		params.setPrice(validPrice);
		params.setName(null);
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Empty name
		params.setName("");
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Too long name
		params.setName(StringUtils.repeat('1', 21));
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		params.setName(validName);
	}
}
