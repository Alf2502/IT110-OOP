/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payroll;

/**
 *
 * @author alfie
 */

import domain.payroll.PayrollPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class PayrollPeriodTest {

    @Test
    void shouldSetFirstDayOfMonth() {
        PayrollPeriod period = PayrollPeriod.of(2026, 1);

        assertEquals(
                LocalDate.of(2026, 1, 1),
                period.getStartDate()
        );
    }

    @Test
    void shouldSetLastDayOfMonth() {
        PayrollPeriod period = PayrollPeriod.of(2026, 1);

        assertEquals(
                LocalDate.of(2026, 1, 31),
                period.getEndDate()
        );
    }

    @Test
    void shouldHandleLeapYear() {
        PayrollPeriod period = PayrollPeriod.of(2024, 2);

        assertEquals(
                LocalDate.of(2024, 2, 29),
                period.getEndDate()
        );

        assertTrue(period.isLeapYear());
    }

    @Test
    void shouldHandleNonLeapYear() {
        PayrollPeriod period = PayrollPeriod.of(2026, 2);

        assertEquals(
                LocalDate.of(2026, 2, 28),
                period.getEndDate()
        );

        assertFalse(period.isLeapYear());
    }

    @Test
    void shouldIncludeFirstDayOfPeriod() {
        PayrollPeriod period = PayrollPeriod.of(2026, 3);

        assertTrue(period.contains(LocalDate.of(2026, 3, 1)));
    }

    @Test
    void shouldIncludeLastDayOfPeriod() {
        PayrollPeriod period = PayrollPeriod.of(2026, 3);

        assertTrue(period.contains(LocalDate.of(2026, 3, 31)));
    }

    @Test
    void shouldExcludeDateOutsidePeriod() {
        PayrollPeriod period = PayrollPeriod.of(2026, 3);

        assertFalse(period.contains(LocalDate.of(2026, 4, 1)));
    }

    @Test
    void shouldReturnCorrectNumberOfDays() {
        PayrollPeriod period = PayrollPeriod.of(2026, 1);

        assertEquals(31, period.getNumberOfDays());
    }

    @Test
    void shouldReturnCorrectYearMonth() {
        PayrollPeriod period = PayrollPeriod.of(2026, 11);

        assertEquals(
                YearMonth.of(2026, 11),
                period.getYearMonth()
        );
    }

    @Test
    void shouldRejectInvalidMonth() {
        assertThrows(
                java.time.DateTimeException.class,
                () -> PayrollPeriod.of(2026, 13)
        );
    }
}

