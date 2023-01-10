package org.gcs.cassandra.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gcs.cassandra.dao.Scan;
import org.gcs.cassandra.dao.ScanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScanService {

	private final static Logger log = LoggerFactory.getLogger(ScanService.class);

	private ScanRepository scanRespository;

	// constructor of scan service
	public ScanService(ScanRepository scanRespository) {

		this.scanRespository = scanRespository;
	}

	// Create scan in DB if scan record (locationId & scanTime) does not exist
	public Optional<Scan> addScan(UUID locationId, LocalDateTime scanDate) {

		// Prepare new scan object
		Scan newScan = new Scan(UUID.randomUUID(), locationId, scanDate);

		// Check if scan exists, obtain list of scan by scanDate for filtering
		List<Scan> searchResults = scanRespository.findByScanTime(scanDate);

		searchResults.forEach(v -> {
			log.debug("searchResults list contains v: {}", v);
		});

		// then use equals method of scan to check exist or not
		if (searchResults.contains(newScan)) {

			// Scan already existed, do nothing
			return Optional.of(searchResults.get(searchResults.indexOf(newScan)));
		} else {

			// create scan and return
			return Optional.of(scanRespository.save(newScan));
		}
	}

	// search scan in DB by scanTime & locationId
	public Optional<Scan> searchScansByLocationIdAndScanDate(UUID locationId, LocalDateTime scanDate) {

		List<Scan> searchResult = scanRespository.findByScanTime(scanDate);

		for (int i = 0; i < searchResult.size(); i++) {

			Scan tmpScan = searchResult.get(i);

			// return target scan information (Assume only 1 scan for each location per
			// date)
			if (tmpScan.getLocationId().equals(locationId)) {

				log.debug("searchScansByLocationIdAndScanDate() triggered, tmpScan = {}", tmpScan);

				return Optional.of(tmpScan);
			}
		}

		return Optional.empty();
	}

	// search scans in DB by scanTime
	public List<Scan> searchScansByScanDate(LocalDateTime scanDate) {

		List<Scan> scanList = scanRespository.findByScanTime(scanDate);

		log.debug("searchScansByScanDate() triggered, total {} available scans found", scanList.size());

		return scanList;
	}

	// search scans in DB by scanTime & locationId
	public List<Scan> searchScansByLocationId(UUID locationId) {

		List<Scan> scanList = scanRespository.findByLocationId(locationId);

		log.debug("searchScansByLocationId() triggered, total {} available scans found", scanList.size());

		return scanList;
	}

	// search scan in DB by scanId
	public Optional<Scan> searchScanByScanId(UUID scanId) {

		return scanRespository.findById(scanId);
	}
}
