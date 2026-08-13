# ==========================================
# GIAI ĐOẠN 1: Xây dựng và đóng gói ứng dụng (Build Stage)
# ==========================================
FROM maven:3.8.5-openjdk-17 as build

WORKDIR /app

# Copy code từ máy vào thư mục /app/discoverserver trong container
COPY . /app/discoverserver

# Chuyển hướng làm việc vào chính thư mục đó để chạy lệnh mvn
WORKDIR /app/discoverserver

# Lúc này mvn sẽ tìm thấy pom.xml tại /app/discoverserver và build thành công
RUN mvn clean package -DskipTests


# ==========================================
# GIAI ĐOẠN 2: Chạy ứng dụng (Runtime Stage)
# ==========================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# QUAN TRỌNG: Phải trỏ đúng đường dẫn /app/discoverserver/target/*.jar để lấy file jar ra
COPY --from=build /app/discoverserver/target/*.jar discoverserver.jar

EXPOSE 9001

ENTRYPOINT ["java", "-jar", "discoverserver.jar"]