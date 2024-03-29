# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table attribute (
  id                        bigint not null,
  name                      varchar(255),
  user_id                   varchar(255),
  parameter_id              bigint,
  version                   timestamp not null,
  constraint pk_attribute primary key (id))
;

create table compatibility (
  id                        bigint not null,
  attr1_id                  bigint,
  attr2_id                  bigint,
  user_id                   varchar(255),
  rating_id                 bigint,
  override_comment          varchar(255),
  constraint pk_compatibility primary key (id))
;

create table parameter (
  id                        bigint not null,
  name                      varchar(255),
  user_id                   varchar(255),
  problem_id                bigint,
  created                   bigint,
  constraint pk_parameter primary key (id))
;

create table problem (
  id                        bigint not null,
  user_id                   varchar(255),
  owner                     varchar(255),
  name                      varchar(255),
  statement                 TEXT,
  stage                     integer,
  constraint ck_problem_stage check (stage in (0,1,2,3,4,5,6)),
  constraint pk_problem primary key (id))
;

create table rating (
  id                        bigint not null,
  name                      varchar(255),
  value                     float,
  constraint pk_rating primary key (id))
;

create sequence attribute_seq;

create sequence compatibility_seq;

create sequence parameter_seq;

create sequence problem_idproblem_seq;

create sequence rating_seq;

alter table attribute add constraint fk_attribute_parameter_1 foreign key (parameter_id) references parameter (id);
create index ix_attribute_parameter_1 on attribute (parameter_id);
alter table compatibility add constraint fk_compatibility_attr1_2 foreign key (attr1_id) references attribute (id);
create index ix_compatibility_attr1_2 on compatibility (attr1_id);
alter table compatibility add constraint fk_compatibility_attr2_3 foreign key (attr2_id) references attribute (id);
create index ix_compatibility_attr2_3 on compatibility (attr2_id);
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

drop sequence if exists problem_idproblem_seq;

drop sequence if exists rating_seq;

