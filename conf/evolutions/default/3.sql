# Tasks schema

# --- !Ups

ALTER TABLE task ADD date date;
ALTER TABLE task ADD priority integer

# --- !Downs

ALTER TABLE task DROP date;
ALTER TABLE task DROP priority;
