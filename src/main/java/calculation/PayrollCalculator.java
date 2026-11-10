/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculation;

/**
 *
 * @author alfie
 */

import domain.employee.Employee;
import domain.payroll.PayrollResult;
import java.time.LocalDate;
import java.time.Duration;

public interface PayrollCalculator {

    PayrollResult calculatePayroll(
            Employee employee,
            Duration totalHours,
            LocalDate payrollDate
    );

    double calculateGrossPay(
            Employee employee,
            Duration totalHours
    );

    double calculateTotalAllowances(Employee employee);
}
