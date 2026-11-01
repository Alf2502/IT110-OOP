/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author alfie
 */

/*
 *  Encapsulation: csvFilePath remains private final; external classes cannot directly modify the handler's path.
 *  Separation of responsibilities: This class loads and filters attendance records; SalaryCalculator calculates hours and salary.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import model.AttendanceRecord;

public class AttendanceFileHandler {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("M/d/uuuu");

    private final String csvFilePath;

    public AttendanceFileHandler() {
        this(FilePathManager.getInstance().getAttendanceFilePath());
    }

    // Constructor injection: allows a custom file path for testing.
    public AttendanceFileHandler(String csvFilePath) {
        if (csvFilePath == null || csvFilePath.isBlank()) {
            throw new IllegalArgumentException(
                    "Attendance file path cannot be empty.");
        }

        this.csvFilePath = csvFilePath;
    }

    public List<AttendanceRecord> readAllRecords() {
        List<AttendanceRecord> records = new ArrayList<>();

        Path path = Path.of(csvFilePath);

        try (BufferedReader reader = Files.newBufferedReader(
                path, StandardCharsets.UTF_8)) {

            // Skip the CSV header.
            reader.readLine();

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    List<String> fields = parseCsvLine(line);

                    if (fields.size() < 6) {
                        System.err.println(
                                "Skipping malformed attendance row "
                                + lineNumber);
                        continue;
                    }

                    AttendanceRecord record = new AttendanceRecord(
                            fields.get(0).trim(),
                            fields.get(1).trim(),
                            fields.get(2).trim(),
                            fields.get(3).trim(),
                            fields.get(4).trim(),
                            fields.get(5).trim()
                    );

                    records.add(record);

                } catch (IllegalArgumentException e) {
                    System.err.println(
                            "Skipping invalid attendance row "
                            + lineNumber + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read attendance file: " + csvFilePath,
                    e);
        }

        return records;
    }

    public List<AttendanceRecord> getRecordsForEmployee(
            String employeeNumber,
            String year,
            String monthTwoDigit) {

        Objects.requireNonNull(
                employeeNumber, "Employee number cannot be null");

        YearMonth targetMonth;

        try {
            targetMonth = YearMonth.of(
                    Integer.parseInt(year),
                    Integer.parseInt(monthTwoDigit));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(
                    "Invalid year or month.", e);
        }

        return readAllRecords().stream()
                .filter(record ->
                        employeeNumber.equals(
                                record.getEmployeeNumber()))
                .filter(record -> {
                    try {
                        LocalDate date = LocalDate.parse(
                                record.getDate(), DATE_FORMATTER);

                        return YearMonth.from(date).equals(targetMonth);

                    } catch (DateTimeParseException e) {
                        System.err.println(
                                "Invalid attendance date: "
                                + record.getDate());
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // Parses CSV fields containing quoted commas and escaped quotes.
    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);

            if (current == '"') {
                if (insideQuotes
                        && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    field.append('"');
                    i++;

                } else {
                    insideQuotes = !insideQuotes;
                }

            } else if (current == ',' && !insideQuotes) {
                fields.add(field.toString());
                field.setLength(0);

            } else {
                field.append(current);
            }
        }

        if (insideQuotes) {
            throw new IllegalArgumentException(
                    "Unclosed quotation mark in CSV row.");
        }

        fields.add(field.toString());
        return fields;
    }
}

