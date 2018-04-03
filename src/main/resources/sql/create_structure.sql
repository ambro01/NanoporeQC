create database NANOPORE_QC;

use NANOPORE_QC;

create table USER (
  USER_ID numeric(8,0),
  LOGIN varchar(30),
  EMAIL varchar(30),
  PASSWORD varchar(30)
);

create table ANALYSE_ENTITY (
  ANALYSE_ID numeric(8,0),
  ANALYSE_TIME datetime,
  SUMMARY_FILE_NAME varchar(50),
  SUMMARY_FILE_CONTENT blob
);