package org.gcs.cassandra.service.request;

import java.util.Set;
import java.util.UUID;

public class AddBirdRequest {

	private UUID birdId;

	private String species;

	private Set<String> traits;

	public AddBirdRequest(UUID birdId, String species, Set<String> traits) {
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

}
