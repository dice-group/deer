##########
## RELEASE
FROM adoptopenjdk/openjdk16:jre
WORKDIR /
VOLUME /plugins
VOLUME /data
VOLUME /.server-storage
ADD deer.jar /
ENV JAVA_OPTS="-Xmx2G"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/deer.jar"]
