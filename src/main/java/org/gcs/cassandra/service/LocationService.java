package org.gcs.cassandra.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gcs.cassandra.dao.Location;
import org.gcs.cassandra.dao.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationService {

	private final static Logger log = LoggerFactory.getLogger(LocationService.class);

	private LocationRepository locationRespository;

	// constructor of location service
	public LocationService(LocationRepository locationRespository) {

		this.locationRespository = locationRespository;
	}

	// search all location in DB that status = "A" (Available)
	public List<Location> searchAllLocation() {

		ArrayList<Location> locationList = new ArrayList<Location>();

		locationRespository.findAll().forEach(v -> {
			if (v.getStatus().equals(Location.STATUS_AVAILABLE)) {
				locationList.add(v);
			}
		});

		log.debug("searchAllLocation() triggered, total {} available locations found", locationList.size());

		return locationList;
	}

	// search location in DB by Latitude & Longitude
	public Optional<Location> searchLocationByLatitudeAndLongitude(Double latitude, Double longitude) {

		List<Location> searchResult = locationRespository.findByLatitude(latitude);

		for (int i = 0; i < searchResult.size(); i++) {

			Location tmpLocation = searchResult.get(i);

			// return target location (Assume latitude & longitude can uniquely definite a
			// location)
			if (tmpLocation.getLongitude().equals(longitude)) {

				log.debug("searchLocationByLatitudeAndLongitude() triggered, tmpLocation = {}", tmpLocation);

				return Optional.of(tmpLocation);
			}
		}

		return Optional.empty();
	}

	// search location in DB by locationId, no filtering on status as location maybe
	// updated (marked disable) already
	public Optional<Location> searchLocationByLocationId(UUID locationId) {

		return locationRespository.findById(locationId);
	}

	// Update location if UUID exist
	// Create location in DB if not
	public Optional<Location> updateLocation(UUID locationId, Double latitude, Double longitude, String name,
			String userId) {

		// Check if location exists or not
		Optional<Location> searchResult = locationRespository.findById(locationId);

		// Continue process if location exists
		if (searchResult.isPresent()) {

			Location tempLocation = searchResult.get();

			// Continue process if location's status = "A"
			if (tempLocation.getStatus().equals(Location.STATUS_AVAILABLE)) {

				// Assume 2 locations are the same if they have the same latitude & longitude
				if ((latitude == null && longitude == null)
						|| tempLocation.getLatitude() == latitude && tempLocation.getLongitude() == longitude) {
					// Modify name only, continue update
					tempLocation.setName(name);
					tempLocation.setUpdateBy(userId);
					tempLocation.setUpdateDate(LocalDateTime.now());

					log.debug("only name is updated, tempLocation = {}", tempLocation);

					return Optional.of(locationRespository.save(tempLocation));

				} else {

					// Disable existing location as user wants to update latitude & longitude
					tempLocation.setStatus(Location.STATUS_DISABLE);
					tempLocation.setUpdateBy(userId);
					tempLocation.setUpdateDate(LocalDateTime.now());

					locationRespository.save(tempLocation);

					// Protect existing data, create a new location instead according to user's
					// request
					Location newLocation = new Location(UUID.randomUUID(),
							latitude == null ? tempLocation.getLatitude() : latitude,
							longitude == null ? tempLocation.getLongitude() : longitude, name,
							Location.STATUS_AVAILABLE, userId, LocalDateTime.now(), userId, LocalDateTime.now());

					log.debug("new location = {} is created, previous location = {} is disabled", newLocation,
							tempLocation);

					return Optional.of(locationRespository.save(newLocation));
				}
			}
		}

		return Optional.empty();
	}

	// Create location in DB if location (latitude & longitude) does not exist
	// Return existing location if location with same latitude & longitude is found
	public Optional<Location> addLocation(double latitude, double longitude, String name, String userId) {

		// Prepare new location object
		Location newLocation = new Location(UUID.randomUUID(), latitude, longitude, name, Location.STATUS_AVAILABLE,
				userId, LocalDateTime.now(), userId, LocalDateTime.now());

		// Check if location exists, obtain list of location by latitude for filtering
		List<Location> searchResults = locationRespository.findByLatitude(latitude);

		// then use equals method of Location to check exist or not
		if (searchResults.contains(newLocation)) {

			// location already existed, do nothing
			return Optional.of(searchResults.get(searchResults.indexOf(newLocation)));
		} else {

			// create location and return
			return Optional.of(locationRespository.save(newLocation));
		}
	}

	// Create location in DB if location (latitude & longitude) does not exist
	public Optional<Location> deleteLocation(UUID locationId, String userId) {

		// Check if location exists or not
		Optional<Location> searchResult = locationRespository.findById(locationId);

		// Continue process if location exists
		if (searchResult.isPresent()) {

			Location tempLocation = searchResult.get();

			// Continue process if location's status = "A"
			if (tempLocation.getStatus().equals(Location.STATUS_AVAILABLE)) {

				// Disable the location
				tempLocation.setStatus(Location.STATUS_DISABLE);
				tempLocation.setUpdateBy(userId);
				tempLocation.setUpdateDate(LocalDateTime.now());

				log.debug("location = {} is disabled", tempLocation);

				// Return the result location
				return Optional.of(locationRespository.save(tempLocation));
			}
		}

		// location does not existed, do nothing
		return Optional.empty();
	}
}
