package com.sunbase.customercrud.repository;

import com.sunbase.customercrud.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
