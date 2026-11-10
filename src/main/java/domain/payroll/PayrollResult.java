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
import java.math.RoundingMode;
import java.time.LocalDate;

public final class PayrollResult {

    private final String employeeNumber;
    private final LocalDate payrollDate;
    private final BigDecimal grossPay;
    private final BigDecimal totalAllowances;
    private final BigDecimal totalDeductions;
    private final BigDecimal netPay;

    public PayrollResult(
            String employeeNumber,
            LocalDate payrollDate,
            BigDecimal grossPay,
            BigDecimal totalAllowances,
            BigDecimal totalDeductions) {

        this.employeeNumber = requireText(
                employeeNumber, "Employee number"
        );

        this.payrollDate = requireNotNull(
                payrollDate, "Payroll date"
        );

        this.grossPay = requireNonNegative(
                grossPay, "Gross pay"
        );

        this.totalAllowances = requireNonNegative(
                totalAllowances, "Total allowances"
        );

        this.totalDeductions = requireNonNegative(
                totalDeductions, "Total deductions"
        );

        if (this.totalDeductions.compareTo(this.grossPay) > 0) {
            throw new IllegalArgumentException(
                    "Deductions cannot exceed gross pay."
            );
        }

        this.netPay = this.grossPay
                .subtract(this.totalDeductions)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public LocalDate getPayrollDate() {
        return payrollDate;
    }

    public BigDecimal getGrossPay() {
        return grossPay;
    }

    public BigDecimal getTotalAllowances() {
        return totalAllowances;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public boolean isPositiveNetPay() {
        return netPay.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean hasDeductions() {
        return totalDeductions.compareTo(BigDecimal.ZERO) > 0;
    }

    private static String requireText(
            String value, String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }

        return value.trim();
    }

    private static BigDecimal requireNonNegative(
            BigDecimal value, String fieldName) {

        requireNotNull(value, fieldName);

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be negative."
            );
        }

        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static <T> T requireNotNull(
            T value, String fieldName) {

        if (value == null) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be null."
            );
        }

        return value;
    }
}
