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
    Page<Customer> findByUserAndFirstNameContaining(User user, String firstName, Pageable pageable);
    Page<Customer> findByUserAndCityContaining(User user, String city, Pageable pageable);
    Page<Customer> findByUserAndEmailContaining(User user, String email, Pageable pageable);
    Page<Customer> findByUserAndPhoneContaining(User user, String phone, Pageable pageable);

}