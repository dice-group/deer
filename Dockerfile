########
## BUILD
# build first using maven
FROM maven:3.8-adoptopenjdk-16 as builder
WORKDIR /deer
ADD . /deer
RUN mvn clean package shade:shade -Dmaven.test.skip=true
WORKDIR /deer/deer-cli
RUN PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) && \
    cp -p ./target/deer-cli-${PROJECT_VERSION}.jar /deer/deer.jar
##########
## RELEASE
# then run in a lighter jdk base
FROM adoptopenjdk/openjdk16:jre
WORKDIR /
VOLUME /plugins
VOLUME /data
# copy jar from build step
COPY --from=builder /deer/deer.jar deer.jar
ENV JAVA_OPTS="-Xmx2G"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/deer.jar"]
