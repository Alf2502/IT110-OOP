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
import domain.payroll.PayrollEarnings;
import domain.payroll.PayrollPeriod;

import java.time.Duration;

public interface PayrollRules {

    PayrollEarnings calculateEarnings(
            Employee employee,
            Duration totalHours,
            PayrollPeriod payrollPeriod
    );

    boolean isEligibleForAllowances(Employee employee);
}


