/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculation;

/**
 *
 * @author alfie
 */

import domain.attendance.AttendanceRecord;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class AttendanceHoursCalculator implements AttendanceCalculator {

    @Override
    public Duration calculateDailyHours(AttendanceRecord record) {

        Objects.requireNonNull(record, "Attendance record cannot be null.");

        if (!isValidAttendance(record)) {
            return Duration.ZERO;
        }

        return Duration.between(record.getLogIn(), record.getLogOut());
    }

    @Override
    public Duration calculateTotalHours(
            List<AttendanceRecord> records) {

        Objects.requireNonNull(records, "Attendance records cannot be null.");

        return records.stream()
                .filter(this::isValidAttendance)
                .map(this::calculateDailyHours)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public boolean isValidAttendance(AttendanceRecord record) {

        if (record == null
                || record.getDate() == null
                || record.getLogIn() == null
                || record.getLogOut() == null) {
            return false;
        }

        return record.getLogOut().isAfter(record.getLogIn());
    }
}

