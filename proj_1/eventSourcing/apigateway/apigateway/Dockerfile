# ==========================================
# GIAI ĐOẠN 1: Xây dựng và đóng gói ứng dụng (Build Stage)
# ==========================================
FROM maven:3.8.5-openjdk-17 as build

WORKDIR /app

# Copy từ thư mục gốc vào container
COPY apigateway/apigateway /app/apigateway

# Sau đó build apigateway
WORKDIR /app/apigateway
RUN mvn clean package -DskipTests


# ==========================================
# GIAI ĐOẠN 2: Chạy ứng dụng (Runtime Stage)
# ==========================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/apigateway/target/*.jar apigateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "apigateway.jar"]