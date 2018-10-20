package com.whub.parser.service;

import com.whub.parser.data.ParserArgs;

import java.time.LocalDateTime;

public interface AccessLogThresholdService {
    /**
     * read & save ip exceeding threshold within given duration
     * @param startDateTime time to start
     * @param duration daily/hourly
     * @param threshold total count
     */
    public void detectIpThreshold(LocalDateTime startDateTime, String duration, Integer threshold);
}
