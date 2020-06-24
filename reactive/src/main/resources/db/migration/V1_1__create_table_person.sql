CREATE TABLE person (
    person_id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    age INT
);

alter table person add unique uk_name (name);