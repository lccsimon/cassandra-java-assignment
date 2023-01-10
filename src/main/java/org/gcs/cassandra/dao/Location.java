package org.gcs.cassandra.dao;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Location {

	public static final String STATUS_AVAILABLE = "A";
	public static final String STATUS_DISABLE = "D";

	@PrimaryKey
	private UUID locationId;

	private Double latitude;

	private Double longitude;

	private String name;

	private String status;

	private String createBy;

	private LocalDateTime createDate;

	private String updateBy;

	private LocalDateTime updateDate;

	public Location(UUID locationId, Double latitude, Double longitude, String name, String status, String createBy,
			LocalDateTime createDate, String updateBy, LocalDateTime updateDate) {
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.status = status;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
	}

	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(latitude, other.latitude) && Objects.equals(longitude, other.longitude);
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("locationId = ").append(locationId).append(",").append("latitude = ").append(latitude).append(",")
				.append("longitude = ").append(longitude).append(",").append("name = ").append(name).append(",")
				.append("status = ").append(status).append(",").append("createBy = ").append(createBy).append(",")
				.append("createDate = ").append(createDate).append(",").append("updateBy = ").append(updateBy)
				.append(",").append("updateDate = ").append(updateDate);

		return sb.toString();
	}
}
