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
import domain.employee.Employee;
import repository.EmployeeRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CSVEmployeeRepository implements EmployeeRepository {

    private static final String HEADER =
        "Employee Number,Last Name,First Name,Birth Date,Address,"
        + "Phone Number,SSS Number,PhilHealth Number,TIN,"
        + "Pag-IBIG Number,Status,Position,Immediate Supervision,"
        + "Basic Salary,Rice Subsidy,Phone Allowance,"
        + "Clothing Allowance,Gross Rate,Hourly Rate";

    private final Path filePath;

    public CSVEmployeeRepository(ApplicationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException(
                "Application configuration cannot be null."
            );
        }

        this.filePath = config.getEmployeeFilePath();
    }

    @Override
    public List<Employee> findAll() throws IOException {

        List<Employee> employees = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return employees;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String header = reader.readLine();

            if (header == null) {
                return employees;
            }

            validateHeader(header);

            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    employees.add(toEmployee(parseCSV(line)));
                }
            }
        }

        return employees;
    }

    @Override
    public Optional<Employee> findByEmployeeNumber(
            String employeeNumber) throws IOException {

        validateText(employeeNumber, "Employee number");

        return findAll().stream()
            .filter(employee -> employee.getEmployeeNumber()
                .equals(employeeNumber.trim()))
            .findFirst();
    }

    @Override
    public boolean existsByEmployeeNumber(
            String employeeNumber) throws IOException {

        return findByEmployeeNumber(employeeNumber).isPresent();
    }

    @Override
    public void save(Employee employee) throws IOException {

        validateEmployee(employee);

        if (existsByEmployeeNumber(employee.getEmployeeNumber())) {
            throw new IllegalArgumentException(
                "Employee number already exists."
            );
        }

        List<String> rows = readRows();

        rows.add(toCSV(employee));

        writeRows(rows);
    }

    @Override
    public void update(Employee employee) throws IOException {

        validateEmployee(employee);

        List<String> rows = readRows();
        boolean found = false;

        for (int i = 0; i < rows.size(); i++) {

            List<String> fields = parseCSV(rows.get(i));

            if (fields.get(0).equals(employee.getEmployeeNumber())) {
                rows.set(i, toCSV(employee));
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                "Employee not found: " + employee.getEmployeeNumber()
            );
        }

        writeRows(rows);
    }

    @Override
    public boolean deleteByEmployeeNumber(
            String employeeNumber) throws IOException {

        validateText(employeeNumber, "Employee number");

        List<String> rows = readRows();

        boolean removed = rows.removeIf(row ->
            parseCSV(row).get(0).equals(employeeNumber.trim())
        );

        if (removed) {
            writeRows(rows);
        }

        return removed;
    }

    private List<String> readRows() throws IOException {

        List<String> rows = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return rows;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String header = reader.readLine();

            if (header == null) {
                return rows;
            }

            validateHeader(header);

            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    rows.add(line);
                }
            }
        }

        return rows;
    }

    private void writeRows(List<String> rows) throws IOException {

        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath, StandardCharsets.UTF_8)) {

            writer.write(HEADER);
            writer.newLine();

            for (String row : rows) {
                writer.write(row);
                writer.newLine();
            }
        }
    }

    private Employee toEmployee(List<String> fields) {

        if (fields.size() != 19) {
            throw new IllegalArgumentException(
                "Invalid employee CSV row. Expected 19 columns, found "
                + fields.size()
            );
        }

        return new Employee(
            fields.get(0),
            fields.get(1),
            fields.get(2),
            fields.get(3),
            fields.get(4),
            fields.get(5),
            fields.get(6),
            fields.get(7),
            fields.get(8),
            fields.get(9),
            fields.get(10),
            fields.get(11),
            fields.get(12),
            parseAmount(fields.get(13)),
            parseAmount(fields.get(14)),
            parseAmount(fields.get(15)),
            parseAmount(fields.get(16)),
            parseAmount(fields.get(17)),
            parseAmount(fields.get(18))
        );
    }

    private String toCSV(Employee employee) {

        List<String> fields = List.of(
            employee.getEmployeeNumber(),
            employee.getLastName(),
            employee.getFirstName(),
            employee.getBirthDate(),
            employee.getAddress(),
            employee.getPhoneNumber(),
            employee.getSssNumber(),
            employee.getPhilHealthNumber(),
            employee.getTin(),
            employee.getPagIbigNumber(),
            employee.getStatus(),
            employee.getPosition(),
            employee.getImmediateSupervision(),
            String.valueOf(employee.getBasicSalary()),
            String.valueOf(employee.getRiceSubsidy()),
            String.valueOf(employee.getPhoneAllowance()),
            String.valueOf(employee.getClothingAllowance()),
            String.valueOf(employee.getGrossRate()),
            String.valueOf(employee.getHourlyRate())
        );

        return fields.stream()
            .map(this::escapeCSV)
            .reduce((a, b) -> a + "," + b)
            .orElse("");
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

        if (value == null) {
            return "";
        }

        if (value.contains(",")
                || value.contains("\"")
                || value.contains("\n")
                || value.contains("\r")) {

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    private double parseAmount(String value) {

        try {
            double amount = Double.parseDouble(value.trim());

            if (!Double.isFinite(amount) || amount < 0) {
                throw new IllegalArgumentException(
                    "Invalid monetary value: " + value
                );
            }

            return amount;

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                "Invalid numeric value in employee CSV: " + value,
                exception
            );
        }
    }

    private void validateHeader(String header) throws IOException {

        String normalized = header.replace("\uFEFF", "").trim();

        if (!HEADER.equals(normalized)) {
            throw new IOException(
                "Employee CSV header does not match the expected format."
            );
        }
    }

    private void validateEmployee(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException(
                "Employee cannot be null."
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

