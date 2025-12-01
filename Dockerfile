#=====================================================
# ETAPA 1: CONSTRUCCION (BUILD)
#=====================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn -q -e -B dependency:go-offline

COPY src ./src

RUN mvn -q -e -B clean package -DskipTests

#=====================================================
#ETAPA 2: EJECUCION (RUNTIME)
#=====================================================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]