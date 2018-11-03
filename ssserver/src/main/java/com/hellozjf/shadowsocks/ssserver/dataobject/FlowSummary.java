package com.hellozjf.shadowsocks.ssserver.dataobject;

import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Jingfeng Zhou
 */
@Entity
@Data
public class FlowSummary extends BaseEntity {

    /**
     * UserInfo的ID
     */
    private String userInfoId;
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
