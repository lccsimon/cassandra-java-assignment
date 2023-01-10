package org.gcs.cassandra.dao;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface BirdRepository extends CrudRepository<Bird, UUID> {

}
