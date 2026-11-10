/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author alfie
 */

import domain.employee.Employee;
import repository.EmployeeRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = Objects.requireNonNull(
                employeeRepository,
                "Employee repository cannot be null."
        );
    }

    // Retrieve all employees
    public List<Employee> getAllEmployees() throws IOException {
        return List.copyOf(employeeRepository.findAll());
    }

    // Find employee by employee number
    public Optional<Employee> getEmployeeByNumber(
            String employeeNumber) throws IOException {

        validateEmployeeNumber(employeeNumber);

        return employeeRepository.findByEmployeeNumber(
                employeeNumber.trim()
        );
    }

    // Register a new employee
    public void addEmployee(Employee employee) throws IOException {

        validateEmployee(employee);

        if (employeeRepository.existsByEmployeeNumber(
                employee.getEmployeeNumber())) {

            throw new IllegalArgumentException(
                    "Employee number already exists."
            );
        }

        employeeRepository.save(employee);
    }

    // Update existing employee
    public void updateEmployee(Employee employee) throws IOException {

        validateEmployee(employee);

        if (!employeeRepository.existsByEmployeeNumber(
                employee.getEmployeeNumber())) {

            throw new IllegalArgumentException(
                    "Employee does not exist."
            );
        }

        employeeRepository.update(employee);
    }

    // Delete employee
    public boolean deleteEmployee(
            String employeeNumber) throws IOException {

        validateEmployeeNumber(employeeNumber);

        return employeeRepository.deleteByEmployeeNumber(
                employeeNumber.trim()
        );
    }

    // Search employees by name
    public List<Employee> searchEmployees(String keyword)
            throws IOException {

        if (keyword == null || keyword.isBlank()) {
            return getAllEmployees();
        }

        String searchTerm = keyword.trim().toLowerCase();

        return employeeRepository.findAll().stream()
                .filter(employee ->
                        employee.getFirstName()
                                .toLowerCase().contains(searchTerm)
                        || employee.getLastName()
                                .toLowerCase().contains(searchTerm)
                        || employee.getEmployeeNumber()
                                .toLowerCase().contains(searchTerm)
                )
                .toList();
    }

    // Retrieve active employees
    public List<Employee> getActiveEmployees() throws IOException {

        return employeeRepository.findAll().stream()
                .filter(Employee::isActive)
                .toList();
    }

    private void validateEmployee(Employee employee) {

        Objects.requireNonNull(
                employee,
                "Employee cannot be null."
        );
    }

    private void validateEmployeeNumber(String employeeNumber) {

        if (employeeNumber == null || employeeNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee number cannot be empty."
            );
        }
    }
}

