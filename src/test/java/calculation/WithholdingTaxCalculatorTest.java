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

class WithholdingTaxCalculatorTest {

    private WithholdingTaxCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new WithholdingTaxCalculator();
    }

    @Test
    void shouldReturnZeroForTaxExemptMonthlyCompensation() {
        assertEquals(
                new BigDecimal("0.00"),
                calculator.calculateMonthlyTax(
                        new BigDecimal("20000")
                )
        );
    }

    @Test
    void shouldCalculateFirstTaxBracket() {
        assertEquals(
                new BigDecimal("625.05"),
                calculator.calculateMonthlyTax(
                        new BigDecimal("25000")
                )
        );
    }

    @Test
    void shouldCalculateSecondTaxBracket() {
        assertEquals(
                new BigDecimal("3208.40"),
                calculator.calculateMonthlyTax(
                        new BigDecimal("40000")
                )
        );
    }

    @Test
    void shouldCalculateThirdTaxBracket() {
        assertEquals(
                new BigDecimal("9375.05"),
                calculator.calculateMonthlyTax(
                        new BigDecimal("70000")
                )
        );
    }

    @Test
    void shouldCalculateHighestTaxBracket() {
        assertEquals(
                new BigDecimal("43541.70"),
                calculator.calculateMonthlyTax(
                        new BigDecimal("200000")
                )
        );
    }

    @Test
    void shouldRejectNegativeTaxableCompensation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateMonthlyTax(
                        new BigDecimal("-1000")
                )
        );
    }

    @Test
    void shouldRejectNullTaxableCompensation() {
        assertThrows(
                NullPointerException.class,
                () -> calculator.calculateMonthlyTax(null)
        );
    }
}

