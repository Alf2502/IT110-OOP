/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.payroll;

/**
 *
 * @author alfie
 */

import java.math.BigDecimal;
import java.util.Objects;

public final class PayrollEarnings {

    private final BigDecimal basicPay;
    private final BigDecimal riceSubsidy;
    private final BigDecimal phoneAllowance;
    private final BigDecimal clothingAllowance;

    private final BigDecimal taxableCompensation;
    private final BigDecimal nonTaxableCompensation;

    public PayrollEarnings(
            BigDecimal basicPay,
            BigDecimal riceSubsidy,
            BigDecimal phoneAllowance,
            BigDecimal clothingAllowance,
            BigDecimal taxableCompensation,
            BigDecimal nonTaxableCompensation) {

        this.basicPay = validateAmount(basicPay, "Basic pay");
        this.riceSubsidy = validateAmount(riceSubsidy, "Rice subsidy");
        this.phoneAllowance = validateAmount(phoneAllowance, "Phone allowance");
        this.clothingAllowance = validateAmount(clothingAllowance, "Clothing allowance");

        this.taxableCompensation =
                validateAmount(taxableCompensation, "Taxable compensation");

        this.nonTaxableCompensation =
                validateAmount(nonTaxableCompensation, "Non-taxable compensation");

        if (getGrossCompensation().compareTo(
                this.taxableCompensation.add(this.nonTaxableCompensation)) != 0) {
            throw new IllegalArgumentException(
                    "Taxable and non-taxable compensation must equal gross compensation.");
        }
    }

    private BigDecimal validateAmount(BigDecimal amount, String name) {
        Objects.requireNonNull(amount, name + " cannot be null.");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(name + " cannot be negative.");
        }

        return amount;
    }

    public BigDecimal getBasicPay() {
        return basicPay;
    }

    public BigDecimal getRiceSubsidy() {
        return riceSubsidy;
    }

    public BigDecimal getPhoneAllowance() {
        return phoneAllowance;
    }

    public BigDecimal getClothingAllowance() {
        return clothingAllowance;
    }

    public BigDecimal getTaxableCompensation() {
        return taxableCompensation;
    }

    public BigDecimal getNonTaxableCompensation() {
        return nonTaxableCompensation;
    }

    public BigDecimal getGrossCompensation() {
        return basicPay
                .add(riceSubsidy)
                .add(phoneAllowance)
                .add(clothingAllowance);
    }
}

