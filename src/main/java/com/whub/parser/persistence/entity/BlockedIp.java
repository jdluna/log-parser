package com.whub.parser.persistence.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BlockedIp {
    @Id
    private String ipAddress;
    private String reason;
}

