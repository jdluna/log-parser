package com.whub.parser.service;

import com.whub.parser.app.type.LogDuration;

import java.time.LocalDateTime;

public interface AccessLogThresholdService {
    /**
     * read & save ip exceeding threshold within given duration
     * @param startDateTime time to start
     * @param duration daily/hourly
     * @param threshold total count
     */
    public void detectIpThreshold(LocalDateTime startDateTime, LogDuration duration, Integer threshold);
}
