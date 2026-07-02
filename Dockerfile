FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY target/DevSync-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Duser.timezone=UTC","-jar","app.jar"]
