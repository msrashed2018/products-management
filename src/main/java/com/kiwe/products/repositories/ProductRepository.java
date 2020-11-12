package com.kiwe.products.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kiwe.products.domain.Product;
import com.kiwe.products.domain.User;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findByOwner(User currentAuthenticatedUser, Pageable pageable);

	Optional<Product> findByIdAndOwner(long id, User currentAuthenticatedUser);

	boolean existsByIdAndOwner(long id, User currentAuthenticatedUser);

}
