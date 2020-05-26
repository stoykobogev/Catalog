package com.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.catalog.entities.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

	@Query(nativeQuery = true, value = "SELECT EXISTS (SELECT 1 FROM products WHERE id != ?1 AND image_code = ?2)")
	boolean existsInOtherProducts(int productId, String imageCode);
}
