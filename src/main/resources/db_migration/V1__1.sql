drop table if exists access_log;
create table access_log
(
  id bigint auto_increment primary key,
  log_date TIMESTAMP not null,
  ip_address varchar(15) not null,
  request varchar(255) not null,
  status INT(3) not null,
  user_agent varchar(255) not null
) CHARACTER SET utf8 COLLATE utf8_general_ci;

drop table if exists blocked_ip;
create table blocked_ip
(
  ip_address VARCHAR(15) primary key,
  reason varchar(255) not null
) CHARACTER SET utf8 COLLATE utf8_general_ci;