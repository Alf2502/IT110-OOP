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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Objects;

public class StandardPayrollRules implements PayrollRules {

    @Override
    public PayrollEarnings calculateEarnings(
            Employee employee,
            Duration totalHours,
            PayrollPeriod payrollPeriod) {

        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(totalHours, "Total hours cannot be null.");
        Objects.requireNonNull(payrollPeriod, "Payroll period cannot be null.");

        if (totalHours.isNegative()) {
            throw new IllegalArgumentException(
                    "Total hours cannot be negative.");
        }

        BigDecimal hours = BigDecimal.valueOf(totalHours.toMillis())
                .divide(BigDecimal.valueOf(3_600_000), 4, RoundingMode.HALF_UP);

        BigDecimal hourlyRate = BigDecimal.valueOf(employee.getHourlyRate());

        BigDecimal basicPay = hours.multiply(hourlyRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal riceSubsidy = BigDecimal.ZERO;
        BigDecimal phoneAllowance = BigDecimal.ZERO;
        BigDecimal clothingAllowance = BigDecimal.ZERO;

        if (isEligibleForAllowances(employee)) {
            riceSubsidy = BigDecimal.valueOf(employee.getRiceSubsidy());
            phoneAllowance = BigDecimal.valueOf(employee.getPhoneAllowance());
            clothingAllowance = BigDecimal.valueOf(employee.getClothingAllowance());
        }

        BigDecimal grossCompensation = basicPay
                .add(riceSubsidy)
                .add(phoneAllowance)
                .add(clothingAllowance);

        // Temporary classification pending verified tax rules.
        return new PayrollEarnings(
                basicPay,
                riceSubsidy,
                phoneAllowance,
                clothingAllowance,
                grossCompensation,
                BigDecimal.ZERO
        );
    }

    @Override
    public boolean isEligibleForAllowances(Employee employee) {
        Objects.requireNonNull(employee, "Employee cannot be null.");

        return employee.isActive();
    }
}
