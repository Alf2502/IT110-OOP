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
import domain.payroll.EmployerContributions;
import domain.payroll.PayrollEarnings;
import domain.payroll.PayrollPeriod;

import java.math.BigDecimal;
import java.util.Objects;

public final class StandardEmployerContributionCalculator
        implements EmployerContributionCalculator {

    private final SSSContributionCalculator sssCalculator;
    private final PhilHealthContributionCalculator philHealthCalculator;
    private final PagIbigContributionCalculator pagIbigCalculator;

    public StandardEmployerContributionCalculator(
            SSSContributionCalculator sssCalculator,
            PhilHealthContributionCalculator philHealthCalculator,
            PagIbigContributionCalculator pagIbigCalculator) {

        this.sssCalculator = Objects.requireNonNull(sssCalculator);
        this.philHealthCalculator = Objects.requireNonNull(philHealthCalculator);
        this.pagIbigCalculator = Objects.requireNonNull(pagIbigCalculator);
    }

    @Override
    public EmployerContributions calculateEmployerContributions(
            Employee employee,
            PayrollEarnings earnings,
            PayrollPeriod payrollPeriod) {

        Objects.requireNonNull(employee, "Employee cannot be null.");
        Objects.requireNonNull(earnings, "Earnings cannot be null.");
        Objects.requireNonNull(payrollPeriod, "Payroll period cannot be null.");

        BigDecimal grossCompensation = earnings.getGrossCompensation();

        BigDecimal sss = sssCalculator.calculateEmployerShare(
                grossCompensation
        );

        BigDecimal philHealth = philHealthCalculator.calculateEmployerShare(
                earnings.getBasicPay()
        );

        BigDecimal pagIbig = pagIbigCalculator.calculateEmployerShare(
                grossCompensation
        );

        return new EmployerContributions(
                sss,
                philHealth,
                pagIbig
        );
    }
}
