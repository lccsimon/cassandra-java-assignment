package org.gcs.cassandra.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.gcs.cassandra.dao.Report;
import org.gcs.cassandra.dao.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportService {

	private final static Logger log = LoggerFactory.getLogger(ReportService.class);

	private ReportRepository reportRespository;

	// constructor of report service
	public ReportService(ReportRepository reportRespository) {

		this.reportRespository = reportRespository;
	}

	// Create report in DB if report record (scanId, locationId, birdId) does not
	// exist
	public Optional<Report> addReport(UUID scanId, UUID birdId, UUID locationId, LocalDateTime scanTime,
			Double latitude, Double longitude, String species, Set<String> traits) {

		// Prepare new result object
		Report newReport = new Report(scanId, birdId, locationId, scanTime, latitude, longitude, species, traits);

		// Check if report exists, obtain report by scanId, locationId, birdId
		Optional<Report> searchResults = reportRespository.findByScanIdAndLocationIdAndBirdId(scanId, locationId,
				birdId);

		// Print out the report for debug purpose
		log.debug("searchResults = {}", searchResults);

		// Check report exist or not
		if (searchResults.isPresent()) {

			// Report already existed, do nothing
			return Optional.empty();
		} else {

			// Create report and return
			return Optional.of(reportRespository.save(newReport));
		}
	}

	// Search report in DB with scanId, locationId and birdId
	public Optional<Report> searchReportByScanIdAndLocationIdAndBirdId(UUID scanId, UUID locationId, UUID birdId) {

		// Obtain report by scanId, locationId and birdId
		return reportRespository.findByScanIdAndLocationIdAndBirdId(scanId, locationId, birdId);

	}

	// Search report in DB with scanId and locationId
	public List<Report> searchReportByScanIdAndLocationId(UUID scanId, UUID locationId) {

		// Obtain report by scanId and locationId
		return reportRespository.findByScanIdAndLocationId(scanId, locationId);

	}
}
