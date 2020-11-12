package com.kiwe.products.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateUserRequest {

	private String username;

	@NotEmpty(message = "firstname must not be empty")
	@NotNull(message = "firstname must not be null")
	private String firstname;

	@NotNull(message = "lastname must not be null")
	@NotEmpty(message = "lastname must not be empty")
	private String lastname;

	@NotNull(message = "idNumber must not be null")
	@NotEmpty(message = "idNumber must not be empty")
	private String idNumber;

	@NotEmpty(message = "email must not be empty")
	@Email(message = "email is not valid")
	private String email;

	@NotNull(message = "age must not be null")
	@Min(value = 18, message = "age must be 18+")
	private int age;

}
