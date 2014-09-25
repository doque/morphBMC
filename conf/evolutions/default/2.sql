# --- !Ups

alter table compatibility add constraint unique_combination UNIQUE(attr1_id, attr2_id);