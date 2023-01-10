package org.gcs.cassandra.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ScanRepository extends CrudRepository<Scan, UUID> {
	List<Scan> findByLocationId(UUID locationId);

	List<Scan> findByScanTime(LocalDateTime scanTime);
}
