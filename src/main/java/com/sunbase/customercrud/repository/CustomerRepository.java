package com.sunbase.customercrud.repository;

import com.sunbase.customercrud.model.Customer;
import com.sunbase.customercrud.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Customer entities.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a paginated list of customers for a given user.
     *
     * @param user the user whose customers are to be found.
     * @param pageable the pagination information.
     * @return a page of customers.
     */
    Page<Customer> findByUser(User user, Pageable pageable);

    /**
     * Finds a customer by email.
     *
     * @param email the email of the customer to be found.
     * @return an Optional containing the customer if found, or empty if not found.
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Finds a paginated list of customers for a given user, filtering by first name.
     *
     * @param user the user whose customers are to be found.
     * @param firstName the first name to filter by.
     * @param pageable the pagination information.
     * @return a page of customers filtered by first name.
     */
    Page<Customer> findByUserAndFirstNameContaining(User user, String firstName, Pageable pageable);

    /**
     * Finds a paginated list of customers for a given user, filtering by city.
     *
     * @param user the user whose customers are to be found.
     * @param city the city to filter by.
     * @param pageable the pagination information.
     * @return a page of customers filtered by city.
     */
    Page<Customer> findByUserAndCityContaining(User user, String city, Pageable pageable);

    /**
     * Finds a paginated list of customers for a given user, filtering by email.
     *
     * @param user the user whose customers are to be found.
     * @param email the email to filter by.
     * @param pageable the pagination information.
     * @return a page of customers filtered by email.
     */
    Page<Customer> findByUserAndEmailContaining(User user, String email, Pageable pageable);

    /**
     * Finds a paginated list of customers for a given user, filtering by phone number.
     *
     * @param user the user whose customers are to be found.
     * @param phone the phone number to filter by.
     * @param pageable the pagination information.
     * @return a page of customers filtered by phone number.
     */
    Page<Customer> findByUserAndPhoneContaining(User user, String phone, Pageable pageable);
}
