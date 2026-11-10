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

public final class PayrollDeductions {

    private final BigDecimal sssContribution;
    private final BigDecimal philHealthContribution;
    private final BigDecimal pagIbigContribution;
    private final BigDecimal withholdingTax;
    private final BigDecimal otherDeductions;

    public PayrollDeductions(
            BigDecimal sssContribution,
            BigDecimal philHealthContribution,
            BigDecimal pagIbigContribution,
            BigDecimal withholdingTax,
            BigDecimal otherDeductions) {

        this.sssContribution = validateAmount(
                sssContribution, "SSS contribution");

        this.philHealthContribution = validateAmount(
                philHealthContribution, "PhilHealth contribution");

        this.pagIbigContribution = validateAmount(
                pagIbigContribution, "Pag-IBIG contribution");

        this.withholdingTax = validateAmount(
                withholdingTax, "Withholding tax");

        this.otherDeductions = validateAmount(
                otherDeductions, "Other deductions");
    }

    private BigDecimal validateAmount(BigDecimal amount, String name) {
        Objects.requireNonNull(amount, name + " cannot be null.");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    name + " cannot be negative.");
        }

        return amount;
    }

    public BigDecimal getSssContribution() {
        return sssContribution;
    }

    public BigDecimal getPhilHealthContribution() {
        return philHealthContribution;
    }

    public BigDecimal getPagIbigContribution() {
        return pagIbigContribution;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public BigDecimal getOtherDeductions() {
        return otherDeductions;
    }

    public BigDecimal getTotalDeductions() {
        return sssContribution
                .add(philHealthContribution)
                .add(pagIbigContribution)
                .add(withholdingTax)
                .add(otherDeductions);
    }

    public boolean hasDeductions() {
        return getTotalDeductions().compareTo(BigDecimal.ZERO) > 0;
    }
}
