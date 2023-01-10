package org.gcs.cassandra.service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.gcs.cassandra.dao.Bird;
import org.gcs.cassandra.dao.BirdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BirdService {

	private final static Logger log = LoggerFactory.getLogger(BirdService.class);

	private BirdRepository birdRespository;

	// constructor of bird service
	public BirdService(BirdRepository birdRespository) {

		this.birdRespository = birdRespository;
	}

	// Create bird in DB if bird record (birdId) does not exist
	// Return existing bird if bird with same birdId is found
	public Optional<Bird> addBird(UUID birdId, String species, Set<String> traits) {

		// Prepare new result object
		Bird newBird = new Bird(birdId, species, traits);

		// Check if bird exists, obtain bird by birdId
		Optional<Bird> searchResults = birdRespository.findById(birdId);

		// Print out the bird for debug purpose
		log.debug("searchResults = {}", searchResults);

		// Check bird exist or not
		if (searchResults.isPresent()) {

			// Bird already existed, do nothing
			return searchResults;
		} else {

			// Create bird and return
			return Optional.of(birdRespository.save(newBird));
		}
	}

	// Search bird in DB with birdId
	public Optional<Bird> searchBirdByBirdId(UUID birdId) {

		// Obtain bird by birdId
		return birdRespository.findById(birdId);

	}

}
