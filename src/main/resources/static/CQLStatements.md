# Cassandra CQL Statements

Use this file to include your DDL.  Also include any DML that you may have created.


## DDL

# Store information of all birds
drop table oboe.bird;

create table oboe.bird(
	birdId UUID PRIMARY KEY,
	species TEXT,
	traits SET<TEXT>
);

# Store information of all scans
drop table oboe.scan;

create table oboe.scan(
	scanId UUID PRIMARY KEY,
	locationId UUID,
	scanTime TIMESTAMP
);

drop index oboe.scan_locationId_idx;
drop index oboe.scan_scanTime_idx;

create index on oboe.scan(locationId);
create index on oboe.scan(scanTime);

# Store birds found in each scan 
drop table oboe.result;

create table oboe.result(
	scanId UUID,
	birdId UUID,
	PRIMARY KEY (scanId, birdId)
);

# Store all locations configured
drop table oboe.location;

create table oboe.location(
	locationId UUID PRIMARY KEY,
	latitude DOUBLE,
	longitude DOUBLE,
	name TEXT,
	status TEXT,
	createBy TEXT,
	createDate TIMESTAMP,
	updateBy TEXT,
	updateDate TIMESTAMP
);

drop index oboe.location_latitude_idx;
drop index oboe.location_longitude_idx;
drop index oboe.location_name_idx;
drop index oboe.location_status_idx;

create index on oboe.location(latitude);
create index on oboe.location(longitude);
create index on oboe.location(name);
create index on oboe.location(status);

# Store report data for scientists
drop table oboe.report;

create table oboe.report(
	scanId UUID,
	locationId UUID,
	birdId UUID,
	scanTime TIMESTAMP,
	latitude DOUBLE,
	longitude DOUBLE,
	species TEXT,
	traits SET<TEXT>,
	PRIMARY KEY (scanId, locationId, birdId)
);


## DML (if any)

