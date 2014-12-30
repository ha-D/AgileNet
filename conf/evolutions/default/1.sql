# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  id                        varchar(255) not null,
  name                      varchar(255),
  parent_id                 varchar(255),
  constraint pk_category primary key (id))
;

create table role (
  id                        varchar(255) not null,
  name                      varchar(255) not null,
  constraint uq_role_name unique (name),
  constraint pk_role primary key (id))
;

create table user (
  id                        varchar(255) not null,
  first_name                varchar(255) not null,
  last_name                 varchar(255) not null,
  email                     varchar(255) not null,
  password                  varchar(255) not null,
  national_id               varchar(255),
  contact_phone             varchar(255),
  creation_date             timestamp not null,
  is_activated              boolean,
  is_suspended              boolean,
  constraint uq_user_email unique (email),
  constraint uq_user_national_id unique (national_id),
  constraint pk_user primary key (id))
;


create table user_role (
  user_id                        varchar(255) not null,
  role_id                        varchar(255) not null,
  constraint pk_user_role primary key (user_id, role_id))
;
create sequence category_seq;

create sequence role_seq;

create sequence user_seq;

alter table category add constraint fk_category_parent_1 foreign key (parent_id) references category (id) on delete restrict on update restrict;
create index ix_category_parent_1 on category (parent_id);



alter table user_role add constraint fk_user_role_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_role add constraint fk_user_role_role_02 foreign key (role_id) references role (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists category;

drop table if exists role;

drop table if exists user;

drop table if exists user_role;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists category_seq;

drop sequence if exists role_seq;

drop sequence if exists user_seq;

