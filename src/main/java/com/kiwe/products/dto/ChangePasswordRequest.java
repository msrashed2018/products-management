package com.kiwe.products.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

	@NotNull(message = "currentPassword must not be null")
	@NotEmpty(message = "currentPassword must not be empty")
	String currentPassword;

	@NotNull(message = "newPassword must not be null")
	@NotEmpty(message = "newPassword must not be empty")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "password is not strong")
	String newPassword;

}
