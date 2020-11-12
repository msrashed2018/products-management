package com.kiwe.products.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiwe.products.domain.Product;
import com.kiwe.products.dto.CreateProductRequest;
import com.kiwe.products.services.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;

	@GetMapping("/products")
	public Page<Product> retrieveAllProducts(Pageable pageable) {
		return productService.getProducts(pageable);
	}

	@GetMapping("/products/{productId}")
	public Product retrieveProductById(@PathVariable Long productId) {
		return productService.getProductById(productId);
	}

	@DeleteMapping("/products/{productId}")
	public void deleteProduct(@PathVariable Long productId) {
		productService.deleteProduct(productId);
	}

	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@Valid @ModelAttribute CreateProductRequest createProductRequest)
			throws IOException {

		Product createdProduct = productService.createProduct(createProductRequest);
		return new ResponseEntity<Product>(createdProduct, HttpStatus.OK);

	}

}
