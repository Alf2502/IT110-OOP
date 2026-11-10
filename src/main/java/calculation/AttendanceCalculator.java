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

public interface AttendanceCalculator {

    Duration calculateDailyHours(AttendanceRecord record);

    Duration calculateTotalHours(List<AttendanceRecord> records);

    boolean isValidAttendance(AttendanceRecord record);
}
