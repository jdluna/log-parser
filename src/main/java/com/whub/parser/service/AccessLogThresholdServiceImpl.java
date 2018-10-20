package com.whub.parser.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccessLogThresholdServiceImpl implements AccessLogThresholdService {

    @Override
    public void detectIpThreshold(LocalDateTime startDateTime, String duration, Integer threshold) {

    }
}
