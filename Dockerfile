FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/realtimechatapp-0.0.1-SNAPSHOT.jar realtimechatapp.jar
ENTRYPOINT ["java","-jar","/realtimechatapp.jar"]