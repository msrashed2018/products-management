package com.kiwe.products.tasks;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kiwe.products.repositories.PasswordResetTokenRepository;

@Service
@Transactional
public class TokensPurgeTask {

	@Autowired
	PasswordResetTokenRepository passwordTokenRepository;

	private static final Logger logger = LoggerFactory.getLogger(TokensPurgeTask.class);

	@Scheduled(cron = "${purge.cron.expression}")
	public void purgeExpired() {
		logger.info("Token Purge Task is started");
		Date now = Date.from(Instant.now());
		passwordTokenRepository.deleteAllExpiredSince(now);
		logger.info("Token Purge Task is finished");
	}
}
