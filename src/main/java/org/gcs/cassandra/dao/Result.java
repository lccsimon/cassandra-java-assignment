package org.gcs.cassandra.dao;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Result {

	@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
	private UUID scanId;

	@PrimaryKeyColumn
	private UUID birdId;

	public Result(UUID scanId, UUID birdId) {
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

	@Override
	public int hashCode() {
		return Objects.hash(birdId, scanId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		return Objects.equals(birdId, other.birdId) && Objects.equals(scanId, other.scanId);
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("scanId = ").append(this.getScanId()).append(",").append("birdId = ").append(this.getBirdId());

		return sb.toString();
	}
}
