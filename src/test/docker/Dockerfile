FROM eclipse-temurin:22-jdk

COPY target/quarkus-app/lib /app/lib/
COPY target/quarkus-app/*.jar /app/
COPY target/quarkus-app/app /app/app/
COPY target/quarkus-app/quarkus /app/quarkus/

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "quarkus-run.jar"]