package com.kiwe.products.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.ModelAndView;

import com.kiwe.products.domain.User;
import com.kiwe.products.dto.ChangePasswordRequest;
import com.kiwe.products.dto.UpdateUserRequest;
import com.kiwe.products.dto.UserRegisterationRequest;

public interface UserService extends UserDetailsService {

	Page<User> getUsers(Pageable pageable);

	User getUserById(Long userId);

	User registerUser(UserRegisterationRequest registerationRequest);

	User updateUser(Long userId, UpdateUserRequest updateUserRequest);

	void changePassword(ChangePasswordRequest changePasswordRequest);

	boolean forgetPassword(String user);

	ModelAndView validateResetToken(String confirmationToken, ModelAndView modelAndView);

	ModelAndView resetPassword(ModelAndView modelAndView, User user);

	User getCurrentAuthenticatedUser();

	String getCurrentAuthenticatedUsername();

}
