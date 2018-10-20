package com.whub.parser.data;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParserArgs {
    private String accessLogPath;
    private LocalDateTime startDate;
    private String duration;
    private Integer threshold;
}
