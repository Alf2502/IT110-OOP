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

class PhilHealthContributionCalculatorTest {

    private PhilHealthContributionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PhilHealthContributionCalculator();
    }

    @Test
    void shouldCalculateMinimumSalaryPremium() {
        assertEquals(
                new BigDecimal("500.00"),
                calculator.calculateMonthlyPremium(
                        new BigDecimal("8000")
                )
        );
    }

    @Test
    void shouldCalculateStandardPremium() {
        assertEquals(
                new BigDecimal("1000.00"),
                calculator.calculateMonthlyPremium(
                        new BigDecimal("20000")
                )
        );
    }

    @Test
    void shouldEnforceMaximumSalaryCeiling() {
        assertEquals(
                new BigDecimal("5000.00"),
                calculator.calculateMonthlyPremium(
                        new BigDecimal("120000")
                )
        );
    }

    @Test
    void shouldCalculateEmployeeShare() {
        assertEquals(
                new BigDecimal("500.00"),
                calculator.calculateEmployeeShare(
                        new BigDecimal("20000")
                )
        );
    }

    @Test
    void shouldCalculateEmployerShare() {
        assertEquals(
                new BigDecimal("500.00"),
                calculator.calculateEmployerShare(
                        new BigDecimal("20000")
                )
        );
    }

    @Test
    void shouldRejectNegativeSalary() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateMonthlyPremium(
                        new BigDecimal("-1000")
                )
        );
    }

    @Test
    void shouldRejectNullSalary() {
        assertThrows(
                NullPointerException.class,
                () -> calculator.calculateMonthlyPremium(null)
        );
    }
}

