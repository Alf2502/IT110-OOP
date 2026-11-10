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

public final class PagIbigContributionCalculator {

    private static final BigDecimal COMPENSATION_THRESHOLD =
            new BigDecimal("1500.00");

    private static final BigDecimal MAXIMUM_FUND_SALARY =
            new BigDecimal("10000.00");

    private static final BigDecimal LOW_EMPLOYEE_RATE =
            new BigDecimal("0.01");

    private static final BigDecimal STANDARD_EMPLOYEE_RATE =
            new BigDecimal("0.02");

    private static final BigDecimal EMPLOYER_RATE =
            new BigDecimal("0.02");

    public BigDecimal calculateEmployeeShare(
            BigDecimal monthlyCompensation) {

        validateCompensation(monthlyCompensation);

        BigDecimal contributionBasis =
                monthlyCompensation.min(MAXIMUM_FUND_SALARY);

        BigDecimal rate = monthlyCompensation.compareTo(
                COMPENSATION_THRESHOLD) <= 0
                ? LOW_EMPLOYEE_RATE
                : STANDARD_EMPLOYEE_RATE;

        return contributionBasis.multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateEmployerShare(
            BigDecimal monthlyCompensation) {

        validateCompensation(monthlyCompensation);

        BigDecimal contributionBasis =
                monthlyCompensation.min(MAXIMUM_FUND_SALARY);

        return contributionBasis.multiply(EMPLOYER_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void validateCompensation(BigDecimal monthlyCompensation) {

        Objects.requireNonNull(
                monthlyCompensation,
                "Monthly compensation cannot be null.");

        if (monthlyCompensation.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Monthly compensation cannot be negative.");
        }
    }
}

