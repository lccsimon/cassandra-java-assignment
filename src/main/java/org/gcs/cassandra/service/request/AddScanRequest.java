package org.gcs.cassandra.service.request;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddScanRequest {

	private UUID locationId;

	private LocalDateTime scanTime;

	public AddScanRequest(UUID locationId, LocalDateTime scanTime) {
		this.locationId = locationId;
		this.scanTime = scanTime;
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

}
