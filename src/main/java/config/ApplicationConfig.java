/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author alfie
 */

import java.nio.file.Path;

public final class ApplicationConfig {

    private static final String DATA_DIRECTORY =
            "data";

    private static final String EMPLOYEE_FILE =
            "MotorPH Employee Details.csv";

    private static final String ATTENDANCE_FILE =
            "MotorPH Attendance Record.csv";

    private static final String ADMIN_FILE =
            "admin.csv";

    private final Path dataDirectory;

    public ApplicationConfig() {
        this.dataDirectory = Path.of(DATA_DIRECTORY);
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public Path getEmployeeFilePath() {
        return dataDirectory.resolve(EMPLOYEE_FILE);
    }

    public Path getAttendanceFilePath() {
        return dataDirectory.resolve(ATTENDANCE_FILE);
    }

    public Path getAdminFilePath() {
        return dataDirectory.resolve(ADMIN_FILE);
    }
}

