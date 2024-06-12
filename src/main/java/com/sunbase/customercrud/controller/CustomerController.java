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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserRepository userRepository;

    // create customer
    // http://localhost:8080/api/customers
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer , Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.createCustomer(customer, user);
    }

    // update customer
    // http://localhost:8080/api/customers/id
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.updateCustomer(id, customerDetails, user);
    }

    // get all customer
    // http://localhost:8080/api/customers
    @GetMapping
    public Page<Customer> getAllCustomers(Authentication authentication,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.getAllCustomers(user, page, size, sortBy);
    }

    // get customer by id
    // http://localhost:8080/api/customers/id
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return customerService.getCustomerById(id, user);
    }

    // delete customer by id
    // http://localhost:8080/api/customers/id
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id, Authentication authentication){
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() ->new ResourceNotFoundException("User not found"));
        customerService.deleteCustomer(id, user);
        return "Customer deleted successfully";
    }


    // sync customers from remote API
    @PostMapping("/sync")
    public String syncCustomers(@RequestBody LoginRequest loginRequest, Authentication authentication) throws JsonProcessingException {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        customerService.syncCustomers(loginRequest, user);
        return "Customers synced successfully";
    }
}
