FROM maven:3.8.4-openjdk-17 as MAVEN_BUILD

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=MAVEN_BUILD /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
