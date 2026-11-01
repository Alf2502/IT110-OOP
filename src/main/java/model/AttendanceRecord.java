/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author alfie
 */

public class AttendanceRecord {

    // Uses "private final" to preserves immutability
    private final String employeeNumber;
    private final String lastName;
    private final String firstName;
    private final String date;
    private final String logIn;
    private final String logOut;

    public AttendanceRecord(
            String employeeNumber,
            String lastName,
            String firstName,
            String date,
            String logIn,
            String logOut) {

        this.employeeNumber = requireValue(
                employeeNumber, "Employee number");

        this.lastName = normalize(lastName);
        this.firstName = normalize(firstName);

        this.date = requireValue(date, "Attendance date");

        // Empty login/logout values are allowed for incomplete records.
        this.logIn = normalize(logIn);
        this.logOut = normalize(logOut);
    }

    // Prevents missing employee numbers and dates
    private static String requireValue(
            String value, String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty.");
        }

        return value.trim();
    }

    // Handles null names and log values safely
    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    // Getters

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    // Combines the first and last name consistently
    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }

    public String getDate() {
        return date;
    }

    public String getLogIn() {
        return logIn;
    }

    public String getLogOut() {
        return logOut;
    }
}

