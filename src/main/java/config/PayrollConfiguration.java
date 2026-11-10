/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author alfie
 */

import java.time.LocalDate;
import java.util.Objects;

public final class PayrollConfiguration {

    private final String version;
    private final LocalDate effectiveFrom;
    private final LocalDate effectiveUntil;

    public PayrollConfiguration(
            String version,
            LocalDate effectiveFrom,
            LocalDate effectiveUntil) {

        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException(
                    "Configuration version cannot be blank.");
        }

        this.version = version.trim();

        this.effectiveFrom = Objects.requireNonNull(
                effectiveFrom, "Effective start date cannot be null.");

        this.effectiveUntil = effectiveUntil;

        if (effectiveUntil != null
                && effectiveUntil.isBefore(effectiveFrom)) {
            throw new IllegalArgumentException(
                    "Effective end date cannot precede start date.");
        }
    }

    public String getVersion() {
        return version;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public LocalDate getEffectiveUntil() {
        return effectiveUntil;
    }

    public boolean appliesTo(LocalDate payrollDate) {
        Objects.requireNonNull(payrollDate, "Payroll date cannot be null.");

        boolean afterStart = !payrollDate.isBefore(effectiveFrom);

        boolean beforeEnd = effectiveUntil == null
                || !payrollDate.isAfter(effectiveUntil);

        return afterStart && beforeEnd;
    }
}

