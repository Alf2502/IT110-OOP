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

public final class WithholdingTaxCalculator {

    private static final BigDecimal ZERO =
            new BigDecimal("0.00");

    public BigDecimal calculateMonthlyTax(
            BigDecimal monthlyTaxableCompensation) {

        Objects.requireNonNull(
                monthlyTaxableCompensation,
                "Taxable compensation cannot be null.");

        if (monthlyTaxableCompensation.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Taxable compensation cannot be negative.");
        }

        BigDecimal income = monthlyTaxableCompensation;

        if (income.compareTo(new BigDecimal("20833")) <= 0) {
            return ZERO;
        }

        if (income.compareTo(new BigDecimal("33333")) < 0) {
            return calculateTax(income, "20833", "0", "0.15");
        }

        if (income.compareTo(new BigDecimal("66667")) < 0) {
            return calculateTax(income, "33333", "1875", "0.20");
        }

        if (income.compareTo(new BigDecimal("166667")) < 0) {
            return calculateTax(income, "66667", "8541.80", "0.25");
        }

        if (income.compareTo(new BigDecimal("666667")) < 0) {
            return calculateTax(income, "166667", "33541.80", "0.30");
        }

        return calculateTax(income, "666667", "183541.80", "0.35");
    }

    private BigDecimal calculateTax(
            BigDecimal income,
            String threshold,
            String baseTax,
            String rate) {

        BigDecimal excess = income.subtract(new BigDecimal(threshold));

        return new BigDecimal(baseTax)
                .add(excess.multiply(new BigDecimal(rate)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
