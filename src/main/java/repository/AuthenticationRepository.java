/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author alfie
 */

import domain.authentication.UserAccount;
import java.io.IOException;
import java.util.Optional;

public interface AuthenticationRepository {

    // Find an account using its username
    Optional<UserAccount> findByUsername(
            String username
    ) throws IOException;

    // Check whether a username already exists
    boolean existsByUsername(
            String username
    ) throws IOException;

    // Save a new user account
    void save(UserAccount account) throws IOException;

    // Update an existing user account
    void update(UserAccount account) throws IOException;

    // Update a user's password hash
    void updatePassword(
            String username,
            String newPasswordHash
    ) throws IOException;
}
