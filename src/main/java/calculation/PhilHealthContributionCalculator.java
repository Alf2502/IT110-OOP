/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculation;

/**
 *
 * @author alfie
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class PhilHealthContributionCalculator {

    private static final BigDecimal MINIMUM_SALARY =
            new BigDecimal("10000.00");

    private static final BigDecimal MAXIMUM_SALARY =
            new BigDecimal("100000.00");

    private static final BigDecimal EMPLOYEE_RATE =
            new BigDecimal("0.025");

    private static final BigDecimal EMPLOYER_RATE =
            new BigDecimal("0.025");

    public BigDecimal calculateMonthlyPremium(
            BigDecimal monthlyBasicSalary) {

        Objects.requireNonNull(
                monthlyBasicSalary,
                "Monthly basic salary cannot be null.");

        if (monthlyBasicSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Monthly basic salary cannot be negative.");
        }

        BigDecimal contributionBasis = monthlyBasicSalary
                .max(MINIMUM_SALARY)
                .min(MAXIMUM_SALARY);

        return contributionBasis
                .multiply(EMPLOYEE_RATE.add(EMPLOYER_RATE))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateEmployeeShare(
            BigDecimal monthlyBasicSalary) {

        return calculateMonthlyPremium(monthlyBasicSalary)
                .divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateEmployerShare(
            BigDecimal monthlyBasicSalary) {

        return calculateMonthlyPremium(monthlyBasicSalary)
                .divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
    }
}
