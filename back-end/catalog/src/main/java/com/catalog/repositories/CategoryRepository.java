package com.catalog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findAllByOrderByNameAsc();
	
	@Query(nativeQuery = true, value = "SELECT EXISTS (SELECT 1 FROM categories WHERE name = ?1)")
	boolean existsByName(String name);
}
