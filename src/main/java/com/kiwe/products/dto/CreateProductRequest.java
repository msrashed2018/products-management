package com.kiwe.products.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateProductRequest {

	@NotEmpty(message = "name must not be empty")
	@NotNull(message = "name must not be null")
	private String name;

	@NotNull(message = "quantity must not be null")
	private Integer quantity;

	@NotEmpty(message = "category must not be empty")
	@NotNull(message = "category must not be null")
	private String category;

	@NotNull(message = "images must not be null")
	private MultipartFile[] images;
}
