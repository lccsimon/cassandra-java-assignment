package org.gcs.cassandra.dao;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Bird {

	@PrimaryKey
	private UUID birdId;

	private String species;

	private Set<String> traits;

	public Bird(UUID birdId, String species, Set<String> traits) {
		this.birdId = birdId;
		this.species = species;
		this.traits = traits;
	}

	public UUID getBirdId() {
		return birdId;
	}

	public void setBirdId(UUID birdId) {
		this.birdId = birdId;
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

	@Override
	public int hashCode() {
		return Objects.hash(birdId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bird other = (Bird) obj;
		return Objects.equals(birdId, other.birdId);
	}

}
