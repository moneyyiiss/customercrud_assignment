package com.sunbase.customercrud.repository;

import com.sunbase.customercrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user to be found.
     * @return an Optional containing the user if found, or empty if not found.
     */
    Optional<User> findByUsername(String username);
}
