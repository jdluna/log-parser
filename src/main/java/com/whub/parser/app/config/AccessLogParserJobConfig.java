package com.whub.parser.app.config;

import com.whub.parser.app.dto.AccessLogDTO;
import com.whub.parser.persistence.entity.AccessLog;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
public class AccessLogParserJobConfig {

    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final Environment environment;
    private final JdbcTemplate jdbcTemplate;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public AccessLogParserJobConfig(Environment environment,
                                    JdbcTemplate jdbcTemplate,
                                    JobBuilderFactory jobBuilderFactory,
                                    StepBuilderFactory stepBuilderFactory) {
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    Job logFileToDbJob(AccessLogParserJobListener accessLogParserJobListener) {
        return jobBuilderFactory.get("logFileToDbJob")
                .incrementer(new RunIdIncrementer())
                .listener(accessLogParserJobListener)
                .flow(logFileToDbStep())
                .end()
                .build();
    }

    @Bean
    public Step logFileToDbStep() {
        return stepBuilderFactory.get("logFileToDbStep")
                .<AccessLogDTO, AccessLog>chunk(250)
                .reader(accessLogItemReader())
                .processor(accessLogItemProcessor())
                .writer(accessLogItemWriter())
                .build();
    }

    @Bean
    public ItemReader<AccessLogDTO> accessLogItemReader() {
        FlatFileItemReader<AccessLogDTO> accessLogFlatReader = new FlatFileItemReader<>();
        accessLogFlatReader.setResource(new FileSystemResource(environment.getProperty("accesslog")));
        accessLogFlatReader.setLineMapper(createLogLineMapper());
        return accessLogFlatReader;
    }

    @Bean
    public ItemProcessor<AccessLogDTO, AccessLog> accessLogItemProcessor() {
        return new ItemProcessor<AccessLogDTO, AccessLog>() {
            @Override
            public AccessLog process(AccessLogDTO item) throws Exception {
                return AccessLog.builder()
                        .logDate(LocalDateTime.parse(item.getLogDateStr(), LOG_DATE_FORMATTER))
                        .ipAddress(item.getIpAddress())
                        .request(item.getRequest())
                        .status(Integer.parseInt(item.getStatus()))
                        .userAgent(item.getUserAgent())
                        .build();
            }
        };
    }

    @Bean
    public JdbcBatchItemWriter<AccessLog> accessLogItemWriter() {
        JdbcBatchItemWriter<AccessLog> logWriter = new JdbcBatchItemWriter<AccessLog>();
        logWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<AccessLog>());
        logWriter.setSql("INSERT INTO access_log (log_date, ip_address, request, status, user_agent) VALUES (:logDate, :ipAddress, :request, :status, :userAgent)");
        logWriter.setDataSource(jdbcTemplate.getDataSource());
        return logWriter;
    }

    private LineMapper<AccessLogDTO> createLogLineMapper() {
        DefaultLineMapper<AccessLogDTO> logLineMapper = new DefaultLineMapper<>();
        logLineMapper.setLineTokenizer(createLogLineTokenizer());
        logLineMapper.setFieldSetMapper(createLogFieldMapper());
        return logLineMapper;
    }

    private LineTokenizer createLogLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("|");
        lineTokenizer.setQuoteCharacter('"');
        lineTokenizer.setNames("logDateStr", "ipAddress", "request", "status", "userAgent");
        return lineTokenizer;
    }

    private FieldSetMapper<AccessLogDTO> createLogFieldMapper() {
        BeanWrapperFieldSetMapper<AccessLogDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(AccessLogDTO.class);
        return fieldSetMapper;
    }
}
