package com.sunbase.customercrud.repository;

import com.sunbase.customercrud.model.Customer;
import com.sunbase.customercrud.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByUser(User user, Pageable pageable);
    Optional<Customer> findByEmail(String email);

}
