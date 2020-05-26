package com.catalog.repositories;

import static com.catalog.SqlPaths.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;;

@Sql({ CATEGORIES, IMAGES, PRODUCTS })
public class ImageRepositoryTests extends AbstractRepositoryTest {

	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private MessageDigest messageDigest;
	
	
	@Test
	@Transactional(readOnly = true)
	public void testExistsInOtherProducts() {
		
		String hashCode = getImageCode("a".getBytes());
		
		boolean result = this.imageRepository.existsInOtherProducts(1, hashCode);
		
		assertTrue(result);
		
		hashCode = getImageCode("b".getBytes());
		
		result = this.imageRepository.existsInOtherProducts(2, hashCode);
		
		assertFalse(result);
	}
	
	private String getImageCode(byte[] bytes) {
		
		byte[] hash = this.messageDigest.digest(bytes);
		
		return Hex.encodeHexString(hash);
	}
}
