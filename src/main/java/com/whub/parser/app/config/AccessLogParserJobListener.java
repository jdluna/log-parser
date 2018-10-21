package com.whub.parser.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class AccessLogParserJobListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(AccessLogParserJobListener.class);
    private LocalDateTime start;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        start = LocalDateTime.now();
        log.info("============ Access log parsing Started: " + start.toString() + " ============");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LocalDateTime end = LocalDateTime.now();
        String duration = getDuration(start, end);
        log.info("============ Access log parsing finished: " + end.toString() + " ============");
        log.info("============ Access log parsing took: " + duration + " ============");
    }

    private String getDuration(LocalDateTime start, LocalDateTime end) {
        Long min = ChronoUnit.MINUTES.between(start, end);
        Long sec = ChronoUnit.SECONDS.between(start, end);
        Long ms = ChronoUnit.MILLIS.between(start, end);
        sec = sec - (min * 60);
        ms = ms - (sec * 1000);
        return min + "m " + sec + "s " + ms + "ms ";
    }
}
