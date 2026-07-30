FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY common ./common
COPY carteira ./carteira
COPY cardapio ./cardapio
COPY pedidos ./pedidos
COPY alunos ./alunos

RUN mvn -B -pl carteira -am -DskipTests package

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/carteira/target/*.jar app.jar
USER spring

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
