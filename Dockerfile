FROM maven:latest AS stage1
LABEL authors="great"
WORKDIR /app
COPY pom.xml /app
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean package -Dmaven.test.skip=true

FROM bellsoft/liberica-openjdk-debian:17.0.1 AS final
RUN groupadd -r appgroup && useradd -r -u 1000 -g appgroup appuser
WORKDIR /app
COPY --from=stage1 /app/target/*jar app.jar
EXPOSE 8080
USER 1000
CMD ["java", "-jar", "app.jar"]