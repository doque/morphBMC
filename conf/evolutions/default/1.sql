# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table attribute (
  id                        bigint not null,
  name                      varchar(255),
  user_id                   varchar(255),
  parameter_id              bigint,
  constraint pk_attribute primary key (id))
;

create table compatibility (
  id                        bigint not null,
  att1_id                   bigint,
  att2_id                   bigint,
  user_id                   varchar(255),
  rating_id                 bigint,
  constraint pk_compatibility primary key (id))
;

create table parameter (
  id                        bigint not null,
  name                      varchar(255),
  user_id                   varchar(255),
  problem_id                bigint,
  constraint pk_parameter primary key (id))
;

create table problem (
  id                        bigint not null,
  user_id                   varchar(255),
  name                      varchar(255),
  current_stage             integer,
  constraint ck_problem_current_stage check (current_stage in (0,1)),
  constraint pk_problem primary key (id))
;

create table rating (
  id                        bigint not null,
  name                      varchar(255),
  value                     varchar(255),
  constraint pk_rating primary key (id))
;

create sequence attribute_seq;

create sequence compatibility_seq;

create sequence parameter_seq;

create sequence problem_seq;

create sequence rating_seq;

alter table attribute add constraint fk_attribute_parameter_1 foreign key (parameter_id) references parameter (id);
create index ix_attribute_parameter_1 on attribute (parameter_id);
alter table compatibility add constraint fk_compatibility_att1_2 foreign key (att1_id) references attribute (id);
create index ix_compatibility_att1_2 on compatibility (att1_id);
alter table compatibility add constraint fk_compatibility_att2_3 foreign key (att2_id) references attribute (id);
create index ix_compatibility_att2_3 on compatibility (att2_id);
alter table compatibility add constraint fk_compatibility_rating_4 foreign key (rating_id) references rating (id);
create index ix_compatibility_rating_4 on compatibility (rating_id);
alter table parameter add constraint fk_parameter_problem_5 foreign key (problem_id) references problem (id);
create index ix_parameter_problem_5 on parameter (problem_id);



# --- !Downs

drop table if exists attribute cascade;

drop table if exists compatibility cascade;

drop table if exists parameter cascade;

drop table if exists problem cascade;

drop table if exists rating cascade;

drop sequence if exists attribute_seq;

drop sequence if exists compatibility_seq;

drop sequence if exists parameter_seq;

drop sequence if exists problem_seq;

drop sequence if exists rating_seq;

