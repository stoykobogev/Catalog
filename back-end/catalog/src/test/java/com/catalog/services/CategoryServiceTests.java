package com.catalog.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.catalog.controllers.params.CategoryParams;
import com.catalog.dtos.CategoryDto;
import com.catalog.entities.Category;
import com.catalog.entities.Product;
import com.catalog.exceptions.CategoryNameAlreadyExistsException;
import com.catalog.exceptions.NoSuchEntityException;
import com.catalog.repositories.CategoryRepository;

@RunWith(PowerMockRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceTests {

	@Mock
	private CategoryRepository categoryRepository;
	
	@Mock
	private ProductService productService;

	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private CategoryService categoryService;
	
	
	@Test
	public void testGetAllCategories() {

		CategoryDto categoryDto = mock(CategoryDto.class);
		Category category1 = mock(Category.class);
		Category category2 = mock(Category.class);
		List<Category> categories = Arrays.asList(category1, category2);
		
		when(this.categoryRepository.findAllByOrderByNameAsc()).thenReturn(categories);
		when(this.modelMapper.map(any(CategoryDto.class), eq(CategoryDto.class))).thenReturn(categoryDto);
		
		this.categoryService.getAllCategories();
		
		verify(this.categoryRepository).findAllByOrderByNameAsc();
		verify(this.modelMapper).map(category1, CategoryDto.class);
		verify(this.modelMapper).map(category2, CategoryDto.class);
	}
	
	@Test
	public void testCreateCategory() {
		
		int categoryId = 1;
		String categoryName = "categoryName";
		CategoryParams params = new CategoryParams();
		params.setName(categoryName);
		
		when(this.categoryRepository.existsByName(categoryName)).thenReturn(false);
		when(this.categoryRepository.save(any(Category.class)))
		.thenAnswer(invocation -> {
			Category category = (Category) invocation.getArgument(0);
			category.setId(categoryId);
	        return null;
	    });
		
		int result = this.categoryService.createCategory(params);
		
		ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
		verify(this.categoryRepository).save(captor.capture());
		
		Category category = captor.getValue();
		
		assertEquals(categoryId, result);
		assertEquals(categoryName, category.getName());
	}
	
	@Test
	public void testUpdateCategory() {
		
		int categoryId = 1;
		String categoryName = "categoryName";
		Category category = new Category();
		CategoryParams params = new CategoryParams();
		params.setName(categoryName);
		
		when(this.categoryRepository.existsByName(categoryName)).thenReturn(false);
		when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		
		this.categoryService.updateCategory(params, categoryId);
		
		assertEquals(categoryName, category.getName());
	}
	
	@Test
	public void testUpdateCategory_invalidId_throwsException() {
		
		int categoryId = 1;
		String categoryName = "categoryName";
		CategoryParams params = new CategoryParams();
		params.setName(categoryName);
		
		when(this.categoryRepository.existsByName(categoryName)).thenReturn(false);
		when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() -> {			
			this.categoryService.updateCategory(params, categoryId);
		});
	}
	
	@Test
	public void testDeleteCategory() {
		
		int categoryId = 1;
		Category category = new Category();
		Product product1 = new Product();
		Product product2 = new Product();
		category.setProducts(Arrays.asList(product1, product2));
		
		when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
		
		this.categoryService.deleteCategory(categoryId);
		
		verify(this.productService).deleteProduct(product1);
		verify(this.productService).deleteProduct(product2);
		verify(this.categoryRepository).delete(category);
	}
	
	@Test
	public void testDeleteCategory_invalidId_throwsException() {
		
		int categoryId = 1;
		
		when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
		
		assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() -> {			
			this.categoryService.deleteCategory(categoryId);
		});
	}
	
	@Test
	public void testValidateUniqueName() throws Exception {
		
		String categoryName = "categoryName";
		
		when(this.categoryRepository.existsByName(categoryName)).thenReturn(false);
		
		Whitebox.invokeMethod(this.categoryService, "validateUniqueName", categoryName);
		
		when(this.categoryRepository.existsByName(categoryName)).thenReturn(true);
		
		assertThatExceptionOfType(CategoryNameAlreadyExistsException.class).isThrownBy(() -> {
			Whitebox.invokeMethod(this.categoryService, "validateUniqueName", categoryName);
		});
	}
}
