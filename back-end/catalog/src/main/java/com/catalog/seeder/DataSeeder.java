package com.catalog.seeder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.catalog.entities.Image;
import com.catalog.entities.Product;
import com.catalog.repositories.ProductRepository;
import com.catalog.services.ImageService;

@Component
public class DataSeeder implements ApplicationRunner {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		Product apple = this.productRepository.findById(1).get();		
		Image appleImage = this.imageService.getAndSaveImage(getBytesByImageName("apple.png"));
		apple.setImage(appleImage);
		
		Product banana = this.productRepository.findById(2).get();		
		Image bananaImage = this.imageService.getAndSaveImage(getBytesByImageName("banana.jpg"));
		banana.setImage(bananaImage);
	}

	private byte[] getBytesByImageName(String name) throws Exception {
		Path path = Paths.get("src", "main", "resources", "images", name);	
		return Files.readAllBytes(path);
	}
}
