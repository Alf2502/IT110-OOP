/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author alfie
 */

import calculation.EmployerContributionCalculator;
import calculation.PayrollCalculator;
import calculation.PayrollRules;
import domain.employee.Employee;
import domain.payroll.EmployerContributions;
import domain.payroll.PayrollDeductions;
import domain.payroll.PayrollEarnings;
import domain.payroll.PayrollPeriod;
import domain.payroll.PayrollResult;
import calculation.StatutoryDeductionCalculator;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public class PayrollService {

    private final StatutoryDeductionCalculator statutoryDeductionCalculator;
    private final EmployerContributionCalculator employerContributionCalculator;
    private final PayrollRules payrollRules;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    private final PayrollCalculator payrollCalculator;

    public PayrollService(
            EmployeeService employeeService,
            AttendanceService attendanceService,
            PayrollCalculator payrollCalculator,
            PayrollRules payrollRules,
            StatutoryDeductionCalculator statutoryDeductionCalculator,
            EmployerContributionCalculator employerContributionCalculator) {

        this.employeeService = Objects.requireNonNull(employeeService);
        this.attendanceService = Objects.requireNonNull(attendanceService);
        this.payrollCalculator = Objects.requireNonNull(payrollCalculator);
        this.payrollRules = Objects.requireNonNull(payrollRules);

        this.statutoryDeductionCalculator =
            Objects.requireNonNull(statutoryDeductionCalculator);

        this.employerContributionCalculator =
            Objects.requireNonNull(employerContributionCalculator);
    }

    // Generate payroll using employee number and payroll period
    public PayrollResult generatePayroll(
            String employeeNumber,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate payrollDate) throws IOException {

        validatePayrollPeriod(startDate, endDate, payrollDate);

        Employee employee = employeeService
                .getEmployeeByNumber(employeeNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Employee not found: " + employeeNumber
                ));

        Duration totalHours =
                attendanceService.calculateTotalHours(
                        employeeNumber,
                        startDate,
                        endDate
                );

        return payrollCalculator.calculatePayroll(
                employee,
                totalHours,
                payrollDate
        );
    }

    // Generate payroll using an Employee object
    public PayrollResult generatePayroll(
            Employee employee,
            Duration totalHours,
            LocalDate payrollDate) {

        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(totalHours, "Total hours cannot be null.");
        Objects.requireNonNull(payrollDate, "Payroll date cannot be null.");

        PayrollPeriod period = new PayrollPeriod(
            YearMonth.from(payrollDate)
            );

        PayrollEarnings earnings = payrollRules.calculateEarnings(
                employee,
                totalHours,
                period
            );

        PayrollDeductions deductions =
        statutoryDeductionCalculator.calculateEmployeeDeductions(
                employee, earnings, period
            );

        EmployerContributions employerContributions =
        employerContributionCalculator.calculateEmployerContributions(
                employee, earnings, period
            );

        return new PayrollResult(
            employee.getEmployeeNumber(),
            period,
            payrollDate,
            earnings,
            deductions,
            employerContributions
        );
    }

    // Calculate gross pay
    public double calculateGrossPay(
            Employee employee,
            Duration totalHours) {

        return payrollCalculator.calculateGrossPay(
                employee,
                totalHours
        );
    }

    // Calculate employee allowances
    public double calculateAllowances(Employee employee) {

        return payrollCalculator.calculateTotalAllowances(
                employee
        );
    }

    private void validatePayrollPeriod(
            LocalDate startDate,
            LocalDate endDate,
            LocalDate payrollDate) {

        Objects.requireNonNull(startDate, "Start date cannot be null.");
        Objects.requireNonNull(endDate, "End date cannot be null.");
        Objects.requireNonNull(payrollDate, "Payroll date cannot be null.");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "Payroll end date cannot precede start date."
            );
        }
    }
}
