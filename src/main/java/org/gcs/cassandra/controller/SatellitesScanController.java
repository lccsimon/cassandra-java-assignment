package org.gcs.cassandra.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.gcs.cassandra.dao.Bird;
import org.gcs.cassandra.dao.BirdRepository;
import org.gcs.cassandra.dao.Location;
import org.gcs.cassandra.dao.LocationRepository;
import org.gcs.cassandra.dao.Report;
import org.gcs.cassandra.dao.ReportRepository;
import org.gcs.cassandra.dao.Result;
import org.gcs.cassandra.dao.ResultRepository;
import org.gcs.cassandra.dao.Scan;
import org.gcs.cassandra.dao.ScanRepository;
import org.gcs.cassandra.service.BirdService;
import org.gcs.cassandra.service.LocationService;
import org.gcs.cassandra.service.ReportService;
import org.gcs.cassandra.service.ResultService;
import org.gcs.cassandra.service.ScanService;
import org.gcs.cassandra.service.request.AddBirdRequest;
import org.gcs.cassandra.service.request.AddScanRequest;
import org.gcs.cassandra.service.request.AddScanResultRequest;
import org.gcs.cassandra.service.request.AddScanResultSingleCallRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/satellitesScan")
public class SatellitesScanController {

	private final static Logger log = LoggerFactory.getLogger(SatellitesScanController.class);

	private final LocationService locationService;
	private final ScanService scanService;
	private final ResultService resultService;
	private final BirdService birdService;
	private final ReportService reportService;

	SatellitesScanController(LocationRepository locationRespository, ScanRepository scanRespository,
			ResultRepository resultRepository, BirdRepository birdRepository, ReportRepository reportRepository) {

		this.locationService = new LocationService(locationRespository);
		this.scanService = new ScanService(scanRespository);
		this.resultService = new ResultService(resultRepository);
		this.birdService = new BirdService(birdRepository);
		this.reportService = new ReportService(reportRepository);
	}

	// To be simple, just list all locations without any searching criteria and
	// paging
	// Satellite should call this function to obtain list of Locations before
	// scanning
	@GetMapping("/searchLocation")
	public List<Location> searchLocation() {
		return locationService.searchAllLocation();
	}

	// To be simple, just return a result message for this calling
	// Satellite should call this function before creating bird and result records
	@PostMapping(value = "/addNewScan", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addNewScan(@RequestBody AddScanRequest addScanRequest) {

		// Read the request parameters
		UUID locationId = addScanRequest.getLocationId();
		LocalDateTime scanTime = addScanRequest.getScanTime();

		// Print out the parameters for debug purpose
		log.debug("addNewScan triggered, locationId = {}, scanTime = {}", locationId, scanTime);

		// Request must have locationId and scanTime
		if (locationId == null || scanTime == null) {
			log.warn("addNewScan triggered without necessary information: locationId = {}, scanTime = {}", locationId,
					scanTime);
			return "Missing information";
		}

		// Check if location exists
		if (locationService.searchLocationByLocationId(locationId).isEmpty()) {
			log.warn("addNewScan triggered with invalid location: locationId = {}", locationId);
			return "Invalid location";
		}

		// Remove time information from scanTime, only keep date information
		// Should modify logic here if satellites can support hourly scanning later
		LocalDateTime scanDate = scanTime.truncatedTo(ChronoUnit.DAYS);

		// Print out scanDate for debug purpose
		log.debug("scanDate = {}", scanDate);

		Optional<Scan> result = scanService.addScan(locationId, scanDate);

		if (result.isPresent()) {
			return "Add completed, scanID=" + result.get().getScanId();
		} else {
			return "Add failed";
		}
	}

	// Assume satellites can store bird information and identify bird uniquely
	// (Satellites generate birdId for us)
	// Satellite should call this function when new bird is found, birdId from
	// satellites will be used for insert result records later
	@PostMapping(value = "/addNewBird", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addNewBird(@RequestBody AddBirdRequest AddBirdRequest) {

		// Read the request parameters
		UUID birdId = AddBirdRequest.getBirdId();
		String species = AddBirdRequest.getSpecies();
		Set<String> traits = AddBirdRequest.getTraits();

		// Print out the parameters for debug purpose
		log.debug("addNewBird triggered, birdId = {}, species = {}, traits = {}", birdId, species, traits);

		// Request must have species and traits
		if (birdId == null || species == null || traits == null || traits.isEmpty()) {
			log.warn("addNewBird triggered without necessary information: birdId = {}, species = {}, traits = {}",
					birdId, species, traits);
			return "Missing information";
		}

		Optional<Bird> result = birdService.addBird(birdId, species, traits);

		if (result.isPresent()) {
			return "Add completed or bird already exists, birdId=" + result.get().getBirdId();
		} else {
			return "Add failed";
		}
	}

	// Assume satellites only send result information to us when scanId and birdId
	// are ready)
	// Satellite should call this function to insert scan result
	@PostMapping(value = "/addScanResult", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addScanResult(@RequestBody AddScanResultRequest addscanResultRequest) {

		// Read the request parameters
		UUID scanId = addscanResultRequest.getScanId();
		UUID birdId = addscanResultRequest.getBirdId();

		// Print out the parameters for debug purpose
		log.debug("addScanResult triggered, scanID = {}, birdId = {}", scanId, birdId);

		// Request must have scanId and birdId
		if (scanId == null || birdId == null) {
			log.warn("addScanResult triggered without necessary information: scanID = {}, birdId = {}", scanId, birdId);
			return "Missing information";
		}

		// Check if scanId exists
		Optional<Scan> targetScan = scanService.searchScanByScanId(scanId);
		Optional<Bird> targetBird = birdService.searchBirdByBirdId(birdId);

		// ScanId and BirdId must be valid
		if (targetScan.isEmpty()) {
			return "Invalid scan information";
		} else if (targetBird.isEmpty()) {
			return "Invalid bird information";
		}

		Optional<Location> targetLocation = locationService
				.searchLocationByLocationId(targetScan.get().getLocationId());

		// LocationId must be valid
		if (targetLocation.isEmpty()) {
			return "Invalid location information";
		}

		// Insert result data for scientists
		Optional<Result> result = resultService.addResult(scanId, birdId);

		// Insert report data for scientists
		Optional<Report> report = reportService.addReport(scanId, birdId, targetLocation.get().getLocationId(),
				targetScan.get().getScanTime(), targetLocation.get().getLatitude(), targetLocation.get().getLongitude(),
				targetBird.get().getSpecies(), targetBird.get().getTraits());

		if (result.isPresent() && report.isPresent()) {
			return "Add completed";
		} else {
			return "Add failed";
		}
	}

	// Assume satellites send result information to us in one single call
	// Satellite should call this function to insert scan result
	@PostMapping(value = "/addScanResultSingleCall", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String addScanResultSingleCall(@RequestBody AddScanResultSingleCallRequest addScanResultSingleCallRequest) {

		// Read the request parameters
		UUID birdId = addScanResultSingleCallRequest.getBirdId();
		Double latitude = addScanResultSingleCallRequest.getLatitude();
		Double longitude = addScanResultSingleCallRequest.getLongitude();
		LocalDateTime scanTime = addScanResultSingleCallRequest.getScanTime();
		String species = addScanResultSingleCallRequest.getSpecies();
		Set<String> traits = addScanResultSingleCallRequest.getTraits();

		// Print out the parameters for debug purpose
		log.debug(
				"addScanResultSingleCall triggered, birdId = {}, latitude = {}, longitude = {}, scanTime = {}, species = {}, traits = {}",
				birdId, latitude, longitude, scanTime, species, traits);

		// Request must have scanId and birdId
		if (birdId == null || latitude == null || longitude == null || scanTime == null || species == null
				|| traits == null) {
			log.warn("addScanResultSingleCall triggered without necessary information: scanID = {}, birdId = {}",
					birdId, latitude, longitude, scanTime, species, traits);
			return "Missing information";
		}

		// Check if related information exists
		Optional<Location> targetLocation = locationService.searchLocationByLatitudeAndLongitude(latitude, longitude);

		// LocationId must be valid
		if (targetLocation.isEmpty()) {
			return "Invalid location information";
		}
		
		// Remove time information from scanTime, only keep date information
		// Should modify logic here if satellites can support hourly scanning later
		LocalDateTime scanDate = scanTime.truncatedTo(ChronoUnit.DAYS);		

		Optional<Scan> targetScan = scanService.searchScansByLocationIdAndScanDate(targetLocation.get().getLocationId(),
				scanDate);
		Optional<Bird> targetBird = birdService.searchBirdByBirdId(birdId);

		log.debug("Before: targetScan.isEmpty() = {}, targetBird.isEmpty() = {}", targetScan.isEmpty(), targetBird.isEmpty());
		
		// Create Scan if not exist
		if (targetScan.isEmpty()) {
			targetScan = scanService.addScan(targetLocation.get().getLocationId(), scanDate);
		}

		// Create Bird if not exist
		if (targetBird.isEmpty()) {
			targetBird = birdService.addBird(birdId, species, traits);
		}

		log.debug("After: targetScan.isEmpty() = {}, targetBird.isEmpty() = {}", targetScan.isEmpty(), targetBird.isEmpty());

		// Double checking in case creation is not success
		if (targetScan.isEmpty() || targetBird.isEmpty()) {
			return "Failed to create scan or bird";
		}

		// Insert result data for scientists
		Optional<Result> result = resultService.addResult(targetScan.get().getScanId(), birdId);

		// Insert report data for scientists
		Optional<Report> report = reportService.addReport(targetScan.get().getScanId(),
				birdId, targetLocation.get().getLocationId(), targetScan.get().getScanTime(),
				targetLocation.get().getLatitude(), targetLocation.get().getLongitude(), targetBird.get().getSpecies(),
				targetBird.get().getTraits());

		if (result.isPresent() && report.isPresent()) {
			return "Add completed";
		} else {
			return "Add failed";
		}
	}
}
