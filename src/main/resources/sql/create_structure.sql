create database NANOPORE_QC;

use NANOPORE_QC;

create table USERS (
  ID decimal(8,0) NOT NULL,
  USER_NAME varchar(50) NOT NULL,
  PASSWORD varchar(255) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (ID),
  UNIQUE (USER_NAME)
);

create table ANALYSES (
  ID decimal(8,0) NOT NULL,
  ANALYSE_TIME datetime,
  ANALYSE_NAME varchar(50),
  ANALYSE_COMMENT varchar(150),
  SUMMARY_FILE_CONTENT mediumblob,
  USER_ID decimal(8,0),
  PRIMARY KEY (ID),
  FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
  UNIQUE (ID)
);