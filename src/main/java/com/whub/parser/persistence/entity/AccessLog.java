package com.whub.parser.persistence.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime logDate;
    private String ipAddress;
    private String request;
    private Integer status;
    private String userAgent;
}
