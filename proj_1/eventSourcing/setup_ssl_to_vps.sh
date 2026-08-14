# Đăng nhập vào máy chủ từ xa qua SSH
ssh root@103.126.161.94

# Cài đặt Certbot và plugin Nginx để tự động cấu hình SSL/TLS
sudo apt install -y certbot python3-certbot-nginx

# Chạy Certbot để xin chứng chỉ SSL và tự động cấu hình chuyển hướng HTTP sang HTTPS cho domain
sudo certbot --nginx -d discoverserver.laptrinhfullstack.com

# -> Nhận được chứng chỉ SSL (Certificate) thành công.
# Lưu ý: Chứng chỉ có thời hạn 3 tháng (90 ngày) và gần hết hạn Certbot sẽ tự động renew (gia hạn).
# Bây giờ domain discoverserver.laptrinhfullstack.com sẽ hoạt động hoàn toàn với giao thức HTTPS an toàn.