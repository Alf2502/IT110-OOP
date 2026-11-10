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
import domain.payroll.PayrollDeductions;

public interface StatutoryDeductionCalculator {

    PayrollDeductions calculateEmployeeDeductions(
            Employee employee,
            PayrollEarnings earnings,
            PayrollPeriod payrollPeriod
    );

}
