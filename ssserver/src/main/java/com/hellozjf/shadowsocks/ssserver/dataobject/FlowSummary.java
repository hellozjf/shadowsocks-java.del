package com.hellozjf.shadowsocks.ssserver.dataobject;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Jingfeng Zhou
 *
 * 这个表每分钟会为所有用户都添加一条记录，记录这分钟的上一分钟的各种流量信息
 */
@Entity
@Data
@Table(indexes = {
        @Index(name = "timeId", columnList = "recordMinuteTime, userInfoId"),
        @Index(name = "idTime", columnList = "userInfoId, recordMinuteTime")
})
@Slf4j
public class FlowSummary extends BaseEntity implements Cloneable {

    public FlowSummary() {}

    public FlowSummary(Long recordMinuteTime, String userInfoId) {
        this.userInfoId = userInfoId;
        this.recordMinuteTime = recordMinuteTime;
        this.thisMinuteInFlowSummary = 0L;
        this.thisMinuteOutFlowSummary = 0L;
        this.thisHourInFlowSummary = 0L;
        this.thisHourOutFlowSummary = 0L;
        this.h1InFlowSummary = 0L;
        this.h1OutFlowSummary = 0L;
        this.thisHalfDayInFlowSummary = 0L;
        this.thisHalfDayOutFlowSummary = 0L;
        this.h12InFlowSummary = 0L;
        this.h12OutFlowSummary = 0L;
        this.thisDayInFlowSummary = 0L;
        this.thisDayOutFlowSummary = 0L;
        this.d1InFlowSummary = 0L;
        this.d1OutFlowSummary = 0L;
        this.thisWeekInFlowSummary = 0L;
        this.thisWeekOutFlowSummary = 0L;
        this.w1InFlowSummary = 0L;
        this.w1OutFlowSummary = 0L;
        this.thisMonthInFlowSummary = 0L;
        this.thisMonthOutFlowSummary = 0L;
        this.m1InFlowSummary = 0L;
        this.m1OutFlowSummary = 0L;
        this.thisQuarterInFlowSummary = 0L;
        this.thisQuarterOutFlowSummary = 0L;
        this.m3InFlowSummary = 0L;
        this.m3OutFlowSummary = 0L;
        this.thisHalfYearInFlowSummary = 0L;
        this.thisHalfYearOutFlowSummary = 0L;
        this.m6InFlowSummary = 0L;
        this.m6OutFlowSummary = 0L;
        this.thisYearInFlowSummary = 0L;
        this.thisYearOutFlowSummary = 0L;
        this.y1InFlowSummary = 0L;
        this.y1OutFlowSummary = 0L;
        this.totalInFlowSummary = 0L;
        this.totalOutFlowSummary = 0L;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public FlowSummary add(FlowSummary flowSummary) {
        try {
            FlowSummary retFlowSummary = (FlowSummary) this.clone();
            retFlowSummary.thisMinuteInFlowSummary += flowSummary.thisMinuteInFlowSummary;
            retFlowSummary.thisMinuteOutFlowSummary += flowSummary.thisMinuteOutFlowSummary;
            retFlowSummary.thisHourInFlowSummary += flowSummary.thisHourInFlowSummary;
            retFlowSummary.thisHourOutFlowSummary += flowSummary.thisHourOutFlowSummary;
            retFlowSummary.h1InFlowSummary += flowSummary.h1InFlowSummary;
            retFlowSummary.h1OutFlowSummary += flowSummary.h1OutFlowSummary;
            retFlowSummary.thisHalfDayInFlowSummary += flowSummary.thisHalfDayInFlowSummary;
            retFlowSummary.thisHalfDayOutFlowSummary += flowSummary.thisHalfDayOutFlowSummary;
            retFlowSummary.h12InFlowSummary += flowSummary.h12InFlowSummary;
            retFlowSummary.h12OutFlowSummary += flowSummary.h12OutFlowSummary;
            retFlowSummary.thisDayInFlowSummary += flowSummary.thisDayInFlowSummary;
            retFlowSummary.thisDayOutFlowSummary += flowSummary.thisDayOutFlowSummary;
            retFlowSummary.d1InFlowSummary += flowSummary.d1InFlowSummary;
            retFlowSummary.d1OutFlowSummary += flowSummary.d1OutFlowSummary;
            retFlowSummary.thisWeekInFlowSummary += flowSummary.thisWeekInFlowSummary;
            retFlowSummary.thisWeekOutFlowSummary += flowSummary.thisWeekOutFlowSummary;
            retFlowSummary.w1InFlowSummary += flowSummary.w1InFlowSummary;
            retFlowSummary.w1OutFlowSummary += flowSummary.w1OutFlowSummary;
            retFlowSummary.thisMonthInFlowSummary += flowSummary.thisMonthInFlowSummary;
            retFlowSummary.thisMonthOutFlowSummary += flowSummary.thisMonthOutFlowSummary;
            retFlowSummary.m1InFlowSummary += flowSummary.m1InFlowSummary;
            retFlowSummary.m1OutFlowSummary += flowSummary.m1OutFlowSummary;
            retFlowSummary.thisQuarterInFlowSummary += flowSummary.thisQuarterInFlowSummary;
            retFlowSummary.thisQuarterOutFlowSummary += flowSummary.thisQuarterOutFlowSummary;
            retFlowSummary.m3InFlowSummary += flowSummary.m3InFlowSummary;
            retFlowSummary.m3OutFlowSummary += flowSummary.m3OutFlowSummary;
            retFlowSummary.thisHalfYearInFlowSummary += flowSummary.thisHalfYearInFlowSummary;
            retFlowSummary.thisHalfYearOutFlowSummary += flowSummary.thisHalfYearOutFlowSummary;
            retFlowSummary.m6InFlowSummary += flowSummary.m6InFlowSummary;
            retFlowSummary.m6OutFlowSummary += flowSummary.m6OutFlowSummary;
            retFlowSummary.thisYearInFlowSummary += flowSummary.thisYearInFlowSummary;
            retFlowSummary.thisYearOutFlowSummary += flowSummary.thisYearOutFlowSummary;
            retFlowSummary.y1InFlowSummary += flowSummary.y1InFlowSummary;
            retFlowSummary.y1OutFlowSummary += flowSummary.y1OutFlowSummary;
            retFlowSummary.totalInFlowSummary += flowSummary.totalInFlowSummary;
            retFlowSummary.totalOutFlowSummary += flowSummary.totalOutFlowSummary;
            return retFlowSummary;
        } catch (CloneNotSupportedException e) {
            log.error("CloneNotSupportedException e = {}", e);
            return new FlowSummary();
        }
    }

    /**
     * UserInfo的ID
     */
    private String userInfoId;
    /**
     * 记录是哪一分钟的时间
     */
    private Long recordMinuteTime;
    /**
     * 这分钟的入口流量汇总
     */
    private Long thisMinuteInFlowSummary;
    /**
     * 这分钟的出口流量汇总
     */
    private Long thisMinuteOutFlowSummary;
    /**
     * 这个小时的入口流量汇总
     */
    private Long thisHourInFlowSummary;
    /**
     * 这个小时的出口流量汇总
     */
    private Long thisHourOutFlowSummary;
    /**
     * 从上个小时这时刻到现在这时刻的入口流量汇总
     */
    private Long h1InFlowSummary;
    /**
     * 从上个小时这时刻到现在这时刻的出口流量汇总
     */
    private Long h1OutFlowSummary;
    /**
     * 这个半天的入口流量汇总
     */
    private Long thisHalfDayInFlowSummary;
    /**
     * 这个半天的出口流量汇总
     */
    private Long thisHalfDayOutFlowSummary;
    /**
     * 从上个12小时这时刻到现在这时刻的入口流量汇总
     */
    private Long h12InFlowSummary;
    /**
     * 从上个12小时这时刻到现在这时刻的出口流量汇总
     */
    private Long h12OutFlowSummary;
    /**
     * 今日从0点到24点的入口流量汇总
     */
    private Long thisDayInFlowSummary;
    /**
     * 今天从0点到24点的出口流量汇总
     */
    private Long thisDayOutFlowSummary;
    /**
     * 从昨天这时刻到今天这时刻的入口流量汇总
     */
    private Long d1InFlowSummary;
    /**
     * 从昨天这时刻到今天这时刻的出口流量汇总
     */
    private Long d1OutFlowSummary;
    /**
     * 这周从周日到周六的入口流量汇总
     */
    private Long thisWeekInFlowSummary;
    /**
     * 这周从周日到周六的出口流量汇总
     */
    private Long thisWeekOutFlowSummary;
    /**
     * 从上个礼拜这时刻到现在这时刻的入口流量汇总
     */
    private Long w1InFlowSummary;
    /**
     * 从上个礼拜这时刻到现在这时刻的出口流量汇总
     */
    private Long w1OutFlowSummary;
    /**
     * 这月从月初到月末的入口流量汇总
     */
    private Long thisMonthInFlowSummary;
    /**
     * 这月从月初到月末的出口流量汇总
     */
    private Long thisMonthOutFlowSummary;
    /**
     * 从上个月这时刻到现在这时刻的入口流量汇总
     */
    private Long m1InFlowSummary;
    /**
     * 从上个月这时刻到现在这时刻的出口流量汇总
     */
    private Long m1OutFlowSummary;
    /**
     * 这季度从季度初到季度末的入口流量汇总
     */
    private Long thisQuarterInFlowSummary;
    /**
     * 这季度从季度初到季度末的出口流量汇总
     */
    private Long thisQuarterOutFlowSummary;
    /**
     * 从三个月前这时刻到现在这时刻的入口流量汇总
     */
    private Long m3InFlowSummary;
    /**
     * 从三个月前这时刻到现在这时刻的出口流量汇总
     */
    private Long m3OutFlowSummary;
    /**
     * 这半年从半年初到半年末的入口流量汇总
     */
    private Long thisHalfYearInFlowSummary;
    /**
     * 这半年从半年初到半年末的出口流量汇总
     */
    private Long thisHalfYearOutFlowSummary;
    /**
     * 从六个月前这时刻到现在这时刻的入口流量汇总
     */
    private Long m6InFlowSummary;
    /**
     * 从六个月前这时刻到现在这时刻的出口流量汇总
     */
    private Long m6OutFlowSummary;
    /**
     * 这年从年初到年末的入口流量汇总
     */
    private Long thisYearInFlowSummary;
    /**
     * 这年从年初到年末的出口流量汇总
     */
    private Long thisYearOutFlowSummary;
    /**
     * 从去年这时刻到现在这时刻的入口流量汇总
     */
    private Long y1InFlowSummary;
    /**
     * 从去年这时刻到现在这时刻的出口流量汇总
     */
    private Long y1OutFlowSummary;
    /**
     * 全部入口流量汇总
     */
    private Long totalInFlowSummary;
    /**
     * 全部出口流量汇总
     */
    private Long totalOutFlowSummary;
}
