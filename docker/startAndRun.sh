#!/bin/bash

#Create network
docker network create qc_net

#Build db container
docker build -t qc_db ./db

#Run db container
docker run -p 3307:3306 -d --name qc_db --hostname=qc_db --net qc_net qc_db

#Build app container
docker build -t qc_app ./app

#Run app container
docker run -p 8080:8080 -d -i --name qc_app --hostname=nanoporeqc --net qc_net qc_app

