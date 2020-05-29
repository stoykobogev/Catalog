package com.catalog.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.catalog.dtos.ProductDto;
import com.catalog.entities.Product;

@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
    	ModelMapper modelMapper = new ModelMapper();
        
    	// Product Mapping
    	
		modelMapper.createTypeMap(Product.class, ProductDto.class)
		.addMappings(new PropertyMap<Product, ProductDto>() {				
			@Override
			protected void configure() {
				map().setImageCode(source.getImage().getCode());
			}
		});
		
		return modelMapper;
    }
    
    @Bean
    public MessageDigest messageDigest() throws NoSuchAlgorithmException {
    	return MessageDigest.getInstance("SHA-256");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
