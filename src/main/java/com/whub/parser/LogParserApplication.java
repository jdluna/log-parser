package com.whub.parser;

import com.whub.parser.data.ParserArgs;
import com.whub.parser.service.AccessLogLoaderService;
import com.whub.parser.service.AccessLogThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class LogParserApplication implements CommandLineRunner {

    private final Environment environment;
    private final AccessLogLoaderService accessLogLoaderService;
    private final AccessLogThresholdService accessLogThresholdService;

    @Autowired
    public LogParserApplication(Environment environment,
                                AccessLogLoaderService accessLogLoaderService,
                                AccessLogThresholdService accessLogThresholdService) {
        this.environment = environment;
        this.accessLogLoaderService = accessLogLoaderService;
        this.accessLogThresholdService = accessLogThresholdService;
    }

    /**
     * main func to run the application
     *
     * @param args command line options
     */
    public static void main(String[] args) {
        SpringApplication.run(LogParserApplication.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {
        ParserArgs parserArgs = this.parseCommandLineArgs();
        this.accessLogLoaderService.loadAccessLogs(parserArgs);
        this.accessLogThresholdService.detectIpThreshold(
                parserArgs.getStartDate(),
                parserArgs.getDuration(),
                parserArgs.getThreshold()
        );
    }


    /**
     * parse the arguments from command line options
     *
     * @return {@link ParserArgs}
     */
    private ParserArgs parseCommandLineArgs() {
        LocalDateTime startDate = LocalDateTime.parse(
                environment.getRequiredProperty("startDate"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss")
        );
        return ParserArgs.builder()
                .accessLogPath(environment.getRequiredProperty("accesslog"))
                .startDate(startDate)
                .duration(environment.getRequiredProperty("duration"))
                .threshold(environment.getRequiredProperty("threshold", Integer.class))
                .build();
    }
}
