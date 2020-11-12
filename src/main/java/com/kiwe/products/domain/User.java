package com.kiwe.products.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PROFILE_USER")
@Getter
@Setter
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;

	@Column(name = "USERNAME", nullable = false, unique = true)
	@NotNull(message = "username must not be null")
	@NotEmpty(message = "username must not be empty")
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	@JsonIgnore
	@ToString.Exclude
	private String password;

	@Column(name = "FIRST_NAME", nullable = false)
	private String firstname;

	@Column(name = "LAST_NAME", nullable = false)
	@NotNull(message = "lastname must not be null")
	@NotEmpty(message = "lastname must not be empty")
	private String lastname;

	@Column(name = "EMAIL", nullable = false, unique = true)
	private String email;

	@Column(name = "AGE", nullable = false)
	private int age;

	@Column(name = "REGISTERED_DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date registeredDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ROLES", joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID") }, inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID") })
	private List<Role> roles;

	@Column(name = "ENABLED")
	private boolean enabled = true;

	@Column(name = "ACCOUNT_NON_EXPIRED")
	@JsonIgnore
	@ToString.Exclude
	private boolean accountNonExpired = true;

	@Column(name = "CREDENTIALS_NON_EXPIRED")
	@JsonIgnore
	@ToString.Exclude
	private boolean credentialsNonExpired = true;

	@Column(name = "ACCOUNT_NON_LOCKED")
	@JsonIgnore
	@ToString.Exclude
	private boolean accountNonLocked = true;

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		getRoles().forEach(role -> {
			// authorities.add(new SimpleGrantedAuthority(role.getName()));
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", email=" + email + ", registeredDate=" + registeredDate + ", enabled=" + enabled + "]";
	}

}
