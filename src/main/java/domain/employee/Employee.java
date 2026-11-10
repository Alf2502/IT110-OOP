/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.employee;

/**
 *
 * @author alfie
 */



import domain.person.Person;

public final class Employee extends Person {

    private final String employeeNumber;

    private final String sssNumber;
    private final String philHealthNumber;
    private final String tin;
    private final String pagIbigNumber;

    private String status;
    private String position;
    private String immediateSupervision;

    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossRate;
    private double monthlyRate;

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
            double basicSalary,
            double riceSubsidy,
            double phoneAllowance,
            double clothingAllowance,
            double grossRate,
            double monthlyRate) {

        super(firstName, lastName, birthDate, address, phoneNumber);

        this.employeeNumber = requireText(
                employeeNumber, "Employee number"
        );

        this.sssNumber = sssNumber;
        this.philHealthNumber = philHealthNumber;
        this.tin = tin;
        this.pagIbigNumber = pagIbigNumber;
        this.status = requireText(status, "Employment status");
        this.position = requireText(position, "Position");
        this.immediateSupervision = immediateSupervision;

        setBasicSalary(basicSalary);
        setRiceSubsidy(riceSubsidy);
        setPhoneAllowance(phoneAllowance);
        setClothingAllowance(clothingAllowance);
        setGrossRate(grossRate);
        setMonthlyRate(monthlyRate);
    }

    // Identity

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    // Government identification

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

    // Employment information

    public String getStatus() {
        return status;
    }

    public String getPosition() {
        return position;
    }

    public String getImmediateSupervision() {
        return immediateSupervision;
    }

    public void setStatus(String status) {
        this.status = requireText(status, "Employment status");
    }

    public void setPosition(String position) {
        this.position = requireText(position, "Position");
    }

    public void setImmediateSupervision(String immediateSupervision) {
        this.immediateSupervision = immediateSupervision;
    }

    // Compensation information

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getRiceSubsidy() {
        return riceSubsidy;
    }

    public double getPhoneAllowance() {
        return phoneAllowance;
    }

    public double getClothingAllowance() {
        return clothingAllowance;
    }

    public double getGrossRate() {
        return grossRate;
    }

    public double getMonthlyRate() {
        return monthlyRate;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = requireNonNegative(
                basicSalary, "Basic salary"
        );
    }

    public void setRiceSubsidy(double riceSubsidy) {
        this.riceSubsidy = requireNonNegative(
                riceSubsidy, "Rice subsidy"
        );
    }

    public void setPhoneAllowance(double phoneAllowance) {
        this.phoneAllowance = requireNonNegative(
                phoneAllowance, "Phone allowance"
        );
    }

    public void setClothingAllowance(double clothingAllowance) {
        this.clothingAllowance = requireNonNegative(
                clothingAllowance, "Clothing allowance"
        );
    }

    public void setGrossRate(double grossRate) {
        this.grossRate = requireNonNegative(
                grossRate, "Gross rate"
        );
    }

    public void setMonthlyRate(double monthlyRate) {
        this.monthlyRate = requireNonNegative(
                monthlyRate, "Monthly rate"
        );
    }

    // Business behavior

    public double calculateTotalAllowances() {
        return riceSubsidy + phoneAllowance + clothingAllowance;
    }

    public double calculateTotalCompensation() {
        return basicSalary + calculateTotalAllowances();
    }

    public boolean isActive() {
        return "Active".equalsIgnoreCase(status);
    }

    @Override
    public String getPersonType() {
        return "Employee";
    }

    private double requireNonNegative(double value, String fieldName) {
        if (!Double.isFinite(value) || value < 0) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be negative or non-finite."
            );
        }

        return value;
    }
}
