FROM registry.access.redhat.com/ubi8/openjdk-11:1.11-2.1648459569

WORKDIR /rccst/

COPY target/rccst-standalone.jar app.jar

ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true"

EXPOSE 8280
EXPOSE 5432
EXPOSE 7777

ENTRYPOINT ["java", "-jar", "app.jar"]