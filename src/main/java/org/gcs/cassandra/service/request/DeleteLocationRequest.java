package org.gcs.cassandra.service.request;

import java.util.UUID;

public class DeleteLocationRequest {

	private UUID locationId;
	private String userId;

	public DeleteLocationRequest(UUID locationId, String userId) {
		this.locationId = locationId;
		this.userId = userId;
	}

	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
