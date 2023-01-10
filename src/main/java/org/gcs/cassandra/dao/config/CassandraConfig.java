package org.gcs.cassandra.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraConfig {

	// To override problem in local testing environment only
	// https://stackoverflow.com/questions/60668792/spring-data-with-cassandra-giving-illegalstateexception
	public @Bean CqlSession session() {
		return CqlSession.builder().withKeyspace("oboe").build();
	}
}
