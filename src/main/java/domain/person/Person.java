/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.person;

/**
 *
 * @author alfie
 */


public abstract class Person {

    private String firstName;
    private String lastName;
    private String birthDate;
    private String address;
    private String phoneNumber;

    protected Person(
            String firstName,
            String lastName,
            String birthDate,
            String address,
            String phoneNumber) {

        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setAddress(address);
        setPhoneNumber(phoneNumber);
    }

    // Encapsulated access

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Controlled modification

    public final void setFirstName(String firstName) {
        this.firstName = requireText(firstName, "First name");
    }

    public final void setLastName(String lastName) {
        this.lastName = requireText(lastName, "Last name");
    }

    public final void setBirthDate(String birthDate) {
        this.birthDate = requireText(birthDate, "Birth date");
    }

    public final void setAddress(String address) {
        this.address = requireText(address, "Address");
    }

    public final void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = requireText(phoneNumber, "Phone number");
    }

    // Shared behavior

    public String getFullName() {
        return firstName + " " + lastName;
    }

    protected String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }

    // Abstract behavior for subclasses

    public abstract String getPersonType();
}
