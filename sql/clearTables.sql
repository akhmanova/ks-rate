#Clear tables and reset the counters (id's)
delete from projectstatecount;
alter table projectstatecount AUTO_INCREMENT = 1;
insert into projectstatecount(id, successcount, failcount) values (1, 0, 0);

delete from countrystats;
alter table countrystats AUTO_INCREMENT = 1;

delete from top10success;
alter table top10success AUTO_INCREMENT = 1;

delete from top10failed;
alter table top10failed AUTO_INCREMENT = 1;
