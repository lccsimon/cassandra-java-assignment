package org.gcs.cassandra.service.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.gcs.cassandra.dao.Bird;

public class SearchResultResponse {

	private UUID locationId;

	private LocalDateTime scanTime;

	private Double latitude;

	private Double longitude;

	private String name;

	private List<Bird> birds;

	public SearchResultResponse(UUID locationId, LocalDateTime scanTime) {
		this.locationId = locationId;
		this.scanTime = scanTime;

		birds = new ArrayList<Bird>();
	}

	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	public LocalDateTime getScanTime() {
		return scanTime;
	}

	public void setScanTime(LocalDateTime scanTime) {
		this.scanTime = scanTime;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean addBird(Bird bird) {
		return this.birds.add(bird);
	}

	public List<Bird> getBirds() {
		return birds;
	}

}
