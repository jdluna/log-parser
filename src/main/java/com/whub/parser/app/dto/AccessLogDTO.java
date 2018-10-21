package com.whub.parser.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogDTO {
    private String logDateStr;
    private String ipAddress;
    private String request;
    private String status;
    private String userAgent;
}
