package org.gcs.cassandra.service.request;

import java.util.UUID;

public class AddScanResultRequest {

	private UUID scanId;

	private UUID birdId;

	public AddScanResultRequest(UUID scanId, UUID birdId) {
		this.scanId = scanId;
		this.birdId = birdId;
	}

	public UUID getScanId() {
		return scanId;
	}

	public void setScanId(UUID scanId) {
		this.scanId = scanId;
	}

	public UUID getBirdId() {
		return birdId;
	}

	public void setBirdId(UUID birdId) {
		this.birdId = birdId;
	}

}
