/**
 * 
 */
package com.kiwe.products.services.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kiwe.products.domain.Product;
import com.kiwe.products.domain.ProductImage;
import com.kiwe.products.dto.CreateProductRequest;
import com.kiwe.products.exceptions.ResourceNotFoundException;
import com.kiwe.products.repositories.ProductRepository;
import com.kiwe.products.services.ProductService;
import com.kiwe.products.services.UserService;

/**
 * @author mohamedsalah
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserService userService;

	@Override
	public Page<Product> getProducts(Pageable pageable) {
		return productRepository.findByOwner(userService.getCurrentAuthenticatedUser(), pageable);
	}

	@Override
	public Product getProductById(long id) {
		Optional<Product> product = productRepository.findByIdAndOwner(id, userService.getCurrentAuthenticatedUser());
		if (!product.isPresent()) {
			throw new ResourceNotFoundException("ProductId " + id + " not found");
		}
		return product.get();
	}

	@Override
	@Transactional
	public Product createProduct(CreateProductRequest createProductRequest) throws IOException {

		Product product = new Product();
		product.setCategory(createProductRequest.getCategory());
		product.setName(createProductRequest.getName());
		product.setQuantity(createProductRequest.getQuantity());
		product.setOwner(userService.getCurrentAuthenticatedUser());
		Set<ProductImage> images = new HashSet<ProductImage>();

		for (MultipartFile productImageFile : createProductRequest.getImages()) {
			byte[] imageBytes = IOUtils.toByteArray(productImageFile.getInputStream());
			ProductImage productImage = new ProductImage();
			productImage.setProduct(product);
			productImage.setImage(imageBytes);
			images.add(productImage);
		}
		product.setImages(images);
		return productRepository.save(product);
	}

	@Override
	public void deleteProduct(long id) {
		if (!productRepository.existsByIdAndOwner(id, userService.getCurrentAuthenticatedUser())) {
			throw new ResourceNotFoundException("ProductId " + id + " not found");
		}
		productRepository.deleteById(id);

	}

}
