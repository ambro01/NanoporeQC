create database NANOPORE_QC;

use NANOPORE_QC;

create table USERS (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  USER_NAME varchar(50) NOT NULL,
  PASSWORD varchar(255) NOT NULL,
  PRIMARY KEY (ID),
  UNIQUE (ID),
  UNIQUE (USER_NAME)
);

create table ANALYSES (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  RUN_TIME datetime,
  NAME varchar(50),
  COMMENT varchar(150),
  TYPE varchar(10),
  USER_RATE varchar(10),
  QUALITY_STATUS varchar(10),
  MAIN_SUMMARY mediumblob,
  ADDITIONAL_SUMMARY mediumblob,
  HTML_REPORT mediumblob,
  USER_ID decimal(8,0),
  PARENT_ANALYSIS_ID decimal(8,0),
  PRIMARY KEY (ID),
  UNIQUE (ID)
);

INSERT INTO USERS (ID, USER_NAME, PASSWORD)
VALUES (1, "admin@nanoporeqc.com", "$2a$10$azwihJHZCxpXm0KYkx07J.apVlbbT7lXrT9pCaF.T5ZFrNxkngOSO")
