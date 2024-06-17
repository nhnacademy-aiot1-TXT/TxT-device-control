FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ./target/control-0.0.1-SNAPSHOT.jar TxT-device-control.jar
ENTRYPOINT ["java", "-jar", "/TxT-device-control.jar"]
