package com.whub.parser.service;

import com.whub.parser.app.type.LogDuration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccessLogThresholdServiceImpl implements AccessLogThresholdService {

    @Override
    public void detectIpThreshold(LocalDateTime startDateTime, LogDuration duration, Integer threshold) {

    }
}
