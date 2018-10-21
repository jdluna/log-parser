package com.whub.parser.service;

import com.whub.parser.app.args.ParserArgs;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * parse the log and save into db
 */
@Service
public class AccessLogLoaderServiceImpl implements AccessLogLoaderService {

    private final JobLauncher jobLauncher;
    private final Job logFileToDbJob;

    public AccessLogLoaderServiceImpl(JobLauncher jobLauncher,
                                      Job logFileToDbJob) {
        this.jobLauncher = jobLauncher;
        this.logFileToDbJob = logFileToDbJob;
    }

    @Override
    public void loadAccessLogs(ParserArgs parserArgs) throws Exception {
        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put("executionId", new JobParameter(UUID.randomUUID().toString()));
        parameterMap.put("accesslog", new JobParameter(parserArgs.getAccessLogPath()));
        parameterMap.put("startDate", new JobParameter(parserArgs.getStartDate().toString()));
        parameterMap.put("duration", new JobParameter(parserArgs.getDuration().name()));
        parameterMap.put("threshold", new JobParameter(parserArgs.getThreshold().toString()));
        this.jobLauncher.run(logFileToDbJob, new JobParameters(parameterMap));
    }
}
