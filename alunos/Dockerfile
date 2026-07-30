FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY common ./common
COPY alunos ./alunos
COPY cardapio ./cardapio
COPY carteira ./carteira
COPY pedidos ./pedidos

RUN mvn -B -pl alunos -am -DskipTests package

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/alunos/target/*.jar app.jar
USER spring

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
