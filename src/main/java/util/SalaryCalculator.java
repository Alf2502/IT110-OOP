/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author alfie
 */

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import model.AttendanceRecord;
import model.Employee;

public class SalaryCalculator {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("H:mm");

    private final AttendanceFileHandler handler;

    public SalaryCalculator(AttendanceFileHandler handler) {
        this.handler = Objects.requireNonNull(
                handler, "Attendance handler cannot be null");
    }

    public double calculateMonthlyHours(
            String employeeNumber,
            String year,
            String monthTwoDigit) {

        double totalHours = 0.0;

        List<AttendanceRecord> records =
                handler.getRecordsForEmployee(
                        employeeNumber, year, monthTwoDigit);

        for (AttendanceRecord record : records) {

            try {
                if (record.getLogIn().isBlank()
                        || record.getLogOut().isBlank()) {
                    continue;
                }

                LocalTime logIn = LocalTime.parse(
                        record.getLogIn(), TIME_FORMATTER);

                LocalTime logOut = LocalTime.parse(
                        record.getLogOut(), TIME_FORMATTER);

                // Skip invalid or overnight records.
                if (!logOut.isAfter(logIn)) {
                    continue;
                }

                Duration duration =
                        Duration.between(logIn, logOut);

                totalHours += duration.toMinutes() / 60.0;

            } catch (DateTimeParseException e) {
                System.err.println(
                        "Invalid attendance time for employee "
                        + record.getEmployeeNumber()
                        + " on " + record.getDate()
                        + ": " + e.getMessage());
            }
        }

        return totalHours;
    }

    public double calculateSalary(
            Employee employee, double totalHours) {

        Objects.requireNonNull(
                employee, "Employee cannot be null");

        if (!Double.isFinite(totalHours) || totalHours < 0) {
            throw new IllegalArgumentException(
                    "Total hours must be non-negative.");
        }

        return totalHours * employee.getMonthlyRate();
    }

    public double calculateTotalWithAllowances(
            Employee employee) {

        Objects.requireNonNull(
                employee, "Employee cannot be null");

        return employee.getBasicSalary()
                + employee.getRiceSubsidy()
                + employee.getPhoneAllowance()
                + employee.getClothingAllowance();
    }
}

