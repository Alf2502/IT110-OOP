/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author alfie
 */

public final class Employee {

    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String birthDate;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philHealthNumber;
    private String tin;
    private String pagIbigNumber;
    private String status;
    private String position;
    private String immediateSupervision;

    private Double basicSalary;
    private Double riceSubsidy;
    private Double phoneAllowance;
    private Double clothingAllowance;
    private Double grossRate;
    private Double monthlyRate;

    public Employee() {
        this.basicSalary = 0.0;
        this.riceSubsidy = 0.0;
        this.phoneAllowance = 0.0;
        this.clothingAllowance = 0.0;
        this.grossRate = 0.0;
        this.monthlyRate = 0.0;
    }

    public Employee(
            String employeeNumber,
            String lastName,
            String firstName,
            String birthDate,
            String address,
            String phoneNumber,
            String sssNumber,
            String philHealthNumber,
            String tin,
            String pagIbigNumber,
            String status,
            String position,
            String immediateSupervision,
            String basicSalary,
            String riceSubsidy,
            String phoneAllowance,
            String clothingAllowance,
            String grossRate,
            String monthlyRate) {

        this();

        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.tin = tin;
        this.pagIbigNumber = pagIbigNumber;
        this.status = status;
        this.position = position;
        this.immediateSupervision = immediateSupervision;

        setBasicSalary(parseDoubleOrZero(basicSalary));
        setRiceSubsidy(parseDoubleOrZero(riceSubsidy));
        setPhoneAllowance(parseDoubleOrZero(phoneAllowance));
        setClothingAllowance(parseDoubleOrZero(clothingAllowance));
        setGrossRate(parseDoubleOrZero(grossRate));
        setMonthlyRate(parseDoubleOrZero(monthlyRate));
    }

    private static double parseDoubleOrZero(String value) {
        if (value == null || value.isBlank()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid numeric value: " + value, e);
        }
    }

    private double validateNonNegative(
            Double value, String fieldName) {

        if (value == null
                || !Double.isFinite(value)
                || value < 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be a finite, non-negative number.");
        }

        return value;
    }

    public String getFullName() {
        String first = firstName == null ? "" : firstName.trim();
        String last = lastName == null ? "" : lastName.trim();

        return (first + " " + last).trim();
    }

    public String toCSV() {
        return String.join(",",
                csvField(employeeNumber),
                csvField(lastName),
                csvField(firstName),
                csvField(birthDate),
                csvField(address),
                csvField(phoneNumber),
                csvField(sssNumber),
                csvField(philHealthNumber),
                csvField(tin),
                csvField(pagIbigNumber),
                csvField(status),
                csvField(position),
                csvField(immediateSupervision),
                csvField(basicSalary),
                csvField(riceSubsidy),
                csvField(phoneAllowance),
                csvField(clothingAllowance),
                csvField(grossRate),
                csvField(monthlyRate)
        );
    }

    private String csvField(Object value) {
        if (value == null) {
            return "";
        }

        String text = value.toString();

        if (text.contains(",")
                || text.contains("\"")
                || text.contains("\n")
                || text.contains("\r")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }

        return text;
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

    public String getBirthDate() {
        return birthDate;
    }

    // Temporary compatibility method for older code.
    public String getBirthDay() {
        return getBirthDate();
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public String getPhilHealthNumber() {
        return philHealthNumber;
    }

    public String getTin() {
        return tin;
    }

    public String getPagIbigNumber() {
        return pagIbigNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getPosition() {
        return position;
    }

    public String getImmediateSupervision() {
        return immediateSupervision;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public Double getRiceSubsidy() {
        return riceSubsidy;
    }

    public Double getPhoneAllowance() {
        return phoneAllowance;
    }

    public Double getClothingAllowance() {
        return clothingAllowance;
    }

    public Double getGrossRate() {
        return grossRate;
    }

    public Double getMonthlyRate() {
        return monthlyRate;
    }

    // Setters

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setSssNumber(String sssNumber) {
        this.sssNumber = sssNumber;
    }

    public void setPhilHealthNumber(String philHealthNumber) {
        this.philHealthNumber = philHealthNumber;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public void setPagIbigNumber(String pagIbigNumber) {
        this.pagIbigNumber = pagIbigNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setImmediateSupervision(String immediateSupervision) {
        this.immediateSupervision = immediateSupervision;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary =
                validateNonNegative(basicSalary, "Basic salary");
    }

    public void setRiceSubsidy(Double riceSubsidy) {
        this.riceSubsidy =
                validateNonNegative(riceSubsidy, "Rice subsidy");
    }

    public void setPhoneAllowance(Double phoneAllowance) {
        this.phoneAllowance =
                validateNonNegative(phoneAllowance, "Phone allowance");
    }

    public void setClothingAllowance(Double clothingAllowance) {
        this.clothingAllowance =
                validateNonNegative(clothingAllowance, "Clothing allowance");
    }

    public void setGrossRate(Double grossRate) {
        this.grossRate =
                validateNonNegative(grossRate, "Gross rate");
    }

    public void setMonthlyRate(Double monthlyRate) {
        this.monthlyRate =
                validateNonNegative(monthlyRate, "Monthly rate");
    }
}
