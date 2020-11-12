package com.kiwe.products.services;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kiwe.products.domain.Product;
import com.kiwe.products.dto.CreateProductRequest;

public interface ProductService {

	Page<Product> getProducts(Pageable pageable);

	Product getProductById(long id);

	Product createProduct(CreateProductRequest createProductRequest) throws IOException;

	void deleteProduct(long id);
}
