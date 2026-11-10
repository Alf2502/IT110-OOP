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

public final class EmployerContributions {

    private final BigDecimal sssContribution;
    private final BigDecimal philHealthContribution;
    private final BigDecimal pagIbigContribution;

    public EmployerContributions(
            BigDecimal sssContribution,
            BigDecimal philHealthContribution,
            BigDecimal pagIbigContribution) {

        this.sssContribution = validateAmount(
                sssContribution, "Employer SSS contribution");

        this.philHealthContribution = validateAmount(
                philHealthContribution, "Employer PhilHealth contribution");

        this.pagIbigContribution = validateAmount(
                pagIbigContribution, "Employer Pag-IBIG contribution");
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

    public BigDecimal getTotalContributions() {
        return sssContribution
                .add(philHealthContribution)
                .add(pagIbigContribution);
    }

    public boolean hasContributions() {
        return getTotalContributions().compareTo(BigDecimal.ZERO) > 0;
    }
}

