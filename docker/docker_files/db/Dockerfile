FROM mysql:5.7.15

ENV MYSQL_ROOT_PASSWORD=nanoporeqc

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306
