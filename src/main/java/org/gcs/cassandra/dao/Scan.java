package org.gcs.cassandra.dao;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Scan {

	@PrimaryKey
	private UUID scanId;

	private UUID locationId;

	private LocalDateTime scanTime;

	public Scan(UUID scanId, UUID locationId, LocalDateTime scanTime) {
		this.scanId = scanId;
		this.locationId = locationId;
		this.scanTime = scanTime;
	}

	public UUID getScanId() {
		return scanId;
	}

	public void setScanId(UUID scanId) {
		this.scanId = scanId;
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

	@Override
	public int hashCode() {
		return Objects.hash(locationId, scanTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scan other = (Scan) obj;
		return Objects.equals(locationId, other.locationId) && Objects.equals(scanTime, other.scanTime);
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("scanId = ").append(scanId).append(",").append("locationId = ").append(locationId).append(",")
				.append("scanTime = ").append(scanTime);

		return sb.toString();
	}
}
