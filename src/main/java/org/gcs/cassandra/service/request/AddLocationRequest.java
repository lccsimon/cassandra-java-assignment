package org.gcs.cassandra.service.request;

public class AddLocationRequest {

	private Double latitude;
	private Double longitude;
	private String name;
	private String userId;

	public AddLocationRequest(Double latitude, Double longitude, String name, String userId) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.userId = userId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
