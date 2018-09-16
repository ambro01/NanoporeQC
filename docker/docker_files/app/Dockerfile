# FROM r-base:3.4.3
# Use ubuntu 16.04 base image
FROM ubuntu:16.04

# set non-interactive mode
ENV DEBIAN_FRONTEND noninteractive

############# BEGIN INSTALLATION ##############

# Prepare to install R
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys E084DAB9
RUN echo 'deb http://cran.rstudio.com/bin/linux/ubuntu trusty/' >> /etc/apt/sources.list

RUN apt-get update && apt-get install -y \
    sudo r-base gdebi-core libcurl4-gnutls-dev libcairo2-dev libxt-dev libssl-dev \
    gsl-bin net-tools less grep psmisc procps git wget libgsl0-dev \
    python-tables python-setuptools python-pip python-dev cython libhdf5-serial-dev python-rpy2

RUN echo 'export PATH=/opt/conda/bin:$PATH' > /etc/profile.d/conda.sh && \
    wget --quiet https://repo.continuum.io/archive/Anaconda2-5.0.1-Linux-x86_64.sh -O ~/anaconda.sh && \
    /bin/bash ~/anaconda.sh -b -p /opt/conda && \
    rm ~/anaconda.sh

ENV PATH /opt/conda/bin:$PATH
RUN pip install readline
RUN pip install six==1.11.0
RUN R -e "install.packages(c('codetools', 'MASS'), repos='http://cran.rstudio.com/')"
RUN git clone https://github.com/ambro01/poretools /tmp/poretools
RUN cd /tmp/poretools && python setup.py install
RUN export PATH=$PATH:/home/poretools/.local/bin
RUN pip install --upgrade pillow

RUN R -e "install.packages(c('dplyr', 'ggplot2', 'gridExtra', 'svDialogs', 'data.table', 'bit64', 'Rmisc', 'rlist', 'magrittr', 'NbClust', 'Rlof'), repos='http://cran.rstudio.com/')"
RUN R -e "install.packages(c('mclust', 'cluster', 'factoextra'), repos='http://cran.rstudio.com/')"
RUN R -e "source('https://bioconductor.org/biocLite.R'); biocLite(c('IONiseR', 'rhdf5', 'ShortRead'))"

RUN apt-get install -y unzip
ENV DISPLAY :0.0

# make directories stucture

RUN mkdir /usr/nanoporeqc
RUN mkdir /usr/nanoporeqc/files
RUN mkdir /usr/nanoporeqc/report
RUN mkdir /usr/nanoporeqc/scripts
RUN mkdir /usr/nanoporeqc/summary
RUN mkdir /usr/nanoporeqc/app
RUN mkdir /usr/nanoporeqc/tools
RUN mkdir /usr/nanoporeqc/tools/rserve

# Java 9 installation
COPY tools/java9oracle.tar.gz /usr/lib/jvm/java9oracle.tar.gz
RUN tar -zxvf /usr/lib/jvm/java9oracle.tar.gz -C /usr/lib/jvm/

ENV JAVA_HOME /usr/lib/jvm/java-9-oracle
ENV PATH /usr/lib/jvm/java-9-oracle/bin:$PATH

COPY tools/Rserv.conf /usr/nanoporeqc/tools/rserve/Rserv.conf

COPY tools/fastqc_v0.11.7.zip /usr/local/fastqc.zip
RUN unzip /usr/local/fastqc.zip -d /usr/nanoporeqc/tools

RUN chmod 777 -R /usr/nanoporeqc
ENV PATH /usr/nanoporeqc/tools/FastQC:$PATH

COPY tools/Rserve_1.7-3.tar.gz /usr/bin/Rserve_1.7-3.tar.gz
RUN R CMD INSTALL /usr/bin/Rserve_1.7-3.tar.gz

# start app
COPY nanoporeqc.jar /usr/nanoporeqc/app/nanoporeqc.jar
COPY scripts/start.sh /usr/nanoporeqc/app/
ENTRYPOINT ["/usr/nanoporeqc/app/start.sh"]

EXPOSE 8080
