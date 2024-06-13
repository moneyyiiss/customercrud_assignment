package com.sunbase.customercrud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sunbase.customercrud.exception.ResourceNotFoundException;
import com.sunbase.customercrud.model.Customer;
import com.sunbase.customercrud.model.User;
import com.sunbase.customercrud.payload.LoginRequest;
import com.sunbase.customercrud.repository.UserRepository;
import com.sunbase.customercrud.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * CustomerController handles customer-related endpoints for the application.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new customer.
     *
     * @param customer the customer details.
     * @param authentication the authentication token.
     * @return the created customer.
     */
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.createCustomer(customer, user);
    }

    /**
     * Updates an existing customer.
     *
     * @param id the customer ID.
     * @param customerDetails the updated customer details.
     * @param authentication the authentication token.
     * @return the updated customer.
     */
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.updateCustomer(id, customerDetails, user);
    }

    /**
     * Retrieves all customers with optional pagination and search parameters.
     *
     * @param authentication the authentication token.
     * @param page the page number (default is 0).
     * @param size the number of records per page (default is 10).
     * @param sortBy the field to sort by (default is 'id').
     * @param searchBy the field to search by (e.g., 'firstName', 'city', 'email', 'phone').
     * @param searchValue the value to search for.
     * @return a page of customers.
     */
    @GetMapping
    public Page<Customer> getAllCustomers(Authentication authentication,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(required = false) String searchBy,
                                          @RequestParam(required = false) String searchValue) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.getAllCustomers(user, page, size, sortBy, searchBy, searchValue);
    }

    /**
     * Retrieves a customer by ID.
     *
     * @param id the customer ID.
     * @param authentication the authentication token.
     * @return the customer details.
     */
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.getCustomerById(id, user);
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id the customer ID.
     * @param authentication the authentication token.
     * @return a success message.
     */
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id, Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        customerService.deleteCustomer(id, user);
        return "Customer deleted successfully";
    }

    /**
     * Syncs customers from a remote API.
     *
     * @param loginRequest the login request containing the username and password.
     * @param authentication the authentication token.
     * @return a success message.
     * @throws JsonProcessingException if there is an error processing JSON.
     */
    @PostMapping("/sync")
    public ResponseEntity<String> syncCustomers(@RequestBody LoginRequest loginRequest, Authentication authentication) throws JsonProcessingException {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        customerService.syncCustomers(loginRequest, user);
        return ResponseEntity.ok("Customers synced successfully");
    }
}
