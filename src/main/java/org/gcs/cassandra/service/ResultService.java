package org.gcs.cassandra.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.gcs.cassandra.dao.Result;
import org.gcs.cassandra.dao.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultService {

	private final static Logger log = LoggerFactory.getLogger(ResultService.class);

	private ResultRepository resultRespository;

	// constructor of result service
	public ResultService(ResultRepository resultRespository) {

		this.resultRespository = resultRespository;
	}

	// Create result in DB if result record (scanId & birdId) does not exist
	public Optional<Result> addResult(UUID scanId, UUID birdId) {

		// Prepare for return object
		Optional<Result> returnResult = Optional.empty();

		// Prepare new result object
		Result newResult = new Result(scanId, birdId);

		// Check if result exists, obtain list of result by scanId and birdId
		Optional<Result> searchResult = resultRespository.findByScanIdAndBirdId(scanId, birdId);

		log.debug("addResult() triggered, searchResult.isEmpty() = {} ", searchResult.isEmpty());

		// Only insert if result not exist
		if (searchResult.isEmpty()) {

			// create scan and return
			returnResult = Optional.of(resultRespository.save(newResult));
		}

		return returnResult;
	}

	// search results in DB by results
	public List<Result> searchResultByScanId(UUID scanId) {

		List<Result> resultList = resultRespository.findByScanId(scanId);

		log.debug("searchResultByScanId() triggered, total {} available results found", resultList.size());

		return resultList;
	}

}
