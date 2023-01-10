package org.gcs.cassandra.service.request;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class AddScanResultSingleCallRequest {

	private UUID birdId;
	private Double latitude;
	private Double longitude;
	private LocalDateTime scanTime;
	private String species;
	private Set<String> traits;

	public AddScanResultSingleCallRequest(UUID birdId, Double latitude, Double longitude, LocalDateTime scanTime,
			String species, Set<String> traits) {
		this.birdId = birdId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.scanTime = scanTime;
		this.species = species;
		this.traits = traits;
	}

	public UUID getBirdId() {
		return birdId;
	}

	public void setBirdId(UUID birdId) {
		this.birdId = birdId;
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

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public Set<String> getTraits() {
		return traits;
	}

	public void setTraits(Set<String> traits) {
		this.traits = traits;
	}

}
