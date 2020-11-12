package com.kiwe.products.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.ToString;

@Data
public class UserRegisterationRequest {

	@NotEmpty(message = "username must not be empty")
	@NotNull(message = "username must not be null")
	private String username;

	@ToString.Exclude
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "password is not strong")
	@NotEmpty(message = "password must not be empty")
	@NotNull(message = "password must not be null")
	private String password;

	@NotEmpty(message = "firstname must not be empty")
	@NotNull(message = "firstname must not be null")
	private String firstname;

	@NotNull(message = "lastname must not be null")
	@NotEmpty(message = "lastname must not be empty")
	private String lastname;

	@NotEmpty(message = "email must not be empty")
	@Email(message = "email is not valid")
	private String email;

	@NotNull(message = "age must not be null")
	@Min(value = 18, message = "age must be 18+")
	private int age;

}
