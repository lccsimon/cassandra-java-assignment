package org.gcs.cassandra.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gcs.cassandra.dao.Location;
import org.gcs.cassandra.dao.LocationRepository;
import org.gcs.cassandra.service.LocationService;
import org.gcs.cassandra.service.request.AddLocationRequest;
import org.gcs.cassandra.service.request.DeleteLocationRequest;
import org.gcs.cassandra.service.request.UpdateLocationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locationManage")
public class LocationManageController {

	private final static Logger log = LoggerFactory.getLogger(LocationManageController.class);

	private final LocationService locationService;

	LocationManageController(LocationRepository locationRespository) {

		this.locationService = new LocationService(locationRespository);
	}

	// To be simple, just list all locations without any searching criteria and
	// paging
	// It should be called to obtain Location ID first for operation such as Delete
	// & Update
	@GetMapping("/searchLocation")
	public List<Location> searchLocation() {
		return locationService.searchAllLocation();
	}

	// To be simple, just return a result message for this calling
	// User should call the searchLocation function to check the detail
	@PostMapping(value = "/updateLocation", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String updateLocation(@RequestBody UpdateLocationRequest updateLocationRequest) {

		// Read the request parameters
		UUID locationId = updateLocationRequest.getLocationId();
		Double latitude = updateLocationRequest.getLatitude();
		Double longitude = updateLocationRequest.getLongitude();
		String name = updateLocationRequest.getName();
		String userId = updateLocationRequest.getUserId();

		// Print out the parameters for debug purpose
		log.debug("updateLocation triggered, locationId = {}, latitude = {}, longitude = {}, name = {}, userId = {}",
				locationId, latitude, longitude, name, userId);

		// Request must have locationId and userId
		if (locationId == null || userId == null) {
			log.warn("updateLocation triggered without necessary information: locationId = {}, userId = {}", locationId,
					userId);
			return "Missing information";
		}

		Optional<Location> result = locationService.updateLocation(locationId, latitude, longitude, name, userId);

		if (result.isPresent()) {
			return "Update completed";
		} else {
			return "Update failed";
		}
	}

	// To be simple, just return a result message for this calling
	// User should call the searchLocation function to check the detail
	@PostMapping(value = "/addLocation", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addLocation(@RequestBody AddLocationRequest addLocationRequest) {

		// Read the request parameters
		Double latitude = addLocationRequest.getLatitude();
		Double longitude = addLocationRequest.getLongitude();
		String name = addLocationRequest.getName();
		String userId = addLocationRequest.getUserId();

		// Print out the parameters for debug purpose
		log.debug("addLocation triggered, latitude = {}, longitude = {}, name = {}, userId = {}", latitude, longitude,
				name, userId);

		// Request must have latitude, longitude and userId
		if (latitude == null || longitude == null || userId == null) {
			log.warn("addLocation triggered without necessary information: latitude = {}, longitude = {}, userId = {}",
					latitude, longitude, userId);
			return "Missing information";
		}

		Optional<Location> result = locationService.addLocation(latitude, longitude, name, userId);

		if (result.isPresent()) {
			return "Add completed or location already exists, birdId=" + result.get().getLocationId();
		} else {
			return "Add failed";
		}
	}
	// To be simple, just return a result message for this calling
	// User should call the searchLocation function to check the detail
	@PostMapping(value = "/deleteLocation", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String deleteLocation(@RequestBody DeleteLocationRequest deleteLocationRequest) {

		// Read the request parameters
		UUID locationId = deleteLocationRequest.getLocationId();
		String userId = deleteLocationRequest.getUserId();

		// Print out the parameters for debug purpose
		log.debug("deleteLocation triggered, locationId = {} userId = {}", locationId, userId);

		// Request must have locationId and userId
		if (locationId == null || userId == null) {
			log.warn("deleteLocation triggered without necessary information: locationId = {}, userId = {}", locationId,
					userId);
			return "Missing information";
		}

		Optional<Location> result = locationService.deleteLocation(locationId, userId);

		if (result.isPresent()) {
			return "Delete completed";
		} else {
			return "Delete failed";
		}
	}
}
