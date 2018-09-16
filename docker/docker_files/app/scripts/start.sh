#!/bin/bash

R CMD Rserve --no-save --RS-conf /usr/nanoporeqc/tools/rserve/Rserv.conf

java -jar /usr/nanoporeqc/app/nanoporeqc.jar
