CREATE USER ''@''  # to be set in 'username'@'user hostname' format
  IDENTIFIED BY '';  # user password
GRANT SELECT,INSERT,UPDATE,DELETE, ALTER
  ON metrics.*
  TO ''@'';  # to be set in 'username'@'user hostname' format
  
