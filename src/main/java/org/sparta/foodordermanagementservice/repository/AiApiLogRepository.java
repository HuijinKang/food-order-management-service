package org.sparta.foodordermanagementservice.repository;

import org.sparta.foodordermanagementservice.entity.AiApiLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiApiLogRepository extends JpaRepository<AiApiLog, UUID> {
}
