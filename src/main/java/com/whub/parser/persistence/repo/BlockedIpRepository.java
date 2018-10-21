package com.whub.parser.persistence.repo;

import com.whub.parser.persistence.entity.BlockedIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedIpRepository extends JpaRepository<BlockedIp, String> {
}
