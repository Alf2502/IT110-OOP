/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.payroll;

/**
 *
 * @author alfie
 */

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class PayrollResult {

    private final String employeeNumber;
    private final PayrollPeriod payrollPeriod;
    private final LocalDate generatedDate;

    private final PayrollEarnings earnings;
    private final PayrollDeductions deductions;
    private final EmployerContributions employerContributions;

    private final BigDecimal netPay;

    public PayrollResult(
            String employeeNumber,
            PayrollPeriod payrollPeriod,
            LocalDate generatedDate,
            PayrollEarnings earnings,
            PayrollDeductions deductions,
            EmployerContributions employerContributions) {

        if (employeeNumber == null || employeeNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee number cannot be blank.");
        }

        this.employeeNumber = employeeNumber.trim();
        this.payrollPeriod = Objects.requireNonNull(
                payrollPeriod, "Payroll period cannot be null.");
        this.generatedDate = Objects.requireNonNull(
                generatedDate, "Generated date cannot be null.");

        this.earnings = Objects.requireNonNull(
                earnings, "Payroll earnings cannot be null.");
        this.deductions = Objects.requireNonNull(
                deductions, "Payroll deductions cannot be null.");
        this.employerContributions = Objects.requireNonNull(
                employerContributions, "Employer contributions cannot be null.");

        if (deductions.getTotalDeductions()
                .compareTo(earnings.getGrossCompensation()) > 0) {
            throw new IllegalArgumentException(
                    "Total deductions cannot exceed gross compensation.");
        }

        this.netPay = earnings.getGrossCompensation()
                .subtract(deductions.getTotalDeductions());
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public PayrollPeriod getPayrollPeriod() {
        return payrollPeriod;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public PayrollEarnings getEarnings() {
        return earnings;
    }

    public PayrollDeductions getDeductions() {
        return deductions;
    }

    public EmployerContributions getEmployerContributions() {
        return employerContributions;
    }

    public BigDecimal getGrossCompensation() {
        return earnings.getGrossCompensation();
    }

    public BigDecimal getTotalEmployeeDeductions() {
        return deductions.getTotalDeductions();
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public BigDecimal getTotalEmployerContributions() {
        return employerContributions.getTotalContributions();
    }

    public boolean hasDeductions() {
        return deductions.hasDeductions();
    }

    public boolean isPositiveNetPay() {
        return netPay.compareTo(BigDecimal.ZERO) > 0;
    }
}
