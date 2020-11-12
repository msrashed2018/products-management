package com.kiwe.products.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PasswordResetToken {

	private static final int EXPIRATION = 60 * 24; // one day

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TOKEN_ID")
	private Long id;

	@Column(name = "TOKEN", nullable = false)
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(nullable = false, name = "USER_ID")
	private User user;

	public PasswordResetToken() {
	}

	public PasswordResetToken(User user) {
		this.user = user;
		expiryDate = calculateExpiryDate(EXPIRATION);
		token = UUID.randomUUID().toString();
	}

	private Date calculateExpiryDate(final int expiryTimeInMinutes) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}
}
