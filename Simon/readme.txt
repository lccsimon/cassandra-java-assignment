Cassandra Java Assignment - Created by Simon Lee (lcc.simon@gmail.com / 07436561586) on 2022-12-16

Attachment:
1. CassandraApplication.zip
	Exported project from Eclipse IDE. Included all source codes and resources of this assignment.
2. CQLStatements.md
	Included the required CQL to create necessary tables and indexes.
3. readme.txt	
	This TEXT file.
4. Test_CURL.zip
	Sample CURL commands
5. Test_JSON.zip
	Sample JSON contents used in CURL commands

Information of project "CassandraApplication"
1. Application should be executed at org.gcs.cassandra.CassandraApplication
2. Requirement: Create a RESTful endpoint for the scientists of OBOE to configure satellite scan locations.
	a) Handle by org.gcs.cassandra.controller.LocationManageController
	b) Support search, update, add, delete location in DB
2. Requirement: Create a RESTful endpoint for satellites to read their scan locations.
	a) Handle by org.gcs.cassandra.controller.SatellitesScanController
	b) List all available locations in DB
3. Requirement: Create a RESTful endpoint for satellites to persist scan results.
	a) Handle by org.gcs.cassandra.controller.SatellitesScanController
	b) Flow 1: Satellites create new scan and new bird before adding scan result
	c) Flow 2: Satellites create new result in single calling
4. Requirement: Create a RESTful endpoint for the scientists to read the scan results for a specific location on a given day.
	a) Handle by org.gcs.cassandra.controller.ResultReadController
	b) Flow 1: Scientists search location by scan date before obtaining result
	c) Flow 2: Scientists search result directly

Information of CQLStatements.md
1. Table bird - Store bird data with birdId as primary key
2. Table scan - Store scan data with scanId as primary key
3. Table result - Store scan result data which relate scan record with bird records
4. Table location - Store location data with locationId as primary key
5. Table report - Store report data for scientists with scanId & locationId & birdId as primary key

Design idea
1. Try to maintain data integrity and user friendly by creating more tables (bird & scan & result), although it may not be necessary in Apache Cassandra
2. All scan result data can be found in report table actually in order to fulfill requirement of this assignment (Use the "oboe" keyspace and create a table to hold scan locations and another table to hold scan results)
3. Try to avoid index in bird related tables (bird, result, report) due to the expected huge number of records
4. Indexes are created in scan & location tables, try to fulfill the interaction with scientists without affecting DB performance as less number of records are expected in the 2 tables


