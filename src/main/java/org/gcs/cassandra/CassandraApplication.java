package org.gcs.cassandra;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gcs.cassandra.dao.Bird;
import org.gcs.cassandra.dao.BirdRepository;
import org.gcs.cassandra.dao.Location;
import org.gcs.cassandra.dao.LocationRepository;
import org.gcs.cassandra.dao.ReportRepository;
import org.gcs.cassandra.dao.Result;
import org.gcs.cassandra.dao.ResultRepository;
import org.gcs.cassandra.dao.Scan;
import org.gcs.cassandra.dao.ScanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CassandraApplication {

	private final static Logger log = LoggerFactory.getLogger(CassandraApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CassandraApplication.class, args);
	}

	// For testing and prepare testing data only
	@Bean
	public CommandLineRunner preLoadData(BirdRepository birdRepository, ScanRepository scanRepository,
			ResultRepository resultRepository, LocationRepository locationRepository,
			ReportRepository reportRepository) {
		return args -> {

			/*
			// Test bird DAO and prepare test data
			birdRepository.deleteAll();

			Bird bird1 = new Bird(UUID.randomUUID(), "Common loon 1",
					new HashSet<>(Arrays.asList("red eyes 1", "swim and dive 1", "webbed feet 1")));
			Bird bird2 = new Bird(UUID.randomUUID(), "Common loon 2",
					new HashSet<>(Arrays.asList("red eyes 2", "swim and dive 2", "webbed feet 2")));
			Bird bird3 = new Bird(UUID.randomUUID(), "Common loon 3",
					new HashSet<>(Arrays.asList("red eyes 3", "swim and dive 3", "webbed feet 3")));

			Bird savedBird1 = birdRepository.save(bird1);
			Bird savedBird2 = birdRepository.save(bird2);
			Bird savedBird3 = birdRepository.save(bird3);

			birdRepository.findAll()
					.forEach(v -> log.info("Bird: {}, {}, {}, {}, {}", v.getBirdId(), v.getSpecies(), v.getTraits()));

			Optional<Bird> searchBird1 = birdRepository.findById(savedBird1.getBirdId());
			Optional<Bird> searchBird2 = birdRepository.findById(savedBird2.getBirdId());
			Optional<Bird> searchBird3 = birdRepository.findById(savedBird3.getBirdId());

			searchBird1.ifPresent(v -> log.info("Bird: {}, {}, {}", v.getBirdId(), v.getSpecies(), v.getTraits()));
			searchBird2.ifPresent(v -> log.info("Bird: {}, {}, {}", v.getBirdId(), v.getSpecies(), v.getTraits()));
			searchBird3.ifPresent(v -> log.info("Bird: {}, {}, {}", v.getBirdId(), v.getSpecies(), v.getTraits()));

			// Test location DAO and prepare test data
			locationRepository.deleteAll();

			Location location1 = new Location(UUID.randomUUID(), Double.valueOf(25), Double.valueOf(71), "Location 1",
					"A", "Simon", LocalDateTime.now(), "Simon", LocalDateTime.now());
			Location location2 = new Location(UUID.randomUUID(), Double.valueOf(52), Double.valueOf(31), "Location 2",
					"A", "Simon", LocalDateTime.now(), "Simon", LocalDateTime.now());

			Location savedLocation1 = locationRepository.save(location1);
			Location savedLocation2 = locationRepository.save(location2);

			locationRepository.findAll()
					.forEach(v -> log.info("Location: {}, {}, {}, {}, {}, {}, {}, {}, {}", v.getLocationId(),
							v.getLatitude(), v.getLongitude(), v.getName(), v.getStatus(), v.getCreateBy(),
							v.getCreateDate(), v.getUpdateBy(), v.getUpdateDate()));

			Optional<Location> searchLocation1 = locationRepository.findById(savedLocation1.getLocationId());
			Optional<Location> searchLocation2 = locationRepository.findById(savedLocation2.getLocationId());

			searchLocation1.ifPresent(v -> log.info("Location: {}, {}, {}, {}, {}, {}, {}, {}, {}", v.getLocationId(),
					v.getLatitude(), v.getLongitude(), v.getName(), v.getStatus(), v.getCreateBy(), v.getCreateDate(),
					v.getUpdateBy(), v.getUpdateDate()));
			searchLocation2.ifPresent(v -> log.info("Location: {}, {}, {}, {}, {}, {}, {}, {}, {}", v.getLocationId(),
					v.getLatitude(), v.getLongitude(), v.getName(), v.getStatus(), v.getCreateBy(), v.getCreateDate(),
					v.getUpdateBy(), v.getUpdateDate()));

			locationRepository.findByLatitude(25)
					.forEach(v -> log.info("Location: {}, {}, {}, {}, {}, {}, {}, {}, {}", v.getLocationId(),
							v.getLatitude(), v.getLongitude(), v.getName(), v.getStatus(), v.getCreateBy(),
							v.getCreateDate(), v.getUpdateBy(), v.getUpdateDate()));
			locationRepository.findByLongitude(71)
					.forEach(v -> log.info("Location: {}, {}, {}, {}, {}, {}, {}, {}, {}", v.getLocationId(),
							v.getLatitude(), v.getLongitude(), v.getName(), v.getStatus(), v.getCreateBy(),
							v.getCreateDate(), v.getUpdateBy(), v.getUpdateDate()));
			locationRepository.findByStatus("A")
					.forEach(v -> log.info("Location: {}, {}, {}, {}, {}, {}, {}, {}, {}", v.getLocationId(),
							v.getLatitude(), v.getLongitude(), v.getName(), v.getStatus(), v.getCreateBy(),
							v.getCreateDate(), v.getUpdateBy(), v.getUpdateDate()));

			// Test scan DAO and prepare test data
			scanRepository.deleteAll();

			Scan scan1 = new Scan(UUID.randomUUID(), savedLocation1.getLocationId(),
					LocalDateTime.of(2022, 12, 1, 0, 0));
			Scan scan2 = new Scan(UUID.randomUUID(), savedLocation2.getLocationId(),
					LocalDateTime.of(2022, 12, 1, 0, 0));
			Scan scan3 = new Scan(UUID.randomUUID(), savedLocation1.getLocationId(),
					LocalDateTime.of(2022, 12, 2, 0, 0));
			Scan scan4 = new Scan(UUID.randomUUID(), savedLocation2.getLocationId(),
					LocalDateTime.of(2022, 12, 2, 0, 0));

			Scan savedScan1 = scanRepository.save(scan1);
			Scan savedScan2 = scanRepository.save(scan2);
			Scan savedScan3 = scanRepository.save(scan3);
			Scan savedScan4 = scanRepository.save(scan4);

			Optional<Scan> searchScan1 = scanRepository.findById(savedScan1.getScanId());
			Optional<Scan> searchScan2 = scanRepository.findById(savedScan2.getScanId());
			Optional<Scan> searchScan3 = scanRepository.findById(savedScan3.getScanId());
			Optional<Scan> searchScan4 = scanRepository.findById(savedScan4.getScanId());

			searchScan1.ifPresent(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));
			searchScan2.ifPresent(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));
			searchScan3.ifPresent(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));
			searchScan4.ifPresent(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));

			scanRepository.findByLocationId(savedScan1.getLocationId())
					.forEach(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));
			scanRepository.findByLocationId(savedScan2.getLocationId())
					.forEach(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));

			scanRepository.findByScanTime(LocalDateTime.of(2022, 12, 1, 0, 0))
					.forEach(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));
			scanRepository.findByScanTime(LocalDateTime.of(2022, 12, 2, 0, 0))
					.forEach(v -> log.info("Scan: {}, {}, {}", v.getScanId(), v.getLocationId(), v.getScanTime()));

			// Test result DAO and prepare test data
			resultRepository.deleteAll();

			Result result1 = new Result(savedScan1.getScanId(), savedBird1.getBirdId());
			Result result2 = new Result(savedScan2.getScanId(), savedBird2.getBirdId());
			Result result3 = new Result(savedScan2.getScanId(), savedBird1.getBirdId());
			Result result4 = new Result(savedScan1.getScanId(), savedBird2.getBirdId());

			Result result5 = new Result(savedScan3.getScanId(), savedBird3.getBirdId());
			Result result6 = new Result(savedScan4.getScanId(), savedBird2.getBirdId());
			Result result7 = new Result(savedScan4.getScanId(), savedBird3.getBirdId());
			Result result8 = new Result(savedScan3.getScanId(), savedBird2.getBirdId());

			Result savedResult1 = resultRepository.save(result1);
			Result savedResult2 = resultRepository.save(result2);
			Result savedResult3 = resultRepository.save(result3);
			Result savedResult4 = resultRepository.save(result4);

			resultRepository.save(result5);
			resultRepository.save(result6);
			resultRepository.save(result7);
			resultRepository.save(result8);

			Optional<Result> searchResult1 = resultRepository.findByScanIdAndBirdId(savedResult1.getScanId(),
					savedResult1.getBirdId());
			Optional<Result> searchResult2 = resultRepository.findByScanIdAndBirdId(savedResult2.getScanId(),
					savedResult2.getBirdId());
			List<Result> searchResult3 = resultRepository.findByScanId(savedResult3.getScanId());
			List<Result> searchResult4 = resultRepository.findByBirdId(savedResult4.getBirdId());

			searchResult1.ifPresent(v -> log.info("Result: {}, {}", v.getScanId(), v.getBirdId()));
			searchResult2.ifPresent(v -> log.info("Result: {}, {}", v.getScanId(), v.getBirdId()));
			searchResult3.forEach(v -> log.info("Result: {}, {}", v.getScanId(), v.getBirdId()));
			searchResult4.forEach(v -> log.info("Result: {}, {}", v.getScanId(), v.getBirdId()));
			
			reportRepository.deleteAll();
			
			*/
		};
	}
}
