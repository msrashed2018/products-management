/**
 * 
 */
package com.kiwe.products.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kiwe.products.domain.PasswordResetToken;
import com.kiwe.products.domain.Role;
import com.kiwe.products.domain.RoleName;
import com.kiwe.products.domain.User;
import com.kiwe.products.dto.ChangePasswordRequest;
import com.kiwe.products.dto.UpdateUserRequest;
import com.kiwe.products.dto.UserRegisterationRequest;
import com.kiwe.products.exceptions.BusinessException;
import com.kiwe.products.exceptions.ResourceAlreadyExistException;
import com.kiwe.products.exceptions.ResourceNotFoundException;
import com.kiwe.products.models.Mail;
import com.kiwe.products.repositories.PasswordResetTokenRepository;
import com.kiwe.products.repositories.RoleRepository;
import com.kiwe.products.repositories.UserRepository;
import com.kiwe.products.services.EmailService;
import com.kiwe.products.services.UserService;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mohamedsalah
 *
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	public static final String USERS_BASIC_STORAGE_PATH = "/users/id-images";
	public static final String ID_FRONT_IMAGE_FILE_NAME = "id-frontimage";
	public static final String ID_BACK_IMAGE_FILE_NAME = "id-backimage";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordResetTokenRepository confirmationTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MessageSource messages;

	@Autowired
	private DefaultTokenServices defaultTokenServices;

	@Value("${oauth.jwt.clientId:products-management-client}")
	private String clientId;

	@Autowired
//	@Qualifier("jdbcTokenStore")
	private TokenStore tokenStore;

	@Override
	public Page<User> getUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public User getUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new ResourceNotFoundException(
					messages.getMessage("error.resourceNotFound.user", null, LocaleContextHolder.getLocale()));
		}
		return user.get();
	}

	@Override
	public User registerUser(UserRegisterationRequest registerationRequest) {

		if (userRepository.existsByUsername(registerationRequest.getUsername())
				|| userRepository.existsByEmailIgnoreCase(registerationRequest.getEmail())) {
			throw new ResourceAlreadyExistException("user already exists");
		}

		User user = new User();
		user.setFirstname(registerationRequest.getFirstname());
		user.setLastname(registerationRequest.getLastname());

		user.setAge(registerationRequest.getAge());
		user.setEmail(registerationRequest.getEmail());

		user.setRegisteredDate(new Date());
		String username = registerationRequest.getUsername();
		String password = registerationRequest.getPassword();

		user.setUsername(username);
		user.setPassword(password);
		// add default Role -> ROLE_USER
		List<Role> roles = new ArrayList<Role>();
		Role role = roleRepository.findByName(RoleName.USER);
		if (role == null) {
			throw new ResourceNotFoundException("USER role is not defined");
		}
		roles.add(role);
		user.setRoles(roles);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepository.save(user);
	}

	@Override
	public User updateUser(Long id, UpdateUserRequest user) {
		Optional<User> existingUser = userRepository.findById(id);
		if (!existingUser.isPresent()) {
			throw new ResourceNotFoundException("User[ID-" + id + "] is not existing");
		}

		if (!existingUser.get().getUsername().equals(user.getUsername())) {
			if (userRepository.existsByUsername(user.getUsername())) {
				throw new ResourceAlreadyExistException("username already exists");
			}
		}

		existingUser.get().setUsername(user.getUsername());
		existingUser.get().setAge(user.getAge());
		existingUser.get().setFirstname(user.getFirstname());
		existingUser.get().setEmail(user.getEmail());
		existingUser.get().setLastname(user.getLastname());

		return userRepository.save(existingUser.get());

	}

	// below method is used by oauth2 to authenticate user Login
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> existingUser = null;
		if (username.contains("@")) {
			// load by email
			existingUser = userRepository.findByEmailIgnoreCase(username);
		} else {
			// load by username
			existingUser = userRepository.findByUsername(username);
		}
		if (existingUser == null || !existingUser.isPresent()) {
			throw new BadCredentialsException(
					messages.getMessage("message.badCredentials", null, LocaleContextHolder.getLocale()));

		}

		new AccountStatusUserDetailsChecker().check(existingUser.get());
		return existingUser.get();
	}

	@Override
	public void changePassword(ChangePasswordRequest changePasswordRequest) {

		User user = getUserById(getCurrentAuthenticatedUser().getId());

		boolean match = passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword());

		if (match == true) {
			user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
			userRepository.save(user);

		} else {
			throw new BusinessException(
					messages.getMessage("error.invalidOldPassword", null, LocaleContextHolder.getLocale()));
		}
	}

	@Override
	public User getCurrentAuthenticatedUser() {
		User currentAuthenticatedUser = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		if (principal instanceof User) {
			currentAuthenticatedUser = ((User) principal);
		}
		return currentAuthenticatedUser;
	}

	public String getCurrentAuthenticatedUsername() {
		return getCurrentAuthenticatedUser().getUsername();
	}

	@Override
	@Transactional
	public boolean forgetPassword(String user) {
		Optional<User> existingUser = null;
		if (user.contains("@")) {
			// forgot by email
			existingUser = userRepository.findByEmailIgnoreCase(user);
		} else {
			// forgot by username
			existingUser = userRepository.findByUsername(user);
		}

		if (existingUser == null || !existingUser.isPresent()) {
			throw new ResourceNotFoundException(messages.getMessage("error.forgotpassword.usernameNotFound", null,
					LocaleContextHolder.getLocale()));
		}

		// create token
		PasswordResetToken confirmationToken = new PasswordResetToken(existingUser.get());

		// save it
		confirmationTokenRepository.save(confirmationToken);

		// send it by mail to user email
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		String subject = messages.getMessage("message.resetpassword.email.subject", null,
				LocaleContextHolder.getLocale());

		Mail mail = new Mail();
		mail.setTo(existingUser.get().getEmail());
		mail.setSubject(subject);
		mail.setTemplate("/emails/reset-password.ftl");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("resetpasswordLink", getAppUrl(request) + "/ext/confirm-reset?token=" + confirmationToken.getToken());
		mail.setModel(model);
		try {

			emailService.sendEmail(mail);
		} catch (MessagingException | IOException | TemplateException e) {
			log.error("error happened while sending email:", e.getMessage());
			throw new RuntimeException(e.getMessage());
		}

		return true;
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@Override
	public ModelAndView validateResetToken(String confirmationToken, ModelAndView modelAndView) {
		PasswordResetToken token = confirmationTokenRepository.findByToken(confirmationToken);

		if (token != null) {
			// check if token is expired
			final Calendar cal = Calendar.getInstance();
			if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
				modelAndView.addObject("message", "Sorry, This link is Expired!");
				modelAndView.setViewName("error.html");
			} else {

				Optional<User> user = userRepository.findByUsername(token.getUser().getUsername());
				user.get().setEnabled(true);
				userRepository.save(user.get());

				modelAndView.addObject("user", user);
				modelAndView.addObject("username", user.get().getUsername());
				modelAndView.setViewName("resetPassword");
			}

		} else {
			modelAndView.addObject("message", "The link is invalid or broken!");
			modelAndView.setViewName("error.html");
		}
		return modelAndView;
	}

	@Override
	public ModelAndView resetPassword(ModelAndView modelAndView, User user) {
		if (user.getUsername() != null) {
			// use email to find user
			Optional<User> tokenUser = userRepository.findByUsername(user.getUsername());
			if (!tokenUser.isPresent()) {
				throw new ResourceNotFoundException(messages.getMessage("error.forgotpassword.usernameNotFound", null,
						LocaleContextHolder.getLocale()));
			}
			tokenUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(tokenUser.get());
			modelAndView.addObject("message",
					"Password successfully reset. You can now log in with the new credentials.");
			modelAndView.setViewName("successResetPassword");
			confirmationTokenRepository.deleteByUserId(tokenUser.get().getId());
		} else {
			modelAndView.addObject("message", "The link is invalid or broken!");
			modelAndView.setViewName("error");
		}

		return modelAndView;
	}

}
