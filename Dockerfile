FROM openjdk:11.0.5
RUN echo "Asia/Tehran" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata
RUN apt-get -y update && apt-get install -y maven
WORKDIR /code
ADD pom.xml /code/pom.xml
ADD src /code/src
RUN ["mvn", "package"]
EXPOSE 8080
CMD ["mvn", "tomcat7:run"]