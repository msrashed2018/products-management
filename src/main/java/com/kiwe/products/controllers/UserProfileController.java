package com.kiwe.products.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kiwe.products.domain.User;
import com.kiwe.products.dto.ChangePasswordRequest;
import com.kiwe.products.dto.ChangePasswordResponse;
import com.kiwe.products.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserProfileController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenStore tokenStore;

	@GetMapping("/profile")
	public User retreiveProfile() {
		return userService.getUserById(userService.getCurrentAuthenticatedUser().getId());
	}

	@PutMapping("/profile/changepassword")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		userService.changePassword(changePasswordRequest);
		return new ResponseEntity<ChangePasswordResponse>(
				new ChangePasswordResponse("your password changed successfully"), HttpStatus.OK);
	}

	@RequestMapping(path = "/profile/logout", method = RequestMethod.POST)
	public void logout(HttpServletRequest request, OAuth2Authentication authentication) throws ServletException {
		OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
		String tokenValue = details.getTokenValue();
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
		tokenStore.removeAccessToken(accessToken);
		tokenStore.removeRefreshToken(refreshToken);
	}

}
