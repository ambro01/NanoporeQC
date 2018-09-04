# NanoporeQC

NanoporeQc is a tool for quality control of data produced by MinION sequencer.
It is a web application. It can run analyses for data in FAST5 and FASTQ formats. 

## Technological stack 

Java 9, Spring Boot 2.x.x, Hibernate 5.x.x, MySql, Vue.js 2,  R 3.4.3, Rserve, Docker

### Installing

Application with R environment and data base are run on docker containers (in separate network).
By running script "startAndRun.sh" from docker dir, everything will be ready for few seconds.
Application and data base can be accessed from localhost on ports: 8080 (app), 3306 (db). 

###  Application building

Firstly run script "prepareFront.sh". It will copy necessary frond files to backend module.
After that build backend module. With maven it will be command "mvn clean install".
Application is packed to .jar file. Thanks to use Spring Boot,it has embedded Apache Tomcat,
so app can be run by simply java command "java -jar xxx.jar"