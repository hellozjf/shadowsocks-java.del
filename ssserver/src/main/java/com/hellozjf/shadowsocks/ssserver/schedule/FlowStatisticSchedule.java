package com.hellozjf.shadowsocks.ssserver.schedule;

import com.hellozjf.shadowsocks.ssserver.service.IFlowSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @author hellozjf
 */
@Component
@Slf4j
public class FlowStatisticSchedule {

    @Autowired
    private IFlowSummaryService flowSummaryService;

    /**
     * 每分钟初的时候计算每个用户日、周、月、季度、年的进出流量
     */
    @Scheduled(cron = "0 * * * * ?")
    public void updateFlowSummary() {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("于{}更新FlowSummary", sdf.format(currentDate));
        // 每分钟更新流量汇总表
        flowSummaryService.updateFlowSummary();
        log.debug("于{}更新FlowSummary花费{}ms", sdf.format(currentDate), System.currentTimeMillis() - currentDate.getTime());
    }
}
