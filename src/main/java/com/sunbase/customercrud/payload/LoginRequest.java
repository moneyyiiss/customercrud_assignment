package com.sunbase.customercrud.payload;

import lombok.Data;

/**
 * Payload class representing a login request.
 */
@Data
public class LoginRequest {

    /**
     * The username provided by the user for login.
     */
    private String username;

    /**
     * The password provided by the user for login.
     */
    private String password;
}
