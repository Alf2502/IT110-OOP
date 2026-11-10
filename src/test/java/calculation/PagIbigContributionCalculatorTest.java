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

class PagIbigContributionCalculatorTest {

    private PagIbigContributionCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PagIbigContributionCalculator();
    }

    @Test
    void shouldCalculateEmployeeContributionBelowThreshold() {
        assertEquals(
                new BigDecimal("15.00"),
                calculator.calculateEmployeeShare(
                        new BigDecimal("1500")
                )
        );
    }

    @Test
    void shouldCalculateEmployeeContributionAboveThreshold() {
        assertEquals(
                new BigDecimal("30.02"),
                calculator.calculateEmployeeShare(
                        new BigDecimal("1501")
                )
        );
    }

    @Test
    void shouldCalculateEmployerContribution() {
        assertEquals(
                new BigDecimal("100.00"),
                calculator.calculateEmployerShare(
                        new BigDecimal("5000")
                )
        );
    }

    @Test
    void shouldEnforceMaximumContributionBasis() {
        assertEquals(
                new BigDecimal("200.00"),
                calculator.calculateEmployeeShare(
                        new BigDecimal("25000")
                )
        );
    }

    @Test
    void shouldCalculateMaximumEmployerContribution() {
        assertEquals(
                new BigDecimal("200.00"),
                calculator.calculateEmployerShare(
                        new BigDecimal("25000")
                )
        );
    }

    @Test
    void shouldRejectNegativeCompensation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateEmployeeShare(
                        new BigDecimal("-1000")
                )
        );
    }

    @Test
    void shouldRejectNullCompensation() {
        assertThrows(
                NullPointerException.class,
                () -> calculator.calculateEmployeeShare(null)
        );
    }
}
