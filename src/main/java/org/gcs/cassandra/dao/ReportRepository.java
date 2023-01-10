package org.gcs.cassandra.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ReportRepository extends CassandraRepository<Report, UUID> {
	Optional<Report> findByScanIdAndLocationIdAndBirdId(UUID scanId, UUID locationId, UUID birdId);

	List<Report> findByScanIdAndLocationId(UUID scanId, UUID locationId);
}
