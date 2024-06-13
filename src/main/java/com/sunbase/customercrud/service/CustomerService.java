package com.sunbase.customercrud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunbase.customercrud.exception.ResourceNotFoundException;
import com.sunbase.customercrud.exception.UnauthorizedException;
import com.sunbase.customercrud.model.Customer;
import com.sunbase.customercrud.model.User;
import com.sunbase.customercrud.payload.LoginRequest;
import com.sunbase.customercrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing customers.
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Finds a customer by email.
     *
     * @param email the email of the customer to be found.
     * @return an Optional containing the customer if found, or empty if not found.
     */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Creates a new customer associated with the given user.
     *
     * @param customer the customer to be created.
     * @param user the user associated with the customer.
     * @return the created customer.
     */
    public Customer createCustomer(Customer customer, User user) {
        customer.setUser(user);
        return customerRepository.save(customer);
    }

    /**
     * Updates an existing customer associated with the given user.
     *
     * @param id the ID of the customer to be updated.
     * @param customerDetails the new details of the customer.
     * @param user the user associated with the customer.
     * @return the updated customer.
     */
    public Customer updateCustomer(Long id, Customer customerDetails, User user) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (!customer.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to update this customer");
        }
        customer.setFirstName(customerDetails.getFirstName());
        customer.setLastName(customerDetails.getLastName());
        customer.setStreet(customerDetails.getStreet());
        customer.setAddress(customerDetails.getAddress());
        customer.setCity(customerDetails.getCity());
        customer.setState(customerDetails.getState());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        return customerRepository.save(customer);
    }

    /**
     * Retrieves all customers for a given user with optional search criteria.
     *
     * @param user the user whose customers are to be found.
     * @param page the page number.
     * @param size the page size.
     * @param sortBy the field to sort by.
     * @param searchBy the field to search by (optional).
     * @param searchValue the value to search for (optional).
     * @return a page of customers.
     */
    public Page<Customer> getAllCustomers(User user, int page, int size, String sortBy, String searchBy, String searchValue) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        if (searchBy != null && searchValue != null) {
            switch (searchBy) {
                case "firstName":
                    return customerRepository.findByUserAndFirstNameContaining(user, searchValue, pageable);
                case "city":
                    return customerRepository.findByUserAndCityContaining(user, searchValue, pageable);
                case "email":
                    return customerRepository.findByUserAndEmailContaining(user, searchValue, pageable);
                case "phone":
                    return customerRepository.findByUserAndPhoneContaining(user, searchValue, pageable);
                default:
                    return customerRepository.findByUser(user, pageable);
            }
        } else {
            return customerRepository.findByUser(user, pageable);
        }
    }

    /**
     * Retrieves a customer by ID for a given user.
     *
     * @param id the ID of the customer to be retrieved.
     * @param user the user associated with the customer.
     * @return the customer.
     */
    public Customer getCustomerById(Long id, User user) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (!customer.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to view this customer");
        }
        return customer;
    }

    /**
     * Deletes a customer by ID for a given user.
     *
     * @param id the ID of the customer to be deleted.
     * @param user the user associated with the customer.
     */
    public void deleteCustomer(Long id, User user) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (!customer.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this customer");
        }
        customerRepository.delete(customer);
    }

    /**
     * Syncs customers from a remote API for a given user.
     *
     * @param loginRequest the login request containing credentials.
     * @param user the user associated with the customers.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    public void syncCustomers(LoginRequest loginRequest, User user) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Get the token
        String authUrl = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
        String authBody = String.format("{\"login_id\":\"%s\", \"password\":\"%s\"}", loginRequest.getUsername(), loginRequest.getPassword());
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.set("Content-Type", "application/json");
        HttpEntity<String> authEntity = new HttpEntity<>(authBody, authHeaders);

        ResponseEntity<String> authResponse = restTemplate.postForEntity(authUrl, authEntity, String.class);
        String rawToken = authResponse.getBody();

        // Extract the token from the response
        String token = extractToken(rawToken);

        // Step 2: Use the token to get the customer list
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list",
                HttpMethod.GET,
                entity,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode customerArray = objectMapper.readTree(responseBody);

        for (JsonNode customerNode : customerArray) {
            Customer remoteCustomer = new Customer();
            remoteCustomer.setFirstName(customerNode.get("first_name").asText());
            remoteCustomer.setLastName(customerNode.get("last_name").asText());
            remoteCustomer.setStreet(customerNode.get("street").asText());
            remoteCustomer.setAddress(customerNode.get("address").asText());
            remoteCustomer.setCity(customerNode.get("city").asText());
            remoteCustomer.setState(customerNode.get("state").asText());
            remoteCustomer.setEmail(customerNode.get("email").asText());
            remoteCustomer.setPhone(customerNode.get("phone").asText());
            remoteCustomer.setUser(user);

            Optional<Customer> existingCustomerOptional = customerRepository.findByEmail(remoteCustomer.getEmail());
            if (existingCustomerOptional.isPresent()) {
                // Update existing customer
                Customer existingCustomer = existingCustomerOptional.get();
                existingCustomer.setFirstName(remoteCustomer.getFirstName());
                existingCustomer.setLastName(remoteCustomer.getLastName());
                existingCustomer.setStreet(remoteCustomer.getStreet());
                existingCustomer.setAddress(remoteCustomer.getAddress());
                existingCustomer.setCity(remoteCustomer.getCity());
                existingCustomer.setState(remoteCustomer.getState());
                existingCustomer.setPhone(remoteCustomer.getPhone());
                customerRepository.save(existingCustomer);
            } else {
                // Insert new customer
                customerRepository.save(remoteCustomer);
            }
        }
    }

    /**
     * Extracts the token from the raw response.
     *
     * @param rawToken the raw token response.
     * @return the extracted token.
     */
    private String extractToken(String rawToken) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(rawToken);
            return rootNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract token", e);
        }
    }
}
