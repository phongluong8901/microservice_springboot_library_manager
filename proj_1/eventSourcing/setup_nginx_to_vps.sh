# Đăng nhập vào máy chủ từ xa qua SSH
ssh root@103.126.161.94

# ==========================================
# 1. CÀI ĐẶT NGINX ĐỂ LÀM REVERSE PROXY
# ==========================================
# Cập nhật danh sách gói phần mềm trên hệ thống
sudo apt update

# Cài đặt web server Nginx (thêm -y để tự động đồng ý cài đặt)
sudo apt install -y nginx

# Kiểm tra các profile ứng dụng được Nginx đăng ký với tường lửa (UFW)
sudo ufw app list

# Kiểm tra trạng thái hoạt động hiện tại của dịch vụ Nginx
systemctl status nginx

# (Thử nghiệm thủ công): Truy cập trình duyệt bằng IP (103.126.161.94) 
# -> Hiển thị trang chủ Nginx mặc định -> Xác nhận cài đặt thành công.

# Khám phá cấu trúc thư mục cấu hình của Nginx trong hệ thống
ls
cd ..
ls
cd etc
ls
cd nginx
ls

# Xem nội dung file cấu hình tổng của Nginx
cat nginx.conf


# ==========================================
# 2. CẤU HÌNH VIRTUAL HOST CHO DISCOVERY SERVER
# ==========================================
# Tạo file cấu hình riêng trong thư mục sites-available của Nginx để che giấu port 8761
sudo vi /etc/nginx/sites-available/discoverserver

# --- Nội dung cấu hình dán vào bên trong file discoverserver ---
server {
    listen 80;
    server_name discoverserver.laptrinhfullstack.com;

    location / {
        proxy_pass http://127.0.0.1:8761; # Chuyển tiếp request ẩn port tới service chạy ngầm
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
# -------------------------------------------------------------
# (Trong vim: bấm phím i để soạn thảo, sau đó bấm Esc, gõ :wq và Enter để lưu và thoát)

# Kiểm tra lại nội dung file cấu hình vừa tạo
cat /etc/nginx/sites-available/discoverserver

# Tạo liên kết tượng trưng (symlink) kích hoạt cấu hình sang thư mục sites-enabled 
# (Đã sửa lỗi thiếu khoảng trắng sau -s)
sudo ln -s /etc/nginx/sites-available/discoverserver /etc/nginx/sites-enabled/

# Di chuyển đến thư mục chứa các site đang kích hoạt và kiểm tra
cd /etc/nginx/sites-enabled/
ls

# Kiểm tra lỗi cú pháp cấu hình của Nginx trước khi áp dụng
sudo nginx -t

# Khởi động lại dịch vụ Nginx để áp dụng cấu hình mới 
# (Đã sửa lỗi chính tả từ systemctk thành systemctl)
sudo systemctl restart nginx

#-> discoveserver.laptrinhfullstack.com:8467 hay discoveserver.laptrinhfullstack.com deu se ra
# -> da che dau dc port


