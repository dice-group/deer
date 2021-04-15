##########
## RELEASE
FROM openjdk:11-jdk
WORKDIR /
VOLUME /plugins
VOLUME /data
ADD deer.jar /
ENV JAVA_OPTS="-Xmx2G"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/deer.jar"]