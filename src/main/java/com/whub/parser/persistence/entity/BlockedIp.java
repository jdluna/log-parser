package com.whub.parser.persistence.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class BlockedIp {
    @Id
    private String ipAddress;
    private String reason;
}

