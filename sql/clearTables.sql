#Clear tables and reset the counters (id's)
delete from projectStateCount;
alter table projectStateCountAUTO_INCREMENT = 1;
insert into projectStateCount(id, successcount, failcount) values (1, 0, 0);

delete from countryStats;
alter table countryStats AUTO_INCREMENT = 1;

delete from top10success;
alter table top10success AUTO_INCREMENT = 1;

delete from top10failed;
alter table top10failed AUTO_INCREMENT = 1;
