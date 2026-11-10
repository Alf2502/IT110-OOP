/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.payroll;

/**
 *
 * @author alfie
 */

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public final class PayrollPeriod {

    private final YearMonth yearMonth;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public PayrollPeriod(YearMonth yearMonth) {

        this.yearMonth = Objects.requireNonNull(
                yearMonth,
                "Payroll month cannot be null."
        );

        this.startDate = yearMonth.atDay(1);
        this.endDate = yearMonth.atEndOfMonth();
    }

    public static PayrollPeriod of(
            int year,
            int month) {

        return new PayrollPeriod(
                YearMonth.of(year, month)
        );
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean contains(LocalDate date) {

        Objects.requireNonNull(
                date,
                "Date cannot be null."
        );

        return !date.isBefore(startDate)
                && !date.isAfter(endDate);
    }

    public int getNumberOfDays() {
        return yearMonth.lengthOfMonth();
    }

    public boolean isLeapYear() {
        return yearMonth.isLeapYear();
    }

    @Override
    public String toString() {
        return yearMonth.toString();
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof PayrollPeriod other)) {
            return false;
        }

        return yearMonth.equals(other.yearMonth);
    }

    @Override
    public int hashCode() {
        return yearMonth.hashCode();
    }
}

