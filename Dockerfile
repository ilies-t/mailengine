FROM maven:3.9.8 AS maven
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package

FROM ghcr.io/graalvm/jdk-community:21
WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/mailengine-1.0.0-SNAPSHOT.jar /opt/app/
EXPOSE 8082
CMD native-image --version
ENTRYPOINT ["java", "-jar", "mailengine-1.0.0-SNAPSHOT.jar"]
