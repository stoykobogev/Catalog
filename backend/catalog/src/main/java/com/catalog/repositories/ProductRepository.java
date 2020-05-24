package com.catalog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByCategoryIdOrderByNameAsc(int categoryId);
}
