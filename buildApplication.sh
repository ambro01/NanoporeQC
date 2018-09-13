#!/bin/bash
mvn clean install -f frontend/pom.xml
rm -r backend/src/main/resources/public/static
cp -r frontend/dist/static backend/src/main/resources/public/
mvn clean install -f backend/pom.xml