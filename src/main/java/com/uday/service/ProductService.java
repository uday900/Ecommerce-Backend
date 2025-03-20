package com.uday.service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uday.Dto.*;
import com.uday.entity.*;
import com.uday.mapper.EntityAndDtoMapper;
import com.uday.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;
	private final EntityAndDtoMapper entityAndDtoMapper;
	private final CategoryRepository categoryRepository;
	private final ImageStorageToDriveService imageService;
	
	// get products by category
	public Response getProductsByCategory(String categoryName) {
        // check the category is present or not
		        Category category = categoryRepository.findByName(categoryName);
		        if (category == null) {
                    return Response.builder().status(400).message("Category is not present").build();
                }
		        
        List<ProductDto> productList = productRepository.findByCategoryId(category.getId())
                .stream()
                .map(entityAndDtoMapper::mapToProductDto)
                .collect(Collectors.toList());

        return Response.builder().status(200).message("All products").products(productList).build();
	}

	// products by seach keyword
	public Response getProductsBySearchKeyword(String searchValue) {
		
		List<ProductDto> productList = productRepository.searchProducts(searchValue)
				.stream()
				.map(entityAndDtoMapper::mapToProductDto)
				.collect(Collectors.toList());

		return Response.builder().status(200).message("All products").products(productList).build();
	}
	
	
	public Response createProduct(ProductDto productDto, MultipartFile image) throws IOException, GeneralSecurityException {
		
		// check the category is present or not
		Category category = categoryRepository.findByName(productDto.getCategoryName());
		if (category == null || image.isEmpty()) {
			return Response.builder()
	                .status(400)
	                .message("Category is not present/Image is not selected")
	                .build();
		}
		
//		File imageFile = File.createTempFile("product_image", null);
//		image.transferTo(imageFile);
//		
//		ImageResponse imageResponse = imageService.uploadImageToDrive(imageFile);
//		
//		if (imageResponse.getStatus() == 200) {
//	        // If upload is successful, get the image URL from the response
//	        String imageUrl = imageResponse.getUrl();
//
//	        // Set the image details in the productDTO
////	        product.setImageName(image.getOriginalFilename());
//	        
//	        productDto.setImageUrl(imageUrl);
	        
	       
	        

	        // Save the product to the database
	        Product newProduct = entityAndDtoMapper.mapToProduct(productDto);
	        // setting image data:
	        
	        newProduct.setImageName(image.getOriginalFilename());
	        newProduct.setImageData(image.getBytes());
	        
	        newProduct.setCategory(category);
	        
	        productRepository.save(newProduct);
	        	        
	        System.out.println("Product created successfully");
//	    } else {
//	        // If image upload failed, return a failure response
//	    	return Response.builder()
//	                .status(400)
//	                .message("Image upload faild")
//	                .build();
//	    }
		
		return Response.builder()
				.status(200)
				.message("Product created successfully")
				.build();
	}
	
	public Response getProductById(Long productId) {
		Product existingProduct = productRepository.findById(productId).orElse(null);
		if (existingProduct == null) {
			return Response.builder().status(404).message("Product not found").build();
		}
		
		ProductDto productDto = entityAndDtoMapper.mapToProductDto(existingProduct);
				//mapper.mapProductToProductDto(existingProduct);
		
		return Response.builder().status(200).message("Product found").product(productDto).build();
	}
	
	public Response getAllProducts() {
		List<ProductDto> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityAndDtoMapper::mapToProductDto)
                .collect(Collectors.toList());
		
		return Response.builder()
				.status(200)
				.message("All products")
				.products(productList)
				.build();
	}
	// update product
public Response updateProduct(Long productId, ProductDto productDto, MultipartFile image) throws IOException, GeneralSecurityException {
		
		Product existingProduct = productRepository.findById(productId).orElse(null);
		if (existingProduct == null) {
			return Response.builder().status(404).message("Product not found").build();
		}	
		
		Category category = categoryRepository.findByName(productDto.getCategoryName());
		if (category == null) {
			return Response.builder().status(400).message("Category is not present").build();
		}
		
		Product newProduct = entityAndDtoMapper.mapToProduct(productDto);
		
		existingProduct.setName(newProduct.getName());
		existingProduct.setDescription(newProduct.getDescription());
		existingProduct.setPrice(newProduct.getPrice());
		existingProduct.setCategory(category);
		existingProduct.setColors(newProduct.getColors());
		existingProduct.setSizes(newProduct.getSizes());
		existingProduct.setBrand(newProduct.getBrand());
		existingProduct.setRating(newProduct.getRating());
		
		
		// If a new image is uploaded, update the image on Google Drive	
		if (image != null && !image.isEmpty()) {
			
			existingProduct.setImageName(image.getOriginalFilename());
			
			existingProduct.setImageData(image.getBytes());
			
			System.out.println("image is updated perfectly");
//            File imageFile = File.createTempFile("product", null);
//            image.transferTo(imageFile);
//
//            String imageId = imageService.extractDriveId(existingProduct.getImageUrl());
//            ImageResponse res = imageService.updateImageOnDrive(imageId, imageFile);
//
//            System.out.println(res.getMessage());
        } else {
        	System.out.println("Image is not selected");
        }
		
//		Product newProduct = entityAndDtoMapper.mapToProduct(productDto);
//				mapper.mapProductDtoToProduct(product);
		
//		existingProduct.setName(newProduct.getName());
//		existingProduct.setDescription(newProduct.getDescription());
//		existingProduct.setPrice(newProduct.getPrice());
//		existingProduct.setCategory(category);
//		existingProduct.setColors(newProduct.getColors());
//		existingProduct.setSizes(newProduct.getSizes());
//		existingProduct.setBrand(newProduct.getBrand());
//		existingProduct.setRating(newProduct.getRating());
//		existingProduct.setImageUrl(newProduct.getImageUrl());
		

		productRepository.save(existingProduct);
		
		
		return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();

		
	}


	// delete product
	public Response deleteProduct(Long productId) throws GeneralSecurityException, IOException {
		// check the product is present or not
		Product product = productRepository.findById(productId).orElse(null);
		if (product == null) {
			return Response.builder().status(400).message("Product is not present").build();
		}

		// delte image from drive
		String imageUrl = product.getImageUrl();
		
//		ImageResponse imageResponse =  imageService.deleteImageFromDrive( imageService.extractDriveId(imageUrl));
		
//		if (imageResponse.getStatus() != 200) {
//			return Response.builder().status(400).message("Image is not deleted from drive").build();
//		}
		// first delete from product in category
		Category category = categoryRepository.findByName(product.getCategory().getName());
		
		List<Product> products = category.getProducts();
		products.remove(product);
		
		
		
		
		
		productRepository.deleteById(productId);
		
		category.setProducts(products);
		categoryRepository.save(category);

		return Response.builder().status(200).message("Product deleted successfully").build();
	}

	public Response testImageUpload(MultipartFile image) throws IOException, GeneralSecurityException {
		// TODO Auto-generated method stub
		
		File imageFile = File.createTempFile("product_image", null);
		image.transferTo(imageFile);
		
		ImageResponse imageResponse = imageService.uploadImageToDrive(imageFile);
		
		if (imageResponse.getStatus() == 200) {
	        // If upload is successful, get the image URL from the response
	        String imageUrl = imageResponse.getUrl();

	        System.out.println("Image uploaded successfully" + imageUrl);
	        // Set the image details in the productDTO
//	        product.setImageName(image.getOriginalFilename());
	        
//	        productDto.setImageUrl(imageUrl);

	        // Save the product to the database
//	        Product newProduct = entityAndDtoMapper.mapToProduct(productDto);
//	        
//	        newProduct.setCategory(category);
//	        
//	        productRepository.save(
//	        		newProduct
//	        		);
	        	        
//	        System.out.println("Product created successfully");
	    } else {
	        // If image upload failed, return a failure response
	    	return Response.builder()
	                .status(400)
	                .message("Image upload faild")
	                .build();
	    }
		
		return Response.builder()
                .status(200)
                .message("Product created successfully")
                .build();
	}
}
