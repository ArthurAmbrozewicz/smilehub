# ─── STAGE 1: Build ───────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia o pom.xml e baixa as dependências (cache eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código e gera o .jar
COPY src ./src
RUN mvn package -DskipTests -B

# ─── STAGE 2: Runtime ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copia apenas o .jar gerado no stage anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]