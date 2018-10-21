package com.whub.parser.persistence.repo;

import com.whub.parser.app.dbdata.IpCountData;
import com.whub.parser.persistence.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    @Query("SELECT new com.whub.parser.app.dbdata.IpCountData(ipAddress, count(ipAddress))" +
            " FROM AccessLog" +
            " WHERE logDate BETWEEN :startDateTime AND :endDateTime" +
            " GROUP BY ipAddress" +
            " HAVING count(ipAddress) > :threshold")
    public List<IpCountData> findIpExceedingThreshold(@Param("startDateTime") LocalDateTime startDateTime,
                                                      @Param("endDateTime") LocalDateTime endDateTime,
                                                      @Param("threshold") Long threshold);
}
