package com.expense_tracker.password.service;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense_tracker.exception.user.UserNotFoundException;
import com.expense_tracker.password.entity.PasswordResetToken;
import com.expense_tracker.password.repository.PasswordResetTokenRepository;
import com.expense_tracker.user.entity.UserInfo;

@Service
public class PasswordResetTokenService {

	private static final Logger log = LoggerFactory.getLogger(PasswordResetTokenService.class);

	private final PasswordResetTokenRepository passwordResetTokenRepository;

	public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
		this.passwordResetTokenRepository = passwordResetTokenRepository;
	}

	// method createPasswordResetTokenForUser
	@Transactional
	public void createPasswordResetTokenForUser(String passwordToken, UserInfo userInfo) {
		log.info("Start create password reset token for user : {} ", userInfo);
		PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, userInfo);
		passwordResetTokenRepository.save(passwordResetToken);
		log.info("Save in bdd passwordResetToken : {} ", passwordResetToken);
	}

	// validate token
	public String validatePasswordResetToken(String passwordToken) {
		PasswordResetToken token = passwordResetTokenRepository.findByToken(passwordToken);
		if (token == null) {
			return "Invalid password reset token";
		}
		// UserInfo userInfo = token.getUserInfo();

		Calendar calendar = Calendar.getInstance();
		// Check expire date token
		if (

		token.getTokenExpirationTime().getTime() - calendar.getTime().getTime() < 0) {
			return "Link expired, resend link";
		}
		return "valid";
	}

	// findUserByPasswordToken
	public UserInfo findUserByPasswordToken(String token) {
		PasswordResetToken pwd = passwordResetTokenRepository.findByToken(token);
		if (pwd == null) {
			log.error("PasswordResetToken not found  with token:  {}", token);
			return null;
		}
		UserInfo userInfo = pwd.getUserInfo();
		if (userInfo == null) {
			// UserInfo est null, gérez cette situation
			throw new UserNotFoundException("UserInfo associé au token est null");
		}
		log.info("PasswordResetToken  found  user :  {}", userInfo);

		return userInfo;

	}

}
