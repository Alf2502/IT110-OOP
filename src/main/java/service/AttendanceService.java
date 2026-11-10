/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author alfie
 */

import calculation.AttendanceCalculator;
import domain.attendance.AttendanceRecord;
import repository.AttendanceRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceCalculator attendanceCalculator;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            AttendanceCalculator attendanceCalculator) {

        this.attendanceRepository = Objects.requireNonNull(
                attendanceRepository,
                "Attendance repository cannot be null."
        );

        this.attendanceCalculator = Objects.requireNonNull(
                attendanceCalculator,
                "Attendance calculator cannot be null."
        );
    }

    // Retrieve all attendance records
    public List<AttendanceRecord> getAllRecords()
            throws IOException {

        return List.copyOf(attendanceRepository.findAll());
    }

    // Retrieve records by employee
    public List<AttendanceRecord> getRecordsByEmployee(
            String employeeNumber) throws IOException {

        validateEmployeeNumber(employeeNumber);

        return List.copyOf(
                attendanceRepository.findByEmployeeNumber(
                        employeeNumber.trim()
                )
        );
    }

    // Retrieve records by date
    public List<AttendanceRecord> getRecordsByDate(
            LocalDate date) throws IOException {

        Objects.requireNonNull(date, "Date cannot be null.");

        return List.copyOf(
                attendanceRepository.findByDate(date)
        );
    }

    // Retrieve records within a date range
    public List<AttendanceRecord> getRecordsByDateRange(
            LocalDate startDate,
            LocalDate endDate) throws IOException {

        validateDateRange(startDate, endDate);

        return List.copyOf(
                attendanceRepository.findByDateRange(
                        startDate, endDate
                )
        );
    }

    // Calculate daily working hours
    public Duration calculateDailyHours(
            AttendanceRecord record) {

        Objects.requireNonNull(
                record,
                "Attendance record cannot be null."
        );

        return attendanceCalculator.calculateDailyHours(record);
    }

    // Calculate total working hours for an employee
    public Duration calculateTotalHours(
            String employeeNumber) throws IOException {

        List<AttendanceRecord> records =
                getRecordsByEmployee(employeeNumber);

        return attendanceCalculator.calculateTotalHours(records);
    }

    // Calculate working hours within a date range
    public Duration calculateTotalHours(
            String employeeNumber,
            LocalDate startDate,
            LocalDate endDate) throws IOException {

        validateEmployeeNumber(employeeNumber);
        validateDateRange(startDate, endDate);

        List<AttendanceRecord> records =
                attendanceRepository.findByDateRange(
                        startDate, endDate
                ).stream()
                .filter(record -> record.getEmployeeNumber()
                        .equals(employeeNumber.trim()))
                .toList();

        return attendanceCalculator.calculateTotalHours(records);
    }

    // Validate attendance
    public boolean isValidAttendance(AttendanceRecord record) {

        return attendanceCalculator.isValidAttendance(record);
    }

    // Register an attendance record
    public void addAttendanceRecord(
            AttendanceRecord record) throws IOException {

        Objects.requireNonNull(
                record,
                "Attendance record cannot be null."
        );

        if (!attendanceCalculator.isValidAttendance(record)) {
            throw new IllegalArgumentException(
                    "Invalid attendance record."
            );
        }

        attendanceRepository.save(record);
    }

    private void validateEmployeeNumber(String employeeNumber) {

        if (employeeNumber == null || employeeNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee number cannot be empty."
            );
        }
    }

    private void validateDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        Objects.requireNonNull(startDate, "Start date cannot be null.");
        Objects.requireNonNull(endDate, "End date cannot be null.");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date."
            );
        }
    }
}

