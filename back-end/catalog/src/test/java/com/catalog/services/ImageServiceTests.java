package com.catalog.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.util.Optional;

import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.catalog.entities.Image;
import com.catalog.exceptions.NoSuchEntityException;
import com.catalog.repositories.ImageRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImageServiceTests {

	@Mock
	private ImageRepository imageRepository;
	
	@Mock
	private MessageDigest messageDigest;
	
	@InjectMocks
	private ImageService imageService;
	
	private final byte[] imageBytes = new byte[0];
	private String hashCode;
	
	
	@Before
	public void init() {
		
		byte[] bytes = new byte[0];
		this.hashCode = Hex.encodeHexString(bytes);
		
		when(this.messageDigest.digest(this.imageBytes)).thenReturn(bytes);
	}
	
	@Test
	public void testGetAndSaveImage_existingImage() {
		
		Image image = new Image();
		
		when(this.imageRepository.findById(this.hashCode)).thenReturn(Optional.of(image));
		
		Image result = this.imageService.getAndSaveImage(this.imageBytes);
		
		assertEquals(image, result);
	}
	
	@Test
	public void testGetAndSaveImage_nonexistingImage() {
		
		when(this.imageRepository.findById(this.hashCode)).thenReturn(Optional.empty());
		when(this.imageRepository.save(any(Image.class))).thenAnswer(new Answer<Image>() {
			@Override
			public Image answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(0);
			}
		});
		
		Image result = this.imageService.getAndSaveImage(this.imageBytes);
		
		ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
		verify(this.imageRepository).save(captor.capture());
			
		Image image = captor.getValue();
		assertEquals(image, result);
		assertEquals(this.hashCode, image.getCode());
		assertEquals(this.imageBytes, image.getBytes());
	}
	
	@Test
	public void testDeleteImage_existingImage() {
		
		int productId = 1;
		
		when(this.imageRepository.existsInOtherProducts(productId, this.hashCode)).thenReturn(false);
		
		this.imageService.deleteImage(productId, this.hashCode);
		
		verify(this.imageRepository).deleteById(this.hashCode);
	}
	
	@Test
	public void testDeleteImage_nonexistingImage() {
		
		int productId = 1;
		
		when(this.imageRepository.existsInOtherProducts(productId, this.hashCode)).thenReturn(true);
		
		this.imageService.deleteImage(productId, this.hashCode);
		
		verify(this.imageRepository, never()).deleteById(this.hashCode);
	}
	
	@Test
	public void testDeleteImage_invalidProductId_throwsException() {
		
		int productId = 1;
		
		when(this.imageRepository.existsInOtherProducts(productId, this.hashCode)).thenReturn(false);
	
		doThrow(IllegalArgumentException.class).when(this.imageRepository).deleteById(this.hashCode);
		
		assertThatExceptionOfType(NoSuchEntityException.class).isThrownBy(() -> {			
			this.imageService.deleteImage(productId, this.hashCode);
		});
	}
}
