package com.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
}
