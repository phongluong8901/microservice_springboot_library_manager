# ==========================================
# GIAI ĐOẠN 1: Xây dựng và đóng gói ứng dụng (Build Stage)
# ==========================================
FROM maven:3.8.5-openjdk-17 as build

WORKDIR /app

# Copy từ thư mục gốc vào container
COPY notificationservice/notificationservice /app/notificationservice
COPY commonservice/commonservice /app/commonservice

# Chuyển hướng làm việc để build commonservice trước bằng INSTALL
WORKDIR /app/commonservice
RUN mvn clean install -DskipTests

# Sau đó build notificationservice
WORKDIR /app/notificationservice
RUN mvn clean package -DskipTests


# ==========================================
# GIAI ĐOẠN 2: Chạy ứng dụng (Runtime Stage)
# ==========================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/notificationservice/target/*.jar notificationservice.jar

EXPOSE 9003

ENTRYPOINT ["java", "-jar", "notificationservice.jar"]