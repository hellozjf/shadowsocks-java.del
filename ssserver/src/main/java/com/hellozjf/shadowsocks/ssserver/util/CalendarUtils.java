package com.hellozjf.shadowsocks.ssserver.util;

import com.hellozjf.shadowsocks.ssserver.config.CustomConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author hellozjf
 */
@Component
public class CalendarUtils {

    @Autowired
    private CustomConfig customConfig;

    /**
     * 中国时区
     */
    public static String CST = "CST";

    /**
     * 获取现在的时间
     * @return
     */
    public Calendar getCurrentTime() {
        return getCurrentTime(customConfig.getTimeZone());
    }

    /**
     * 获取这小时初的时间
     * @return
     */
    public Calendar getHourStartTime() {
        return getHourStartTime(customConfig.getTimeZone());
    }

    /**
     * 获取今天初的时间
     * @return
     */
    public Calendar getTodayStartTime() {
        return getTodayStartTime(customConfig.getTimeZone());
    }

    /**
     * 获取这周初的时间，周日是一周的开始，因为Calendar类就是这样规定的
     * @return
     */
    public Calendar getWeekStartTime() {
        return getWeekStartTime(customConfig.getTimeZone());
    }

    /**
     * 获取这月初的时间
     * @return
     */
    public Calendar getMonthStartTime() {
        return getMonthStartTime(customConfig.getTimeZone());
    }

    /**
     * 获取这季度初的时间
     * @return
     */
    public Calendar getQuarterStartTime() {
        return getQuarterStartTime(customConfig.getTimeZone());
    }

    /**
     * 获取这年初的时间
     * @return
     */
    public Calendar getYearStartTime() {
        return getYearStartTime(customConfig.getTimeZone());
    }

    /**
     * 获取昨天这个时候的时间
     * @return
     */
    public Calendar getH24StartTime() {
        return getH24StartTime(customConfig.getTimeZone());
    }

    /**
     * 获取上周这个时候的时间
     * @return
     */
    public Calendar getD7StartTime() {
        return getD7StartTime(customConfig.getTimeZone());
    }

    /**
     * 获取上个月这个时候的时间
     * @return
     */
    public Calendar getM1StartTime() {
        return getM1StartTime(customConfig.getTimeZone());
    }

    /**
     * 获取上个季度这个时候的时间
     * @return
     */
    public Calendar getM3StartTime() {
        return getM1StartTime(customConfig.getTimeZone());
    }

    /**
     * 获取去年这个时候的时间
     * @return
     */
    public Calendar getY1StartTime() {
        return getY1StartTime(customConfig.getTimeZone());
    }

    /**
     * 获取TimeZone对应的时间
     * @param timeZone
     * @return
     */
    public Calendar getCurrentTime(String timeZone) {
        return Calendar.getInstance(TimeZone.getTimeZone(timeZone));
    }

    /**
     * 获取TimeZone这小时初的时间
     * @return
     */
    public Calendar getHourStartTime(String timeZone) {
        Calendar hourStartTime = getCurrentTime(timeZone);
        hourStartTime.set(Calendar.MILLISECOND, 0);
        hourStartTime.set(Calendar.SECOND, 0);
        hourStartTime.set(Calendar.MINUTE, 0);
        return hourStartTime;
    }

    /**
     * 获取timeZone今天初的时间
     * @return
     */
    public Calendar getTodayStartTime(String timeZone) {
        Calendar todayStartTime = getCurrentTime(timeZone);
        todayStartTime.set(Calendar.MILLISECOND, 0);
        todayStartTime.set(Calendar.SECOND, 0);
        todayStartTime.set(Calendar.MINUTE, 0);
        todayStartTime.set(Calendar.HOUR_OF_DAY, 0);
        return todayStartTime;
    }

    /**
     * 获取TimeZone这周初的时间，周日是一周的开始，因为Calendar类就是这样规定的
     * @return
     */
    public Calendar getWeekStartTime(String timeZone) {
        Calendar weekStartTime = getCurrentTime(timeZone);
        weekStartTime.set(Calendar.SECOND, 0);
        weekStartTime.set(Calendar.MINUTE, 0);
        weekStartTime.set(Calendar.HOUR_OF_DAY, 0);
        weekStartTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return weekStartTime;
    }

    /**
     * 获取timeZone这月初的时间
     * @return
     */
    public Calendar getMonthStartTime(String timeZone) {
        Calendar monthStartTime = getCurrentTime(timeZone);
        monthStartTime.set(Calendar.SECOND, 0);
        monthStartTime.set(Calendar.MINUTE, 0);
        monthStartTime.set(Calendar.HOUR_OF_DAY, 0);
        monthStartTime.set(Calendar.DAY_OF_MONTH, 1);
        return monthStartTime;
    }

    /**
     * 获取timeZone这季度初的时间
     * @return
     */
    public Calendar getQuarterStartTime(String timeZone) {
        Calendar quarterStartTime = getCurrentTime(timeZone);
        quarterStartTime.set(Calendar.SECOND, 0);
        quarterStartTime.set(Calendar.MINUTE, 0);
        quarterStartTime.set(Calendar.HOUR_OF_DAY, 0);
        quarterStartTime.set(Calendar.DAY_OF_MONTH, 1);
        int month = quarterStartTime.get(Calendar.MONTH);
        if (Calendar.JANUARY <= month && month <= Calendar.MARCH) {
            quarterStartTime.set(Calendar.MONTH, Calendar.JANUARY);
        } else if (Calendar.APRIL <= month && month <= Calendar.JUNE) {
            quarterStartTime.set(Calendar.MONTH, Calendar.APRIL);
        } else if (Calendar.JULY <= month && month <= Calendar.SEPTEMBER) {
            quarterStartTime.set(Calendar.MONTH, Calendar.JULY);
        } else {
            quarterStartTime.set(Calendar.MONTH, Calendar.OCTOBER);
        }
        return quarterStartTime;
    }

    /**
     * 获取timeZone这年初的时间
     * @return
     */
    public Calendar getYearStartTime(String timeZone) {
        Calendar yearStartTime = getCurrentTime(timeZone);
        yearStartTime.set(Calendar.SECOND, 0);
        yearStartTime.set(Calendar.MINUTE, 0);
        yearStartTime.set(Calendar.HOUR_OF_DAY, 0);
        yearStartTime.set(Calendar.DAY_OF_MONTH, 1);
        yearStartTime.set(Calendar.MONTH, Calendar.JANUARY);
        return yearStartTime;
    }

    /**
     * 获取昨天这个时候的时间
     * @param timeZone
     * @return
     */
    public Calendar getH24StartTime(String timeZone) {
        Calendar h24StartTime = getCurrentTime(timeZone);
        h24StartTime.set(Calendar.DAY_OF_YEAR, -1);
        return h24StartTime;
    }

    /**
     * 获取上周这个时候的时间
     * @param timeZone
     * @return
     */
    public Calendar getD7StartTime(String timeZone) {
        Calendar d7StartTime = getCurrentTime(timeZone);
        d7StartTime.set(Calendar.WEEK_OF_YEAR, -1);
        return d7StartTime;
    }

    /**
     * 获取上个月这个时候的时间
     * @param timeZone
     * @return
     */
    public Calendar getM1StartTime(String timeZone) {
        Calendar m1StartTime = getCurrentTime(timeZone);
        m1StartTime.set(Calendar.MONTH, -1);
        return m1StartTime;
    }

    /**
     * 获取上个季度这个时候的时间
     * @param timeZone
     * @return
     */
    public Calendar getM3StartTime(String timeZone) {
        Calendar m3StartTime = getCurrentTime(timeZone);
        m3StartTime.set(Calendar.MONTH, -3);
        return m3StartTime;
    }

    /**
     * 获取去年这个时候的时间
     * @param timeZone
     * @return
     */
    public Calendar getY1StartTime(String timeZone) {
        Calendar y1StartTime = getCurrentTime(timeZone);
        y1StartTime.set(Calendar.YEAR, -1);
        return y1StartTime;
    }
}
