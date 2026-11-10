/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.attendance;

/**
 *
 * @author alfie
 */

// Java Time API: used instead of storing dates and times as string.
import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceRecord {

    // Encapsulation: private fields.
    // Immutability: all fields are final; records cannot be modified after creation.
    private final String employeeNumber;
    private final String lastName;
    private final String firstName;
    private final LocalDate date;
    private final LocalTime logIn;
    private final LocalTime logOut;

    // Constructor: initializes attendance data.
    public AttendanceRecord(
            String employeeNumber,
            String lastName,
            String firstName,
            LocalDate date,
            LocalTime logIn,
            LocalTime logOut) {

        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getLogIn() {
        return logIn;
    }

    public LocalTime getLogOut() {
        return logOut;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

