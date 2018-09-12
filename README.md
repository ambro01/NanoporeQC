# NanoporeQC

NanoporeQc is a tool for quality control of data produced by MinION sequencer.
It is a web application. It can run analyses for data in FAST5 and FASTQ formats. 

## Technological stack 

Java 9, Spring Boot 2.x.x, Hibernate 5.x.x, MySql, Vue.js 2,  R 3.4.3, Rserve, Docker

### Installing

Application with R environment and data base are run on docker containers (in separate network).
Images have been build and they are stored in DockerHub. You only need to install docker-compose.
To create and run containers:
1. Run command 'docker-compose up' in /docker directory
To stop and start containers run: 'docker-compose stop' 'docker-compose stop'

Application is deployed in container named: 'nanoporeqc_app', port 8080 is open.
Data base is run in container named: 'nanoporeqc_db', port 3037 is open.

You can access to application on: localhost:8080

###  Application building

1. Build front by running 'npm run-script build' in /frontend directory.
2. Run script "prepareFront.sh". It will copy necessary frond files to backend module.
3. Build backend. Run e.g. 'mvn clean install' in /backend directory.


### Application restart
After building new package you can run your new version. 
Firstly copy .jar package from /backend directory to application container:
$ docker cp <project location>/backend/target/backend-1.0-SNAPSHOT.jar nanoporeqc_app:/usr/nanoporeqc/app/nanoporeqc.jar
And restart the container with application (web service)
$ docker stop web
$ docker start web
