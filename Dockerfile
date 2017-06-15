FROM absurd/rick-java-redis:latest
MAINTAINER Absurd "www_1350@163.com"
ENV REFRESHED_AT 2017-06-06
WORKDIR /code

ADD target /code/target

#RUN ["mvn", "package","-Dmaven.test.skip=true"]

ENTRYPOINT ["java", "-jar", "target/rick-1.0-SNAPSHOT.jar"]
CMD ["redis-server","--appendonly yes"]
EXPOSE 8080
EXPOSE 6379



