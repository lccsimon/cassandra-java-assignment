package org.gcs.cassandra.dao;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Report {

	@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
	private UUID scanId;

	@PrimaryKeyColumn
	private UUID locationId;

	@PrimaryKeyColumn
	private UUID birdId;

	private LocalDateTime scanTime;

	private Double latitude;

	private Double longitude;

	private String species;

	private Set<String> traits;

	public Report(UUID scanId, UUID birdId, UUID locationId, LocalDateTime scanTime, Double latitude, Double longitude,
			String species, Set<String> traits) {
		this.scanId = scanId;
		this.birdId = birdId;
		this.locationId = locationId;
		this.scanTime = scanTime;
		this.latitude = latitude;
		this.longitude = longitude;
		this.species = species;
		this.traits = traits;
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
