package com.catalog.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.catalog.dtos.ProductDto;
import com.catalog.entities.Product;

@Configuration
@Profile("!test")
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
}
