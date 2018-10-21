# Log parser [![Build Status](https://travis-ci.org/mamunsrdr/log-parser.svg?branch=master)](https://travis-ci.org/mamunsrdr/log-parser)
[The access log parser problem](https://github.com/mamunsrdr/log-parser/wiki/Problem)'s solution

### What's inside
* jdk 8
* spring boot 2.0.6
* spring batch

### Steps to follow
1. Create MySQL database and user from `resource/create_db_n_user.txt`
2. Compile source to build `.jar` file
3. Execute jar file accordingly. Expected time to finish execution `~16s`

```
//step: 1
# mysql -u root -p < resource\create_db_n_user.txt
# Enter Password: ********   
  
//step: 2
# ./greadlew build
  
//step: 3
# java -jar build/libs/log-parser-0.0.1.jar --accesslog=/path/to/access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200
```

* spring batch

### Database
1. DB `whub_access` contains 2 basic tables
    1. `access_log` - this table save all the logs during parsing
    2. `blocked_ip` - after parsing detected ip saved into this table
2. `batch_*` tables holds spring batch execution related information  
3. Exceeding ip will show as `info` log
4. Tables schema and Query that extracts the blocked ip
```
-- access_log table
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
CREATE INDEX access_log_log_date_index ON access_log (log_date);
  
-- blocked_ip table  
drop table if exists blocked_ip;
create table blocked_ip
(
  ip_address VARCHAR(15) primary key,
  reason varchar(255) not null
) CHARACTER SET utf8 COLLATE utf8_general_ci;
  
-- sql query
SELECT ip_address, count(ip_address) total
FROM access_log
WHERE log_date BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.14:00:00'
GROUP BY ip_address
HAVING count(ip_address) > 200;
```

### Log to show ip exceeding threshold
```
[ INFO] c.w.p.s.AccessLogThresholdServiceImpl - ====================== 192.168.106.134 exceeding hourly limit of 200 requests [total: 232] ======================
[ INFO] c.w.p.s.AccessLogThresholdServiceImpl - ====================== 192.168.11.231 exceeding hourly limit of 200 requests [total: 211] ======================
```