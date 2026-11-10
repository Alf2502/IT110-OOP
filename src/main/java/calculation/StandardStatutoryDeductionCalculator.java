/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculation;

/**
 *
 * @author alfie
 */

import domain.employee.Employee;
import domain.payroll.PayrollEarnings;
import domain.payroll.PayrollPeriod;
import domain.payroll.PayrollDeductions;

import java.math.BigDecimal;
import java.util.Objects;

public final class StandardStatutoryDeductionCalculator
        implements StatutoryDeductionCalculator {

    private final SSSContributionCalculator sssCalculator;
    private final PhilHealthContributionCalculator philHealthCalculator;
    private final PagIbigContributionCalculator pagIbigCalculator;
    private final WithholdingTaxCalculator withholdingTaxCalculator;

    public StandardStatutoryDeductionCalculator(
            SSSContributionCalculator sssCalculator,
            PhilHealthContributionCalculator philHealthCalculator,
            PagIbigContributionCalculator pagIbigCalculator,
            WithholdingTaxCalculator withholdingTaxCalculator) {

        this.sssCalculator = Objects.requireNonNull(sssCalculator);
        this.philHealthCalculator = Objects.requireNonNull(philHealthCalculator);
        this.pagIbigCalculator = Objects.requireNonNull(pagIbigCalculator);
        this.withholdingTaxCalculator =
                Objects.requireNonNull(withholdingTaxCalculator);
    }

    @Override
    public PayrollDeductions calculateEmployeeDeductions(
            Employee employee,
            PayrollEarnings earnings,
            PayrollPeriod payrollPeriod) {

        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(earnings, "Earnings cannot be null.");
        Objects.requireNonNull(payrollPeriod, "Payroll period cannot be null.");

        BigDecimal grossCompensation = earnings.getGrossCompensation();

        BigDecimal sss = sssCalculator.calculateEmployeeShare(
                grossCompensation
        );

        BigDecimal philHealth = philHealthCalculator.calculateEmployeeShare(
                earnings.getBasicPay()
        );

        BigDecimal pagIbig = pagIbigCalculator.calculateEmployeeShare(
                grossCompensation
        );

        BigDecimal taxableIncome = earnings.getTaxableCompensation()
                .subtract(sss)
                .subtract(philHealth)
                .subtract(pagIbig)
                .max(BigDecimal.ZERO);

        BigDecimal withholdingTax =
                withholdingTaxCalculator.calculateMonthlyTax(taxableIncome);

        return new PayrollDeductions(
                sss,
                philHealth,
                pagIbig,
                withholdingTax,
                BigDecimal.ZERO
        );
    }
}

