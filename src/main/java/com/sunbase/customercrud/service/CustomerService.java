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

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    // create customer
    public Customer createCustomer(Customer customer, User user){
        customer.setUser(user);
        return customerRepository.save(customer);
    }

    //update customer
    public Customer updateCustomer(Long id, Customer customerDetails, User user){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
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

    //get all customers
    public Page<Customer> getAllCustomers(User user, int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return customerRepository.findByUser(user, pageable);
    }

    // get customer by id
    public Customer getCustomerById(Long id, User user){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if(!customer.getUser().getId().equals(user.getId())){
            throw new UnauthorizedException("You are not authorized to view this customer");

        }
        return customer;
    }

    // deleteCustomer
    public void deleteCustomer(Long id, User user){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if(!customer.getUser().getId().equals(user.getId())){
            throw new UnauthorizedException("You are not authorized to delete this customer");
        }
        customerRepository.delete(customer);
    }
//    // Sync customers from remote API
//    public void syncCustomers(LoginRequest loginRequest, User user) throws JsonProcessingException {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Step 1: Get the token
//        String authUrl = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";
//        String authBody = String.format("{\"login_id\":\"%s\", \"password\":\"%s\"}", loginRequest.getUsername(), loginRequest.getPassword());
//        HttpHeaders authHeaders = new HttpHeaders();
//        authHeaders.set("Content-Type", "application/json");
//        HttpEntity<String> authEntity = new HttpEntity<>(authBody, authHeaders);
//
//        ResponseEntity<String> authResponse = restTemplate.postForEntity(authUrl, authEntity, String.class);
//        String rawToken = authResponse.getBody();
//
//        // Extract the token from the response
//        String token = extractToken(rawToken);
//
//        // Step 2: Use the token to get the customer list
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//
//        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list",
//                HttpMethod.GET,
//                entity,
//                String.class
//        );
//
//        String responseBody = response.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode customerArray = objectMapper.readTree(responseBody);
//
//        for (JsonNode customerNode : customerArray) {
//            Customer remoteCustomer = new Customer();
//            remoteCustomer.setFirstName(customerNode.get("first_name").asText());
//            remoteCustomer.setLastName(customerNode.get("last_name").asText());
//            remoteCustomer.setStreet(customerNode.get("street").asText());
//            remoteCustomer.setAddress(customerNode.get("address").asText());
//            remoteCustomer.setCity(customerNode.get("city").asText());
//            remoteCustomer.setState(customerNode.get("state").asText());
//            remoteCustomer.setEmail(customerNode.get("email").asText());
//            remoteCustomer.setPhone(customerNode.get("phone").asText());
//            remoteCustomer.setUser(user);
//
//            Optional<Customer> existingCustomerOptional = customerRepository.findByEmail(remoteCustomer.getEmail());
//            if (existingCustomerOptional.isPresent()) {
//                // Update existing customer
//                Customer existingCustomer = existingCustomerOptional.get();
//                existingCustomer.setFirstName(remoteCustomer.getFirstName());
//                existingCustomer.setLastName(remoteCustomer.getLastName());
//                existingCustomer.setStreet(remoteCustomer.getStreet());
//                existingCustomer.setAddress(remoteCustomer.getAddress());
//                existingCustomer.setCity(remoteCustomer.getCity());
//                existingCustomer.setState(remoteCustomer.getState());
//                existingCustomer.setPhone(remoteCustomer.getPhone());
//                customerRepository.save(existingCustomer);
//            } else {
//                // Insert new customer
//                customerRepository.save(remoteCustomer);
//            }
//        }
//    }
//
//    private String extractToken(String rawToken) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(rawToken);
//            return rootNode.get("access_token").asText();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to extract token", e);
//        }
//    }

    // Sync customers from remote API
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
