# Stage 1: Build JAR
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Sửa lỗi CRLF trên Windows nếu có
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=9090
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]

