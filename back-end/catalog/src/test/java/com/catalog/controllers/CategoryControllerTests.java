package com.catalog.controllers;

import static com.catalog.SqlPaths.CATEGORIES;
import static com.catalog.SqlPaths.IMAGES;
import static com.catalog.SqlPaths.PRODUCTS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.catalog.controllers.params.CategoryParams;
import com.catalog.dtos.CategoryDto;
import com.catalog.entities.Category;
import com.catalog.entities.Image;
import com.catalog.entities.Product;
import com.catalog.repositories.CategoryRepository;
import com.catalog.repositories.ImageRepository;
import com.catalog.repositories.ProductRepository;

@Sql({  CATEGORIES, IMAGES, PRODUCTS })
public class CategoryControllerTests extends AbstractControllerTest {

	private static final String BASE_URL = "/categories";
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Test
	@Transactional(readOnly = true)
	public void testGetAllCategories() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = get(BASE_URL);
		
		String jsonResult = super.perform(requestBuilder)
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		List<CategoryDto> result = super.parseJsonToList(jsonResult, CategoryDto.class);
		
		assertEquals(2, result.size());
		
		CategoryDto category1 = result.get(0);
		assertEquals("clothes", category1.getName());
		
		CategoryDto category2 = result.get(1);
		assertEquals("food", category2.getName());
		
		testAuthorities(requestBuilder, Roles.ALL);
	}
	
	@Test
	@Transactional
	public void testCreateCategory() throws Exception {
		
		CategoryParams params = new CategoryParams();
		params.setName("name");
		
		MockHttpServletRequestBuilder requestBuilder = post(BASE_URL);
		
		String result = super.performWithBody(requestBuilder, params).andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();
		
		int categoryId = Integer.parseInt(result);
		
		Optional<Category> categoryOptional = this.categoryRepository.findById(categoryId);		
		assertTrue(categoryOptional.isPresent());
		
		Category category = categoryOptional.get();	
		assertEquals(params.getName(), category.getName());
		
		testAuthorities(requestBuilder, Arrays.asList(Roles.ADMIN));
	}
	
	@Test
	@Transactional
	public void testCreateCategory_invalidName_throwsBadRequest() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = post(BASE_URL);
		
		testInvalidParams(requestBuilder);
	}
	
	@Test
	@Transactional
	public void testUpdateCategory() throws Exception {
		
		int categoryId = 1;
		CategoryParams params = new CategoryParams();
		params.setName("name");
		
		MockHttpServletRequestBuilder requestBuilder = put(String.format("%s/%d", BASE_URL, categoryId));
		
		super.performWithBody(requestBuilder, params).andExpect(status().isOk());
		
		Category category = this.categoryRepository.findById(categoryId).get();
								
		assertEquals(params.getName(), category.getName());
		
		testAuthorities(requestBuilder, Arrays.asList(Roles.ADMIN));
	}
	
	@Test
	@Transactional
	public void testUpdateCategory_invalidName_throwsBadRequest() throws Exception {
		
		MockHttpServletRequestBuilder requestBuilder = put(String.format("%s/%d", BASE_URL, 1));
		
		testInvalidParams(requestBuilder);
	}
	
	@Test
	@Transactional
	public void testDeleteCategory() throws Exception {
		
		int categoryId = 1;
		
		Category category = this.categoryRepository.findById(categoryId).get();
		List<Product> products = category.getProducts();
		products.sort((a,b) -> Integer.compare(a.getId(), b.getId()));
		Product product1 = products.get(0);
		Product product2 = products.get(1);
		
		int product1Id = product1.getId();
		int product2Id = product2.getId();
		String product1ImageCode = product1.getImage().getCode();
		String product2ImageCode = product2.getImage().getCode();
		
		MockHttpServletRequestBuilder requestBuilder = delete(String.format("%s/%d", BASE_URL, categoryId));
		super.perform(requestBuilder).andExpect(status().isOk());
		
		Optional<Category> categoryOptional = this.categoryRepository.findById(categoryId);
		assertFalse(categoryOptional.isPresent());
		
		Optional<Product> product1Optional = this.productRepository.findById(product1Id);
		assertFalse(product1Optional.isPresent());
		
		Optional<Product> product2Optional = this.productRepository.findById(product2Id);
		assertFalse(product2Optional.isPresent());
		
		Optional<Image> image1Optional = this.imageRepository.findById(product1ImageCode);
		assertTrue(image1Optional.isPresent());
		
		Optional<Image> image2Optional = this.imageRepository.findById(product2ImageCode);
		assertFalse(image2Optional.isPresent());
		
		testAuthorities(requestBuilder, Arrays.asList(Roles.ADMIN));
	}
	
	private void testInvalidParams(MockHttpServletRequestBuilder requestBuilder) throws Exception {
		
		CategoryParams params = new CategoryParams();
		
		// Null name
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
		
		// Too long name
		params.setName(StringUtils.repeat('1', 21));
		super.performWithBody(requestBuilder, params).andExpect(status().isBadRequest());
	}
}
