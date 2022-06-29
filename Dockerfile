FROM registry.access.redhat.com/ubi8/openjdk-11:latest

WORKDIR /rccst/

COPY target/rccst-standalone.jar app.jar

ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true"

ENV RCCST_DATABASE_HOST="rccst-postgres"
ENV RCCST_DATABASE_USER="1001710000"


EXPOSE 8280
EXPOSE 5432

ENTRYPOINT ["java", "-jar", "app.jar", "false"]