package com.sunbase.customercrud.service;

import com.sunbase.customercrud.exception.ResourceNotFoundException;
import com.sunbase.customercrud.model.Customer;
import com.sunbase.customercrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    // create customer
    public Customer createCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    //update customer
    public Customer updateCustomer(Long id, Customer customerDetails){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
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
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    // get customer by id
    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
    }

    // deleteCustomer
    public void deleteCustomer(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }





































}
