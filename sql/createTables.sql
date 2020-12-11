use metrics;
CREATE TABLE projectStateCount
(
  id              TINYINT unsigned NOT NULL AUTO_INCREMENT, # Unique ID for the record
  successCount    INT unsigned NOT NULL,                # Total number of successful projects
  failCount       INT unsigned NOT NULL,                # Total number of failed projects
  PRIMARY KEY     (id)                                  # Make the id the primary key
);

CREATE TABLE countryStats
(
  id              SMALLINT unsigned NOT NULL AUTO_INCREMENT, # Unique ID for the record
  country         varchar(10) NOT NULL,                 # Country code/name
  successCount    INT unsigned NOT NULL,                # Total number of successful projects
  failCount       INT unsigned NOT NULL,                # Total number of failed projects
  PRIMARY KEY     (id)                                  # Make the id the primary key
);

CREATE TABLE top10success
(
  id              TINYINT unsigned NOT NULL AUTO_INCREMENT, # Unique index in the top list
  country         varchar(10) NOT NULL,                 # Country code/name
  successCount    INT unsigned NOT NULL,                # Total number of successful projects
  PRIMARY KEY     (id)                                  # Make the id the primary key
);

CREATE TABLE top10failed
(
  id              TINYINT unsigned NOT NULL AUTO_INCREMENT, # Unique index in the top list
  country         varchar(10) NOT NULL,                 # Country code/name
  failCount       INT unsigned NOT NULL,                # Total number of failed projects
  PRIMARY KEY     (id)                                  # Make the id the primary key
);
