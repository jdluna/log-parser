package com.whub.parser.runner;

import com.whub.parser.app.args.ParserArgs;
import com.whub.parser.app.type.LogDuration;
import com.whub.parser.service.AccessLogLoaderService;
import com.whub.parser.service.AccessLogThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AccessLogParserRunner implements CommandLineRunner {

    private final Environment environment;
    private final AccessLogLoaderService accessLogLoaderService;
    private final AccessLogThresholdService accessLogThresholdService;

    @Autowired
    public AccessLogParserRunner(Environment environment,
                                 AccessLogLoaderService accessLogLoaderService,
                                 AccessLogThresholdService accessLogThresholdService) {
        this.environment = environment;
        this.accessLogLoaderService = accessLogLoaderService;
        this.accessLogThresholdService = accessLogThresholdService;
    }

    @Override
    public void run(String... args) throws Exception {
        ParserArgs parserArgs = this.parseCommandLineArgs();
        this.accessLogLoaderService.loadAccessLogs(parserArgs);
        this.accessLogThresholdService.detectIpThreshold(parserArgs.getStartDate(), parserArgs.getDuration(), parserArgs.getThreshold());
    }

    /**
     * parse the arguments from command line options
     *
     * @return {@link ParserArgs}
     */
    private ParserArgs parseCommandLineArgs() {
        if (environment.getProperty("accesslog") == null ||
                environment.getProperty("startDate") == null ||
                environment.getProperty("duration") == null ||
                environment.getProperty("threshold") == null) {
            System.out.println("Required command line options are not found");
            System.out.println("Sample usage $: java -jar log-parser.jar --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100");
            System.exit(0);
        }

        LocalDateTime startDate = LocalDateTime.parse(
                environment.getRequiredProperty("startDate"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")
        );
        return ParserArgs.builder()
                .accessLogPath(environment.getRequiredProperty("accesslog"))
                .startDate(startDate)
                .duration(LogDuration.valueOf(environment.getRequiredProperty("duration")))
                .threshold(environment.getRequiredProperty("threshold", Integer.class))
                .build();
    }
}
