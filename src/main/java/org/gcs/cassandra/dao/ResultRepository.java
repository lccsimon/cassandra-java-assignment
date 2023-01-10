package org.gcs.cassandra.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ResultRepository extends CassandraRepository<Result, UUID> {
	Optional<Result> findByScanIdAndBirdId(UUID scanId, UUID birdId);

	List<Result> findByScanId(UUID scanId);

	List<Result> findByBirdId(UUID birdId);
}
