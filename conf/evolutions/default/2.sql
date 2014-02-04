# Tasks schema

# --- !Ups

ALTER TABLE task ADD memo varchar(1024);

# --- !Downs

ALTER TABLE task DROP memo;

