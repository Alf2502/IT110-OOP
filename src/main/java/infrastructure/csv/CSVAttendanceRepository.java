/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure.csv;

/**
 *
 * @author alfie
 */

import config.ApplicationConfig;
import domain.attendance.AttendanceRecord;
import repository.AttendanceRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CSVAttendanceRepository implements AttendanceRepository {

    private static final String HEADER =
            "Employee #,Last Name,First Name,Date,Log In,Log Out";

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("H:mm");

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("M/d/yyyy");

    private final Path filePath;

    public CSVAttendanceRepository(ApplicationConfig config) {
        Objects.requireNonNull(config, "Configuration cannot be null.");
        this.filePath = config.getAttendanceFilePath();
    }

    @Override
    public List<AttendanceRecord> findAll() throws IOException {

        List<AttendanceRecord> records = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return records;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String header = reader.readLine();

            if (header == null) {
                return records;
            }

            validateHeader(header);

            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(toAttendanceRecord(parseCSV(line)));
                }
            }
        }

        return records;
    }

    @Override
    public List<AttendanceRecord> findByEmployeeNumber(
            String employeeNumber) throws IOException {

        validateText(employeeNumber, "Employee number");

        return findAll().stream()
                .filter(record -> record.getEmployeeNumber()
                        .equals(employeeNumber.trim()))
                .toList();
    }

    @Override
    public List<AttendanceRecord> findByDate(
            LocalDate date) throws IOException {

        Objects.requireNonNull(date, "Date cannot be null.");

        return findAll().stream()
                .filter(record -> record.getDate().equals(date))
                .toList();
    }

    @Override
    public List<AttendanceRecord> findByDateRange(
            LocalDate startDate,
            LocalDate endDate) throws IOException {

        Objects.requireNonNull(startDate, "Start date cannot be null.");
        Objects.requireNonNull(endDate, "End date cannot be null.");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date."
            );
        }

        return findAll().stream()
                .filter(record ->
                        !record.getDate().isBefore(startDate)
                        && !record.getDate().isAfter(endDate))
                .toList();
    }

    @Override
    public void save(AttendanceRecord record) throws IOException {

        Objects.requireNonNull(record, "Attendance record cannot be null.");

        List<AttendanceRecord> records = findAll();

        records.add(record);

        writeAll(records);
    }

    @Override
    public void saveAll(
            List<AttendanceRecord> records) throws IOException {

        Objects.requireNonNull(records, "Records cannot be null.");

        if (records.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Attendance list cannot contain null records."
            );
        }

        List<AttendanceRecord> existingRecords = findAll();

        existingRecords.addAll(records);

        writeAll(existingRecords);
    }

    private AttendanceRecord toAttendanceRecord(
            List<String> fields) {

        if (fields.size() != 6) {
            throw new IllegalArgumentException(
                    "Invalid attendance CSV row. Expected 6 columns, found "
                    + fields.size()
            );
        }

        return new AttendanceRecord(
                fields.get(0),
                fields.get(1),
                fields.get(2),
                LocalDate.parse(fields.get(3), DATE_FORMAT),
                LocalTime.parse(fields.get(4), TIME_FORMAT),
                LocalTime.parse(fields.get(5), TIME_FORMAT)
        );
    }

    private void writeAll(
            List<AttendanceRecord> records) throws IOException {

        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath, StandardCharsets.UTF_8)) {

            writer.write(HEADER);
            writer.newLine();

            for (AttendanceRecord record : records) {

                List<String> fields = List.of(
                        record.getEmployeeNumber(),
                        record.getLastName(),
                        record.getFirstName(),
                        record.getDate().format(DATE_FORMAT),
                        record.getLogIn().format(TIME_FORMAT),
                        record.getLogOut().format(TIME_FORMAT)
                );

                writer.write(fields.stream()
                        .map(this::escapeCSV)
                        .reduce((a, b) -> a + "," + b)
                        .orElse(""));

                writer.newLine();
            }
        }
    }

    private List<String> parseCSV(String line) {

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
                    "Invalid CSV row: unclosed quotation mark."
            );
        }

        fields.add(field.toString());

        return fields;
    }

    private String escapeCSV(String value) {

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\n")
                || value.contains("\r")) {

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    private void validateHeader(String header) throws IOException {

        String normalized = header.replace("\uFEFF", "").trim();

        if (!HEADER.equals(normalized)) {
            throw new IOException(
                    "Attendance CSV header does not match expected format."
            );
        }
    }

    private void validateText(String value, String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty."
            );
        }
    }
}

