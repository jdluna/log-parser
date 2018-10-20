package com.whub.parser.persistence.repo;

import com.whub.parser.persistence.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
