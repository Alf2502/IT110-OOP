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
import domain.payroll.PayrollResult;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class SalaryCalculator implements PayrollCalculator {

    @Override
    public PayrollResult calculatePayroll(
            Employee employee,
            Duration totalHours,
            LocalDate payrollDate) {

        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(totalHours, "Total hours cannot be null.");
        Objects.requireNonNull(payrollDate, "Payroll date cannot be null.");

        double salary = calculateBaseSalary(employee, totalHours);
        double allowances = salary > 0
                ? calculateTotalAllowances(employee)
                : 0.0;

        double grossPay = salary + allowances;

        return new PayrollResult(
                employee.getEmployeeNumber(),
                payrollDate,
                BigDecimal.valueOf(grossPay),
                BigDecimal.valueOf(allowances),
                BigDecimal.ZERO
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

        return hours * employee.getMonthlyRate();
    }
}
