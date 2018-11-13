package com.hellozjf.shadowsocks.ssserver.util;

import com.hellozjf.shadowsocks.ssserver.SpringContextUtil;
import com.hellozjf.shadowsocks.ssserver.constant.ResultEnum;
import com.hellozjf.shadowsocks.ssserver.exception.ShadowsocksException;
import com.hellozjf.shadowsocks.ssserver.service.impl.FlowStatisticsDetailServiceImpl;

/**
 * @author hellozjf
 */
public class BeanUtils {

    /**
     * 从SpringContext中获取FlowStatisticsDetailServiceImpl
     *
     * @return
     */
    public static FlowStatisticsDetailServiceImpl getFlowStatisticsDetailServiceImpl() {
        Object bean = SpringContextUtil.getBean("flowStatisticsDetailServiceImpl");
        if (bean instanceof FlowStatisticsDetailServiceImpl) {
            return (FlowStatisticsDetailServiceImpl) bean;
        } else {
            throw new ShadowsocksException(ResultEnum.CAN_NOT_GET_BEAN);
        }
    }
}
