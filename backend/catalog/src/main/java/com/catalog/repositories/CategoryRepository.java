package com.catalog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findAllByOrderByNameAsc();
	
	@Query(nativeQuery = true, value = 
			"IF EXISTS (SELECT * FROM categories " + 
			"    WHERE name == ?1) " + 
			"  SELECT 1 " + 
			"ELSE " + 
			"  SELECT 0")
	boolean existsByName(String name);
}
