package com.whub.parser.app.args;

import com.whub.parser.app.type.LogDuration;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParserArgs {
    private String accessLogPath;
    private LocalDateTime startDate;
    private LogDuration duration;
    private Integer threshold;
}
