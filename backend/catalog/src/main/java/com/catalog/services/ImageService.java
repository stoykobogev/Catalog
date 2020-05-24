package com.catalog.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catalog.entities.Image;
import com.catalog.exceptions.NoSuchEntityException;
import com.catalog.repositories.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;

	private MessageDigest messageDigest;
	
	public ImageService() throws NoSuchAlgorithmException {
		this.messageDigest = MessageDigest.getInstance("SHA-256");
	}
	
	@Transactional
	public Image getAndSaveImage(byte[] bytes) {
		
		byte[] hash = this.messageDigest.digest(bytes);
		String hashCode = Hex.encodeHexString(hash);
		
		Optional<Image> optional = this.imageRepository.findById(hashCode);
		
		if (optional.isPresent()) {
			
			return optional.get();
			
		} else {
			
			Image image = new Image();
			image.setCode(hashCode);
			image.setBytes(bytes);
			
			this.imageRepository.save(image);
			
			return image;
		}
	}
	
	@Transactional
	public void deleteImage(int productId, String imageCode) {
		
		boolean productImageExistsInOtherProducts = this.imageRepository.existsInOtherProducts(productId, imageCode);
		
		if (!productImageExistsInOtherProducts) {		
			try {			
				this.imageRepository.deleteById(imageCode);			
			} catch (IllegalArgumentException e) {
				throw new NoSuchEntityException(Image.class, imageCode);
			}
		}
	}
}
