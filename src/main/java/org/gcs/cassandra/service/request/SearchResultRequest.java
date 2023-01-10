package org.gcs.cassandra.service.request;

import java.time.LocalDateTime;
import java.util.UUID;

public class SearchResultRequest {

	private UUID locationId;

	private LocalDateTime scanTime;

	private String userId;

	public SearchResultRequest(UUID locationId, LocalDateTime scanTime, String userId) {
		this.locationId = locationId;
		this.scanTime = scanTime;
		this.userId = userId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
