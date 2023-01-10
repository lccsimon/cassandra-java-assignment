package org.gcs.cassandra.service.request;

import java.time.LocalDateTime;

public class SearchLocationByScanDateRequest {

	private LocalDateTime scanTime;

	private String userId;

	public SearchLocationByScanDateRequest(LocalDateTime scanTime, String userId) {
		this.scanTime = scanTime;
		this.userId = userId;
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
