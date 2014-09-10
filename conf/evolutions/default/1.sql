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
  user_id                   varchar(255),
  rating                    float,
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


create table compatibility_attribute (
  compatibility_id               bigint not null,
  attribute_id                   bigint not null,
  constraint pk_compatibility_attribute primary key (compatibility_id, attribute_id))
;
create sequence attribute_seq;

create sequence compatibility_seq;

create sequence parameter_seq;

create sequence problem_seq;

alter table attribute add constraint fk_attribute_parameter_1 foreign key (parameter_id) references parameter (id);
create index ix_attribute_parameter_1 on attribute (parameter_id);
alter table parameter add constraint fk_parameter_problem_2 foreign key (problem_id) references problem (id);
create index ix_parameter_problem_2 on parameter (problem_id);



alter table compatibility_attribute add constraint fk_compatibility_attribute_co_01 foreign key (compatibility_id) references compatibility (id);

alter table compatibility_attribute add constraint fk_compatibility_attribute_at_02 foreign key (attribute_id) references attribute (id);

# --- !Downs

drop table if exists attribute cascade;

drop table if exists compatibility cascade;

drop table if exists compatibility_attribute cascade;

drop table if exists parameter cascade;

drop table if exists problem cascade;

drop sequence if exists attribute_seq;

drop sequence if exists compatibility_seq;

drop sequence if exists parameter_seq;

drop sequence if exists problem_seq;

