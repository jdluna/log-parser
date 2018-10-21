package com.whub.parser.service;

import com.whub.parser.app.dbdata.IpCountData;
import com.whub.parser.app.type.LogDuration;
import com.whub.parser.persistence.entity.BlockedIp;
import com.whub.parser.persistence.repo.AccessLogRepository;
import com.whub.parser.persistence.repo.BlockedIpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccessLogThresholdServiceImpl implements AccessLogThresholdService {

    private static final Logger log = LoggerFactory.getLogger(AccessLogThresholdServiceImpl.class);

    private final AccessLogRepository accessLogRepository;
    private final BlockedIpRepository blockedIpRepository;

    @Autowired
    public AccessLogThresholdServiceImpl(AccessLogRepository accessLogRepository,
                                         BlockedIpRepository blockedIpRepository) {
        this.accessLogRepository = accessLogRepository;
        this.blockedIpRepository = blockedIpRepository;
    }

    @Override
    public void detectIpThreshold(LocalDateTime startDateTime, LogDuration duration, Integer threshold) {
        LocalDateTime endDateTime = this.calculateEndDateTime(startDateTime, duration);
        //get list of ip address exceeding threshold
        List<IpCountData> countDataList = this.accessLogRepository.findIpExceedingThreshold(startDateTime, endDateTime, threshold.longValue());
        if (countDataList == null || countDataList.isEmpty()) return;

        //prepare to save as blocked ip
        List<BlockedIp> blockedIpList = new ArrayList<>();
        countDataList.forEach(ipCountData -> {
            String comment = ipCountData.getIpAddress() + " exceeding " + duration.name() + " limit of " + threshold + " requests [total: " + ipCountData.getTotal() + "]";
            log.info("====================== " + comment + " ======================");
            blockedIpList.add(BlockedIp.builder()
                    .ipAddress(ipCountData.getIpAddress())
                    .reason(comment)
                    .build()
            );
        });
        this.blockedIpRepository.saveAll(blockedIpList);
    }

    private LocalDateTime calculateEndDateTime(LocalDateTime startDateTime, LogDuration duration) {
        switch (duration) {
            case hourly:
                return startDateTime.plusHours(1);
            case daily:
                return startDateTime.plusHours(24);
            default:
                throw new IllegalArgumentException("");
        }
    }
}
