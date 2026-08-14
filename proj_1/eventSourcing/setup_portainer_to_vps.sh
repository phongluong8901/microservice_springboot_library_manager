ssh root@103.126.161.94

# 1. Tạo volume để lưu trữ dữ liệu Portainer
docker volume create portainer_data

# 2. Chạy Portainer container
docker run -d -p 8000:8000 -p 9443:9443 --name portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data portainer/portainer-ce:latest

docker ps -a

# truy cap public portainer (admin - admin)
103.126.161.94:9000

#
