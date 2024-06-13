package com.sunbase.customercrud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a customer.
 */
@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    /**
     * The unique identifier for a customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The first name of the customer.
     */
    private String firstName;

    /**
     * The last name of the customer.
     */
    private String lastName;

    /**
     * The street address of the customer.
     */
    private String street;

    /**
     * The full address of the customer.
     */
    private String address;

    /**
     * The city where the customer resides.
     */
    private String city;

    /**
     * The state where the customer resides.
     */
    private String state;

    /**
     * The email address of the customer.
     */
    private String email;

    /**
     * The phone number of the customer.
     */
    private String phone;

    /**
     * The user associated with the customer.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
