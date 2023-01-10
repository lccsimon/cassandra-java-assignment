package org.gcs.cassandra.service.request;

import java.time.LocalDateTime;

public class SearchResultSingleCallRequest {

	private Double latitude;

	private Double longitude;

	private LocalDateTime scanTime;

	private String userId;

	public SearchResultSingleCallRequest(Double latitude, Double longitude, LocalDateTime scanTime, String userId) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.scanTime = scanTime;
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

	public LocalDateTime getScanTime() {
		return scanTime;
	}

	public void setScanTime(LocalDateTime scanTime) {
		this.scanTime = scanTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
