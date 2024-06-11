package com.sunbase.customercrud.controller;

import com.sunbase.customercrud.model.Customer;
import com.sunbase.customercrud.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    // create customer
    // http://localhost:8080/api/customers
    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer){
        return customerService.createCustomer(customer);
    }

    // update customer
    // http://localhost:8080/api/customers/id
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails){
        return customerService.updateCustomer(id, customerDetails);
    }

    // get all customer
    // http://localhost:8080/api/customers
    @GetMapping
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    // get customer by id
    // http://localhost:8080/api/customers/id
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id);
    }

    // delete customer by id
    // http://localhost:8080/api/customers/id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}























