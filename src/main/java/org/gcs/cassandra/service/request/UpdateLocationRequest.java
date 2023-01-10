package org.gcs.cassandra.service.request;

import java.util.UUID;

public class UpdateLocationRequest {

	private UUID locationId;
	private Double latitude;
	private Double longitude;
	private String name;
	private String userId;

	public UpdateLocationRequest(UUID locationId, Double latitude, Double longitude, String name, String userId) {
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
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

}
