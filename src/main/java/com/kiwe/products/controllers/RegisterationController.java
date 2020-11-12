package com.kiwe.products.controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kiwe.products.dto.UserRegisterationRequest;
import com.kiwe.products.dto.UserRegisterationResponse;
import com.kiwe.products.services.UserService;

@RestController
public class RegisterationController {

	@Autowired
	private UserService userService;

	@PostMapping("/registeration")
	@Transactional
	public ResponseEntity<UserRegisterationResponse> register(
			@Valid @RequestBody UserRegisterationRequest registerationRequest) {

		userService.registerUser(registerationRequest);

		return new ResponseEntity<UserRegisterationResponse>(
				new UserRegisterationResponse("You're registered successfully"), HttpStatus.OK);

	}

}
