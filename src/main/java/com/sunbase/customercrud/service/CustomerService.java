package com.sunbase.customercrud.service;

import com.sunbase.customercrud.exception.ResourceNotFoundException;
import com.sunbase.customercrud.exception.UnauthorizedException;
import com.sunbase.customercrud.model.Customer;
import com.sunbase.customercrud.model.User;
import com.sunbase.customercrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

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

}
