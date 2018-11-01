package com.hellozjf.shadowsocks.ssserver.dataobject;

import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Jingfeng Zhou
 */
@Entity
@Data
public class FlowSummary extends BaseEntity {

    public FlowSummary() {
        todayInFlowSummary = 0L;
        todayOutFlowSummary = 0L;
        weekInFlowSummary = 0L;
        weekOutFlowSummary = 0L;
        monthInFlowSummary = 0L;
        monthOutFlowSummary = 0L;
        quarterInFlowSummary = 0L;
        quarterOutFlowSummary = 0L;
        yearInFlowSummary = 0L;
        yearOutFlowSummary = 0L;
        y1OutFlowSummary = 0L;
        h24InFlowSummary = 0L;
        h24OutFlowSummary = 0L;
        d7InFlowSummary = 0L;
        d7OutFlowSummary = 0L;
        m1InFlowSummary = 0L;
        m1OutFlowSummary = 0L;
        m3InFlowSummary = 0L;
        m3OutFlowSummary = 0L;
        y1InFlowSummary = 0L;
        y1OutFlowSummary = 0L;
    }

    /**
     * UserInfo的ID
     */
    private Long userInfoId;
    /**
     * 今日从0点到24点的入口流量汇总
     */
    private Long todayInFlowSummary;
    /**
     * 今天从0点到24点的出口流量汇总
     */
    private Long todayOutFlowSummary;
    /**
     * 从昨天这时刻到今天这时刻的入口流量汇总
     */
    private Long h24InFlowSummary;
    /**
     * 从昨天这时刻到今天这时刻的出口流量汇总
     */
    private Long h24OutFlowSummary;
    /**
     * 这周从周日到周六的入口流量汇总
     */
    private Long weekInFlowSummary;
    /**
     * 这周从周日到周六的出口流量汇总
     */
    private Long weekOutFlowSummary;
    /**
     * 从上个礼拜这时刻到现在这时刻的入口流量汇总
     */
    private Long d7InFlowSummary;
    /**
     * 从上个礼拜这时刻到现在这时刻的出口流量汇总
     */
    private Long d7OutFlowSummary;
    /**
     * 这月从月初到月末的入口流量汇总
     */
    private Long monthInFlowSummary;
    /**
     * 这月从月初到月末的出口流量汇总
     */
    private Long monthOutFlowSummary;
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
    private Long quarterInFlowSummary;
    /**
     * 这季度从季度初到季度末的出口流量汇总
     */
    private Long quarterOutFlowSummary;
    /**
     * 从三个月前这时刻到现在这时刻的入口流量汇总
     */
    private Long m3InFlowSummary;
    /**
     * 从三个月前这时刻到现在这时刻的出口流量汇总
     */
    private Long m3OutFlowSummary;
    /**
     * 这年从年初到年末的入口流量汇总
     */
    private Long yearInFlowSummary;
    /**
     * 这年从年初到年末的出口流量汇总
     */
    private Long yearOutFlowSummary;
    /**
     * 从去年这时刻到现在这时刻的入口流量汇总
     */
    private Long y1InFlowSummary;
    /**
     * 从去年这时刻到现在这时刻的出口流量汇总
     */
    private Long y1OutFlowSummary;
}
