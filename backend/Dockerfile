FROM maven:3.9-eclipse-temurin-25

WORKDIR /app

# Копируем pom.xml отдельно, чтобы Maven мог закешировать зависимости
COPY pom.xml .

RUN mvn -q dependency:go-offline

# Исходники будут примонтированы через volume, поэтому копировать их не нужно

ENTRYPOINT ["sh", "-c", "mvn -q clean package -DskipTests && java -jar target/*.jar"]
