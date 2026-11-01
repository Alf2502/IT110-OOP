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
 *  Encapsulation applied
 *  The file path and CSV parsing implementation are private.
 *  The handler uses the shared "FilePathManager" and creates "Employee" objects
 *  Code resuse writeallEmployees() centralized the file-rewriting logic used by updating, deleting, and saving all employees.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.Employee;

public class EmployeeFileHandler {

    private static final String HEADER =
            "Employee Number,Last Name,First Name,Birth Date,Address,"
            + "Phone Number,SSS Number,PhilHealth Number,TIN,"
            + "Pag-IBIG Number,Status,Position,Immediate Supervision,"
            + "Basic Salary,Rice Subsidy,Phone Allowance,"
            + "Clothing Allowance,Gross Rate,Hourly Rate";

    private final Path employeeFilePath;

    public EmployeeFileHandler() {
        this(FilePathManager.getInstance().getEmployeeFilePath());
    }

    // Constructor injection supports testing with a different CSV file.
    public EmployeeFileHandler(String employeeFilePath) {
        if (employeeFilePath == null || employeeFilePath.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee file path cannot be empty.");
        }

        this.employeeFilePath = Path.of(employeeFilePath)
                .toAbsolutePath()
                .normalize();
    }

    // READ
    public List<Employee> readEmployees() {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                employeeFilePath, StandardCharsets.UTF_8)) {

            reader.readLine(); // Skip CSV header.

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    List<String> fields = parseCsvLine(line);

                    if (fields.size() != 19) {
                        System.err.println(
                                "Skipping employee row " + lineNumber
                                + ": expected 19 fields.");
                        continue;
                    }

                    Employee employee = new Employee(
                            fields.get(0), fields.get(1),
                            fields.get(2), fields.get(3),
                            fields.get(4), fields.get(5),
                            fields.get(6), fields.get(7),
                            fields.get(8), fields.get(9),
                            fields.get(10), fields.get(11),
                            fields.get(12), fields.get(13),
                            fields.get(14), fields.get(15),
                            fields.get(16), fields.get(17),
                            fields.get(18)
                    );

                    employees.add(employee);

                } catch (IllegalArgumentException e) {
                    System.err.println(
                            "Invalid employee row " + lineNumber
                            + ": " + e.getMessage());
                }
            }

            return employees;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read employee file: "
                    + employeeFilePath, e);
        }
    }

    // CREATE
    public boolean saveEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null");

        try {
            boolean needsHeader =
                    !Files.exists(employeeFilePath)
                    || Files.size(employeeFilePath) == 0;

            try (BufferedWriter writer = Files.newBufferedWriter(
                    employeeFilePath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                if (needsHeader) {
                    writer.write(HEADER);
                    writer.newLine();
                }

                writer.write(employee.toCSV());
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            System.err.println(
                    "Unable to save employee: " + e.getMessage());
            return false;
        }
    }

    // UPDATE
    public boolean updateEmployee(Employee updatedEmployee) {
        Objects.requireNonNull(
                updatedEmployee, "Employee cannot be null");

        try {
            List<Employee> employees = readEmployees();
            boolean found = false;

            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmployeeNumber()
                        .equals(updatedEmployee.getEmployeeNumber())) {

                    employees.set(i, updatedEmployee);
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }

            return writeAllEmployees(employees);

        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteEmployeeByNumber(String employeeNumber) {
        if (employeeNumber == null || employeeNumber.isBlank()) {
            return false;
        }

        try {
            List<Employee> employees = readEmployees();

            boolean removed = employees.removeIf(employee ->
                    employeeNumber.equals(
                            employee.getEmployeeNumber()));

            if (!removed) {
                return false;
            }

            return writeAllEmployees(employees);

        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    // SAVE ALL
    public boolean saveAllEmployees(List<Employee> employees) {
        Objects.requireNonNull(employees, "Employee list cannot be null");

        return writeAllEmployees(employees);
    }

    // Shared method for update and save-all operations.
    private boolean writeAllEmployees(List<Employee> employees) {
        Path parent = employeeFilePath.getParent();
        Path tempFile = null;

        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }

            tempFile = Files.createTempFile(
                    parent, "employees-", ".tmp");

            try (BufferedWriter writer = Files.newBufferedWriter(
                    tempFile, StandardCharsets.UTF_8)) {

                writer.write(HEADER);
                writer.newLine();

                for (Employee employee : employees) {
                    writer.write(employee.toCSV());
                    writer.newLine();
                }
            }

            try {
                Files.move(
                        tempFile,
                        employeeFilePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        tempFile,
                        employeeFilePath,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return true;

        } catch (IOException e) {
            System.err.println(
                    "Unable to write employee file: "
                    + e.getMessage());
            return false;

        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println(
                            "Unable to remove temporary file: "
                            + e.getMessage());
                }
            }
        }
    }

    // Handles quoted CSV fields and escaped quotation marks.
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

