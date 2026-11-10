/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculation;

/**
 *
 * @author alfie
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SSSContributionCalculatorTest {

    private SSSContributionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SSSContributionCalculator();
    }

    @Test
    void shouldCalculateMinimumMonthlySalaryCredit() {
        BigDecimal result = calculator.calculateMonthlySalaryCredit(
                new BigDecimal("5000")
        );

        assertEquals(new BigDecimal("5000.00"), result);
    }

    @Test
    void shouldCalculateFirstSalaryCreditBracket() {
        BigDecimal result = calculator.calculateMonthlySalaryCredit(
                new BigDecimal("5250")
        );

        assertEquals(new BigDecimal("5500.00"), result);
    }

    @Test
    void shouldCalculateNextSalaryCreditBracket() {
        BigDecimal result = calculator.calculateMonthlySalaryCredit(
                new BigDecimal("5750")
        );

        assertEquals(new BigDecimal("6000.00"), result);
    }

    @Test
    void shouldEnforceMaximumMonthlySalaryCredit() {
        BigDecimal result = calculator.calculateMonthlySalaryCredit(
                new BigDecimal("50000")
        );

        assertEquals(new BigDecimal("35000.00"), result);
    }

    @Test
    void shouldCalculateEmployeeContribution() {
        BigDecimal result = calculator.calculateEmployeeShare(
                new BigDecimal("10000")
        );

        assertEquals(new BigDecimal("500.00"), result);
    }

    @Test
    void shouldCalculateEmployerContribution() {
        BigDecimal result = calculator.calculateEmployerShare(
                new BigDecimal("10000")
        );

        assertEquals(new BigDecimal("1000.00"), result);
    }

    @Test
    void shouldCalculateEmployerECForLowerSalaryCredit() {
        BigDecimal result = calculator.calculateEmployerEC(
                new BigDecimal("10000")
        );

        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void shouldCalculateEmployerECForHigherSalaryCredit() {
        BigDecimal result = calculator.calculateEmployerEC(
                new BigDecimal("15000")
        );

        assertEquals(new BigDecimal("30.00"), result);
    }

    @Test
    void shouldRejectNegativeCompensation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateMonthlySalaryCredit(
                        new BigDecimal("-1000")
                )
        );
    }

    @Test
    void shouldRejectNullCompensation() {
        assertThrows(
                NullPointerException.class,
                () -> calculator.calculateMonthlySalaryCredit(null)
        );
    }
}
