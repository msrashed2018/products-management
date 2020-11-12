package com.kiwe.products.controllers;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kiwe.products.domain.User;
import com.kiwe.products.services.UserService;

@CrossOrigin(origins = "*")
@Controller
public class ForgetPasswordController {

	@Autowired
	private UserService userService;

	@PutMapping("/ext/forgotpassword")
	public ResponseEntity<Boolean> forgetPassword(@RequestParam(name = "user", required = true) String user) {
		userService.forgetPassword(user);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@RequestMapping(value = "/ext/confirm-reset", method = RequestMethod.GET)
	public ModelAndView validateResetToken(@RequestParam("token") String confirmationToken, ModelAndView modelAndView) {

		return userService.validateResetToken(confirmationToken, modelAndView);
	}

	@RequestMapping(value = "/ext/resetpassword", method = RequestMethod.POST)
	@Transactional
	public ModelAndView resetPassword(ModelAndView modelAndView, User user) {
		return userService.resetPassword(modelAndView, user);
	}

}
