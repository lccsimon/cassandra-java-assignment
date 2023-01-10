package org.gcs.cassandra.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.gcs.cassandra.service.request.SearchLocationByScanDateRequest;
import org.gcs.cassandra.service.request.SearchResultRequest;
import org.gcs.cassandra.service.request.SearchResultSingleCallRequest;
import org.gcs.cassandra.service.response.SearchResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resultRead")
public class ResultReadController {

	private final static Logger log = LoggerFactory.getLogger(ResultReadController.class);

	private final LocationService locationService;
	private final ScanService scanService;
	private final ResultService resultService;
	private final BirdService birdService;
	private final ReportService reportService;

	ResultReadController(LocationRepository locationRespository, ScanRepository scanRespository,
			ResultRepository resultRepository, BirdRepository birdRepository, ReportRepository reportRepository) {

		this.locationService = new LocationService(locationRespository);
		this.scanService = new ScanService(scanRespository);
		this.resultService = new ResultService(resultRepository);
		this.birdService = new BirdService(birdRepository);
		this.reportService = new ReportService(reportRepository);
	}

	// To be simple, just list all locations by scan date without any searching
	// criteria and paging
	// Scientists can call this function to obtain list of Locations before
	// retrieving the detail scan result of each location
	@PostMapping(value = "/searchLocationByScanDate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Location> searchLocationByScanDate(
			@RequestBody SearchLocationByScanDateRequest searchLocationByScanDateRequest) {

		// Read the request parameters
		LocalDateTime scanTime = searchLocationByScanDateRequest.getScanTime();
		String userId = searchLocationByScanDateRequest.getUserId(); // logging purpose only

		// Print out the parameters for debug purpose
		log.debug("searchLocationByScanDate triggered, scanTime = {}, userId = {}", scanTime, userId);

		// Request must have scanTime
		if (scanTime == null) {

			// Print out missing scan time message
			log.warn("searchLocationByScanDate triggered without necessary information: scanTime = {}, userId = {}",
					scanTime, userId);

			// Return EMPTY list
			return new ArrayList<Location>();
		}

		// Remove time information from scanTime, only keep date information
		// Should modify logic here if satellites can support hourly scanning later
		LocalDateTime scanDate = scanTime.truncatedTo(ChronoUnit.DAYS);

		// Print out scanDate for debug purpose
		log.debug("scanDate = {}", scanDate);

		// Obtain scans data by scan date first
		List<Scan> scans = scanService.searchScansByScanDate(scanDate);

		// Obtain list of Location that performed scan on requested scan date
		List<Location> locations = new ArrayList<Location>();

		// Add Location to ArrayList
		scans.forEach(v -> {
			Optional<Location> tmpLocation = locationService.searchLocationByLocationId(v.getLocationId());
			tmpLocation.ifPresent(l -> locations.add(l));
		});

		return locations;
	}

	// SearchResultResponse is returned according to request locationId & scanTime
	// Scientists should call this function to obtain scan result of
	// specified scan date and location
	@PostMapping(value = "/searchResult", consumes = MediaType.APPLICATION_JSON_VALUE)
	public SearchResultResponse searchResult(@RequestBody SearchResultRequest searchResultRequest) {

		// Read the request parameters
		UUID locationId = searchResultRequest.getLocationId();
		LocalDateTime scanTime = searchResultRequest.getScanTime();
		String userId = searchResultRequest.getUserId(); // logging purpose only

		// Print out the parameters for debug purpose
		log.debug("searchResult triggered, locationId = {}, scanTime = {}, userId = {}", locationId, scanTime, userId);

		// Prepare response object
		SearchResultResponse searchResultResponse = new SearchResultResponse(locationId, scanTime);

		// Request must have locationId & scanTime
		if (locationId == null || scanTime == null) {

			// Print out missing locationId / scanTime message
			log.warn(
					"searchResult triggered without necessary information: locationId = {}, scanTime = {}, userId = {}",
					locationId, scanTime, userId);

			// Return SearchResultResponse object with request locationId and scanTime only
			return searchResultResponse;
		}

		// Remove time information from scanTime, only keep date information
		// Should modify logic here if satellites can support hourly scanning later
		LocalDateTime scanDate = scanTime.truncatedTo(ChronoUnit.DAYS);

		// Set the truncated scan date in response object
		searchResultResponse.setScanTime(scanDate);

		// Obtain scans data by scan date first
		List<Scan> scans = scanService.searchScansByScanDate(scanDate);

		// Check every scan record
		for (int i = 0; i < scans.size(); i++) {

			Scan v = scans.get(i);

			// Only use the record with correct locationId
			if (locationId.equals(v.getLocationId())) {

				// Prepare the response accordingly with location information
				Optional<Location> tmpLocation = locationService.searchLocationByLocationId(v.getLocationId());
				tmpLocation.ifPresent(l -> {

					searchResultResponse.setName(l.getName());
					searchResultResponse.setLatitude(l.getLatitude());
					searchResultResponse.setLongitude(l.getLongitude());

				});

				// Prepare the response accordingly with birds information
				List<Result> tmpResults = resultService.searchResultByScanId(v.getScanId());
				tmpResults.forEach(r -> {

					Optional<Bird> tmpBird = birdService.searchBirdByBirdId(r.getBirdId());

					// If bird exists, add it to the response
					tmpBird.ifPresent(b -> {
						searchResultResponse.addBird(b);
					});
				});

				// It is abnormal but OK to break even no location / bird information is found
				// in DB
				// In that case, location / bird related information will be EMPTY as data not
				// completed
				break;
			}
		}

		return searchResultResponse;
	}

	// SearchResultResponse is returned according to request location (latitude &
	// longitude) & scanTime
	// Assume scientists only input latitude & longitude & scanTime to obtain scan
	// result of
	// specified scan date and location
	@PostMapping(value = "/searchResultSingleCall", consumes = MediaType.APPLICATION_JSON_VALUE)
	public SearchResultResponse searchResultSingleCall(
			@RequestBody SearchResultSingleCallRequest searchResultSingleCallRequest) {

		// Read the request parameters
		Double latitude = searchResultSingleCallRequest.getLatitude();
		Double longitude = searchResultSingleCallRequest.getLongitude();
		LocalDateTime scanTime = searchResultSingleCallRequest.getScanTime();
		String userId = searchResultSingleCallRequest.getUserId(); // logging purpose only

		// Print out the parameters for debug purpose
		log.debug("searchResultSingleCall triggered, latitude = {}, longitude = {}, scanTime = {}, userId = {}",
				latitude, longitude, scanTime, userId);

		// Prepare response object (No locationId available yet, only add back all
		// requested fields for reference)
		SearchResultResponse searchResultResponse = new SearchResultResponse(null, scanTime);
		searchResultResponse.setLatitude(latitude);
		searchResultResponse.setLongitude(longitude);

		// Request must have locationId & scanTime
		if (latitude == null || longitude == null || scanTime == null) {

			// Print out missing latitude / longitude / scanTime message
			log.warn(
					"searchResultSingleCall triggered without necessary information: latitude = {}, longitude = {}, scanTime = {}, userId = {}",
					latitude, longitude, scanTime, userId);

			// Return SearchResultResponse object with request latitude, longitude and
			// scanTime only
			return searchResultResponse;
		}

		// Remove time information from scanTime, only keep date information
		// Should modify logic here if satellites can support hourly scanning later
		LocalDateTime scanDate = scanTime.truncatedTo(ChronoUnit.DAYS);

		// Set the truncated scan date in response object
		searchResultResponse.setScanTime(scanDate);

		// obtain location information
		Optional<Location> tmpLocation = locationService.searchLocationByLatitudeAndLongitude(latitude, longitude);

		if (tmpLocation.isEmpty()) {

			// Print out missing locationId message
			log.warn("searchResultSingleCall triggered without necessary information: tmpLocation = {}, userId = {}",
					tmpLocation, userId);

			// Return SearchResultResponse object with request latitude, longitude and
			// scanDate only
			return searchResultResponse;
		}

		// Set the locationId and name in response object
		searchResultResponse.setLocationId(tmpLocation.get().getLocationId());
		searchResultResponse.setName(tmpLocation.get().getName());

		// obtain scan information
		Optional<Scan> tmpScan = scanService.searchScansByLocationIdAndScanDate(tmpLocation.get().getLocationId(),
				scanDate);

		if (tmpScan.isEmpty()) {

			// Print out missing locationId message
			log.warn("searchResultSingleCall triggered without necessary information: tmpScan = {}, userId = {}",
					tmpScan, userId);

			// Return SearchResultResponse object with request latitude, longitude and
			// scanDate only
			return searchResultResponse;
		}

		// Obtain scans data by scan date first
		List<Report> reports = reportService.searchReportByScanIdAndLocationId(tmpScan.get().getScanId(),
				tmpScan.get().getLocationId());

		// Add bird information
		reports.forEach(v -> {

			searchResultResponse.addBird(new Bird(v.getBirdId(), v.getSpecies(), v.getTraits()));
		});

		return searchResultResponse;
	}
}
