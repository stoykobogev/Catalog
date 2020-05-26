package com.catalog.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.catalog.SqlPaths.CATEGORIES;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql( CATEGORIES )
public class CategoryRepositoryTests extends AbstractRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	@Transactional(readOnly = true)
	public void testExistsByName() {
		
		boolean result = this.categoryRepository.existsByName("food");
		
		assertTrue(result);
		
		result = this.categoryRepository.existsByName("doof");
		
		assertFalse(result);
	}
}
