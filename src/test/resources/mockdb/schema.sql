drop table TRIBONNACI if exists;

create table TRIBONNACI (
  NUMBER integer not null,
  COUNTER integer not null,
  TYPE varchar(9) not null,
  CONSTRAINT TRIBONNACI_PK PRIMARY KEY (NUMBER, TYPE)
);

