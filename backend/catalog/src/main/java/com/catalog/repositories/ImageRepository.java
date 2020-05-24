package com.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catalog.entities.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

	@Query(nativeQuery = true, value = 
			"IF EXISTS (SELECT * FROM products " + 
			"    WHERE id != ?1 AND image_code = ?2) " + 
			"  SELECT 1 " + 
			"ELSE " + 
			"  SELECT 0")
	boolean existsInOtherProducts(int productId, String imageCode);
}
