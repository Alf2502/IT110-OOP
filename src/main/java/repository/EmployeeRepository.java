/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

/**
 *
 * @author alfie
 */

import domain.employee.Employee;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    // Retrieve all employees
    List<Employee> findAll() throws IOException;

    // Find an employee using their employee number
    Optional<Employee> findByEmployeeNumber(
            String employeeNumber
    ) throws IOException;

    // Check whether an employee already exists
    boolean existsByEmployeeNumber(
            String employeeNumber
    ) throws IOException;

    // Save a new employee
    void save(Employee employee) throws IOException;

    // Update an existing employee
    void update(Employee employee) throws IOException;

    // Delete an employee
    boolean deleteByEmployeeNumber(
            String employeeNumber
    ) throws IOException;
}
