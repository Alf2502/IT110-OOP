/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculation;

/**
 *
 * @author alfie
 */

import domain.employee.Employee;
import domain.payroll.EmployerContributions;
import domain.payroll.PayrollDeductions;
import domain.payroll.PayrollEarnings;
import domain.payroll.PayrollPeriod;
import domain.payroll.PayrollResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public class SalaryCalculator implements PayrollCalculator {

    
    @Override
    public PayrollResult calculatePayroll(Employee employee,Duration totalHours,LocalDate payrollDate) {

        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(totalHours, "Total hours cannot be null.");
        Objects.requireNonNull(payrollDate, "Payroll date cannot be null.");

        if (totalHours.isNegative()) {
            throw new IllegalArgumentException("Total hours cannot be negative.");
        }

        BigDecimal hours = BigDecimal.valueOf(
            totalHours.toMinutes() / 60.0
        );

        BigDecimal hourlyRate = BigDecimal.valueOf(employee.getHourlyRate());

        BigDecimal basicPay = hours.multiply(hourlyRate)
            .setScale(2, RoundingMode.HALF_UP);

        // Full monthly allowances, subject to employee eligibility.
        BigDecimal riceSubsidy = BigDecimal.valueOf(employee.getRiceSubsidy());
        BigDecimal phoneAllowance = BigDecimal.valueOf(employee.getPhoneAllowance());
        BigDecimal clothingAllowance = BigDecimal.valueOf(employee.getClothingAllowance());

        BigDecimal grossCompensation = basicPay
            .add(riceSubsidy)
            .add(phoneAllowance)
            .add(clothingAllowance);

        // Temporary classification; actual tax treatment must be verified.
        BigDecimal taxableCompensation = grossCompensation;
        BigDecimal nonTaxableCompensation = BigDecimal.ZERO;

        BigDecimal zero = BigDecimal.ZERO;

        PayrollPeriod period = new PayrollPeriod(
            YearMonth.from(payrollDate)
        );

        
        PayrollEarnings earnings = new PayrollEarnings(
                basicPay,
                riceSubsidy,
                phoneAllowance,
                clothingAllowance,
                taxableCompensation,
                nonTaxableCompensation
            );

        PayrollDeductions deductions = new PayrollDeductions(
        zero, zero, zero, zero, zero
            );

        EmployerContributions employerContributions =
            new EmployerContributions(zero, zero, zero);

        return new PayrollResult(
        employee.getEmployeeNumber(),
         period,
         payrollDate,
                    earnings,
                    deductions,
                    employerContributions
            );
        }

    @Override
    public double calculateGrossPay(
            Employee employee,
            Duration totalHours) {

        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(totalHours, "Total hours cannot be null.");

        double salary = calculateBaseSalary(employee, totalHours);

        double allowances = salary > 0
                ? calculateTotalAllowances(employee)
                : 0.0;

        return salary + allowances;
    }

    @Override
    public double calculateTotalAllowances(Employee employee) {

        Objects.requireNonNull(employee, "Employee cannot be null.");

        return employee.calculateTotalAllowances();
    }

    private double calculateBaseSalary(
            Employee employee,
            Duration totalHours) {

        if (totalHours.isNegative()) {
            throw new IllegalArgumentException(
                    "Total working hours cannot be negative."
            );
        }

        double hours = totalHours.toNanos()
                / 3_600_000_000_000.0;

        return hours * employee.getHourlyRate();
    }
}
