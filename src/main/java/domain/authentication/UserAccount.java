/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.authentication;

/**
 *
 * @author alfie
 */

public class UserAccount {

    private final String username;
    private String passwordHash;
    private final String role;
    private boolean active;

    public UserAccount(
            String username,
            String passwordHash,
            String role,
            boolean active) {

        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public void updatePasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasRole(String role) {
        return this.role.equalsIgnoreCase(role);
    }
}

