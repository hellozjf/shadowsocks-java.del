package com.hellozjf.shadowsocks.ssserver.vo;

import lombok.Data;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @author hellozjf
 */
@Data
public class AllOffsetDateTimes {

    public AllOffsetDateTimes(String timeZone, Integer dayOfWeek) {
        this(Instant.now().toEpochMilli(), timeZone, dayOfWeek);
    }

    public AllOffsetDateTimes(Long timeMs, String timeZone, Integer dayOfWeek) {
        currentTime = Instant.ofEpochMilli(timeMs).atOffset(ZoneOffset.of(timeZone));
        minuteStartTime = currentTime.truncatedTo(ChronoUnit.MINUTES);
        hourStartTime = currentTime.truncatedTo(ChronoUnit.HOURS);
        halfDayStartTime = currentTime.truncatedTo(ChronoUnit.HALF_DAYS);
        dayStartTime = currentTime.truncatedTo(ChronoUnit.DAYS);
        weekStartTime = dayStartTime.with(TemporalAdjusters.dayOfWeekInMonth(0, DayOfWeek.of(dayOfWeek)));
        monthStartTime = dayStartTime.withDayOfMonth(1);
        Month month = monthStartTime.getMonth();
        if (month.equals(Month.JANUARY) || month.equals(Month.FEBRUARY) || month.equals(Month.MARCH)) {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue());
        } else if (month.equals(Month.APRIL) || month.equals(Month.MAY) || month.equals(Month.JUNE)) {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.APRIL.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue());
        } else if (month.equals(Month.JULY) || month.equals(Month.AUGUST) || month.equals(Month.SEPTEMBER)) {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JULY.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JULY.getValue());
        } else {
            quarterStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.OCTOBER.getValue());
            halfYearStartTime = monthStartTime.with(ChronoField.MONTH_OF_YEAR, Month.JULY.getValue());
        }
        yearStartTime = dayStartTime.withDayOfYear(1);
        beforeH1StartTime = currentTime.minusHours(1);
        beforeH12StartTime = currentTime.minusHours(12);
        beforeD1StartTime = currentTime.minusDays(1);
        beforeW1StartTime = currentTime.minusWeeks(1);
        beforeM1StartTime = currentTime.minusMonths(1);
        beforeM3StartTime = currentTime.minusMonths(3);
        beforeM6StartTime = currentTime.minusMonths(6);
        beforeY1StartTime = currentTime.minusYears(1);

        beforeMin1MinuteStartTime = minuteStartTime.minusMinutes(1);
        beforeMin2MinuteStartTime = minuteStartTime.minusMinutes(2);
        beforeH1Min1MinuteStartTime = minuteStartTime.minusHours(1).minusMinutes(1);
        beforeH12Min1MinuteStartTime = minuteStartTime.minusHours(12).minusMinutes(1);
        beforeD1Min1MinuteStartTime = minuteStartTime.minusDays(1).minusMinutes(1);
        beforeW1Min1MinuteStartTime = minuteStartTime.minusWeeks(1).minusMinutes(1);
        beforeM1Min1MinuteStartTime = minuteStartTime.minusMonths(1).minusMinutes(1);
        beforeM3Min1MinuteStartTime = minuteStartTime.minusMonths(3).minusMinutes(1);
        beforeM6Min1MinuteStartTime = minuteStartTime.minusMonths(6).minusMinutes(1);
        beforeY1Min1MinuteStartTime = minuteStartTime.minusYears(1).minusMinutes(1);
    }

    public Long getCurrentTimeMs() {
        return ms(currentTime);
    }

    public Long getMinuteStartTimeMs() {
        return ms(minuteStartTime);
    }

    public Long getHourStartTimeMs() {
        return ms(hourStartTime);
    }

    public Long getHalfDayStartTimeMs() {
        return ms(halfDayStartTime);
    }

    public Long getDayStartTimeMs() {
        return ms(dayStartTime);
    }

    public Long getWeekStartTimeMs() {
        return ms(weekStartTime);
    }

    public Long getMonthStartTimeMs() {
        return ms(monthStartTime);
    }

    public Long getQuarterStartTimeMs() {
        return ms(quarterStartTime);
    }

    public Long getHalfYearStartTimeMs() {
        return ms(halfYearStartTime);
    }

    public Long getYearStartTimeMs() {
        return ms(yearStartTime);
    }

    public Long getBeforeH1StartTimeMs() {
        return ms(beforeH1StartTime);
    }

    public Long getBeforeH12StartTimeMs() {
        return ms(beforeH12StartTime);
    }

    public Long getBeforeD1StartTimeMs() {
        return ms(beforeD1StartTime);
    }

    public Long getBeforeW1StartTimeMs() {
        return ms(beforeW1StartTime);
    }

    public Long getBeforeM1StartTimeMs() {
        return ms(beforeM1StartTime);
    }

    public Long getBeforeM3StartTimeMs() {
        return ms(beforeM3StartTime);
    }

    public Long getBeforeM6StartTimeMs() {
        return ms(beforeM6StartTime);
    }

    public Long getBeforeY1StartTimeMs() {
        return ms(beforeY1StartTime);
    }

    public Long getBeforeMin1MinuteStartTimeMs() {
        return ms(beforeMin1MinuteStartTime);
    }

    public Long getBeforeMin2MinuteStartTimeMs() {
        return ms(beforeMin2MinuteStartTime);
    }

    public Long getBeforeH1Min1MinuteStartTimeMs() {
        return ms(beforeH1Min1MinuteStartTime);
    }

    public Long getBeforeH12Min1MinuteStartTimeMs() {
        return ms(beforeH12Min1MinuteStartTime);
    }

    public Long getBeforeD1Min1MinuteStartTimeMs() {
        return ms(beforeD1Min1MinuteStartTime);
    }

    public Long getBeforeW1Min1MinuteStartTimeMs() {
        return ms(beforeW1Min1MinuteStartTime);
    }

    public Long getBeforeM1Min1MinuteStartTimeMs() {
        return ms(beforeM1Min1MinuteStartTime);
    }

    public Long getBeforeM3Min1MinuteStartTimeMs() {
        return ms(beforeM3Min1MinuteStartTime);
    }

    public Long getBeforeM6Min1MinuteStartTimeMs() {
        return ms(beforeM6Min1MinuteStartTime);
    }

    public Long getBeforeY1Min1MinuteStartTimeMs() {
        return ms(beforeY1Min1MinuteStartTime);
    }

    private Long ms(OffsetDateTime offsetDateTime) {
        return offsetDateTime.toInstant().toEpochMilli();
    }

    private OffsetDateTime currentTime;
    private OffsetDateTime minuteStartTime;
    private OffsetDateTime hourStartTime;
    private OffsetDateTime halfDayStartTime;
    private OffsetDateTime dayStartTime;
    private OffsetDateTime weekStartTime;
    private OffsetDateTime monthStartTime;
    private OffsetDateTime quarterStartTime;
    private OffsetDateTime halfYearStartTime;
    private OffsetDateTime yearStartTime;
    private OffsetDateTime beforeH1StartTime;
    private OffsetDateTime beforeH12StartTime;
    private OffsetDateTime beforeD1StartTime;
    private OffsetDateTime beforeW1StartTime;
    private OffsetDateTime beforeM1StartTime;
    private OffsetDateTime beforeM3StartTime;
    private OffsetDateTime beforeM6StartTime;
    private OffsetDateTime beforeY1StartTime;

    private OffsetDateTime beforeMin1MinuteStartTime;
    private OffsetDateTime beforeMin2MinuteStartTime;
    private OffsetDateTime beforeH1Min1MinuteStartTime;
    private OffsetDateTime beforeH12Min1MinuteStartTime;
    private OffsetDateTime beforeD1Min1MinuteStartTime;
    private OffsetDateTime beforeW1Min1MinuteStartTime;
    private OffsetDateTime beforeM1Min1MinuteStartTime;
    private OffsetDateTime beforeM3Min1MinuteStartTime;
    private OffsetDateTime beforeM6Min1MinuteStartTime;
    private OffsetDateTime beforeY1Min1MinuteStartTime;
}
