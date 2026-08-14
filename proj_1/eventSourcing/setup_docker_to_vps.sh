# ==========================================
# 1. SETUP GIT & SSH KEY
# ==========================================
sudo apt update
sudo apt install -y git-all
git --version

# Tạo SSH key (Dùng thuật toán ed25519 như bạn đã chọn)
ssh-keygen -t ed25519 -C "phongluong3366@gmail.com"

# Vào thư mục .ssh và xem public key
cd ~/.ssh
ls -la
# LƯU Ý: Vì bạn dùng ed25519 nên tên file là id_ed25519.pub (không phải id_rsa.pub)
cat id_ed25519.pub

# (Hành động thủ công: Copy đoạn key trên dán vào GitHub -> Settings -> SSH and GPG keys -> New SSH key)

# ==========================================
# 2. CLONE REPOSITORY
# ==========================================
cd ~
mkdir -p app
cd app

# Clone repo qua SSH
git clone git@github.com:phongluong8901/microservice_springboot_library_manager.git

cd microservice_springboot_library_manager

# ==========================================
# 3. SETUP DOCKER & DOCKER COMPOSE
# ==========================================
# Cập nhật hệ thống và cài đặt các gói phụ thuộc cần thiết
sudo apt-get update
sudo apt-get install -y ca-certificates curl

# Tạo thư mục chứa khóa GPG của Docker và tải về khóa chính thức
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Thêm Docker repository vào danh sách nguồn APT của Ubuntu
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Cập nhật lại danh sách gói và cài đặt Docker Engine + Docker Compose plugin
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Cấp quyền chạy Docker không cần sudo
sudo usermod -aG docker $USER

# Áp dụng quyền nhóm Docker ngay lập tức cho phiên làm việc hiện tại
newgrp docker

docker ps

ls
docker compose -f docker-compose-k8s.yml up -d
docker ps
docker compose -f docker-compose-k8s.yml logs -f