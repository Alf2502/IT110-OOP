/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author alfie
 */

import domain.authentication.UserAccount;
import infrastructure.security.PasswordHasher;
import repository.AuthenticationRepository;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    public AuthenticationService(
            AuthenticationRepository authenticationRepository) {

        this.authenticationRepository = Objects.requireNonNull(
                authenticationRepository,
                "Authentication repository cannot be null."
        );
    }

    // Authenticate an account
    public Optional<UserAccount> authenticate(
            String username,
            char[] password) throws IOException {

        validateUsername(username);

        if (password == null || password.length == 0) {
            return Optional.empty();
        }

        Optional<UserAccount> account =
                authenticationRepository.findByUsername(username);

        if (account.isEmpty()) {
            return Optional.empty();
        }

        UserAccount user = account.get();

        if (!user.isActive()) {
            return Optional.empty();
        }

        boolean passwordMatches = PasswordHasher.verify(
                password,
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    // Create an account
    public void createAccount(
            String username,
            char[] password,
            String role) throws IOException {

        validateUsername(username);
        validatePassword(password);
        validateRole(role);

        if (authenticationRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        String passwordHash = PasswordHasher.hash(password);

        UserAccount account = new UserAccount(
                username.trim(),
                passwordHash,
                role.trim().toUpperCase(),
                true
        );

        authenticationRepository.save(account);
    }

    // Change an account password
    public void changePassword(
            String username,
            char[] newPassword) throws IOException {

        validateUsername(username);
        validatePassword(newPassword);

        if (!authenticationRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "User account not found."
            );
        }

        String newPasswordHash = PasswordHasher.hash(newPassword);

        authenticationRepository.updatePassword(
                username,
                newPasswordHash
        );
    }

    // Activate or deactivate an account
    public void updateAccountStatus(
            String username,
            boolean active) throws IOException {

        validateUsername(username);

        UserAccount account = authenticationRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User account not found."
                ));

        account.setActive(active);

        authenticationRepository.update(account);
    }

    private void validateUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username cannot be empty."
            );
        }
    }

    private void validatePassword(char[] password) {

        if (password == null || password.length < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }
    }

    private void validateRole(String role) {

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "User role cannot be empty."
            );
        }
    }
}

