package org.gcs.cassandra.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, UUID> {
	List<Location> findByLatitude(double latitude);

	List<Location> findByLongitude(double longitude);

	List<Location> findByName(String name);

	List<Location> findByStatus(String status);
}
