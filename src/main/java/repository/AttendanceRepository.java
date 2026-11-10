/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author alfie
 */

import domain.attendance.AttendanceRecord;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {

    // Retrieve all attendance records
    List<AttendanceRecord> findAll() throws IOException;

    // Find attendance records belonging to an employee
    List<AttendanceRecord> findByEmployeeNumber(
            String employeeNumber
    ) throws IOException;

    // Retrieve attendance records for a specific date
    List<AttendanceRecord> findByDate(
            LocalDate date
    ) throws IOException;

    // Retrieve attendance records within a date range
    List<AttendanceRecord> findByDateRange(
            LocalDate startDate,
            LocalDate endDate
    ) throws IOException;

    // Save a new attendance record
    void save(AttendanceRecord record) throws IOException;

    // Save multiple attendance records
    void saveAll(
            List<AttendanceRecord> records
    ) throws IOException;
}
