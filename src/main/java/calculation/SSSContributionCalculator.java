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

public final class SSSContributionCalculator {

    private static final BigDecimal MINIMUM_MSC =
            new BigDecimal("5000.00");

    private static final BigDecimal MAXIMUM_MSC =
            new BigDecimal("35000.00");

    private static final BigDecimal FIRST_BRACKET_LIMIT =
            new BigDecimal("5250.00");

    private static final BigDecimal FIRST_BRACKET_MSC =
            new BigDecimal("5500.00");

    private static final BigDecimal BRACKET_INCREMENT =
            new BigDecimal("500.00");

    private static final BigDecimal EMPLOYEE_RATE =
            new BigDecimal("0.05");

    private static final BigDecimal EMPLOYER_RATE =
            new BigDecimal("0.10");

    private static final BigDecimal EC_LOW =
            new BigDecimal("10.00");

    private static final BigDecimal EC_HIGH =
            new BigDecimal("30.00");

    public BigDecimal calculateMonthlySalaryCredit(
            BigDecimal monthlyCompensation) {

        Objects.requireNonNull(
                monthlyCompensation,
                "Monthly compensation cannot be null.");

        if (monthlyCompensation.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Monthly compensation cannot be negative.");
        }

        if (monthlyCompensation.compareTo(FIRST_BRACKET_LIMIT) < 0) {
            return MINIMUM_MSC;
        }

        BigDecimal increments = monthlyCompensation
                .subtract(FIRST_BRACKET_LIMIT)
                .divide(BRACKET_INCREMENT, 0, RoundingMode.FLOOR);

        BigDecimal msc = FIRST_BRACKET_MSC.add(
                increments.multiply(BRACKET_INCREMENT));

        return msc.min(MAXIMUM_MSC);
    }

    public BigDecimal calculateEmployeeShare(
            BigDecimal monthlyCompensation) {

        BigDecimal msc =
                calculateMonthlySalaryCredit(monthlyCompensation);

        return msc.multiply(EMPLOYEE_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateEmployerShare(
            BigDecimal monthlyCompensation) {

        BigDecimal msc =
                calculateMonthlySalaryCredit(monthlyCompensation);

        return msc.multiply(EMPLOYER_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateEmployerEC(
            BigDecimal monthlyCompensation) {

        BigDecimal msc =
                calculateMonthlySalaryCredit(monthlyCompensation);

        return msc.compareTo(new BigDecimal("14500.00")) <= 0
                ? EC_LOW
                : EC_HIGH;
    }
}
