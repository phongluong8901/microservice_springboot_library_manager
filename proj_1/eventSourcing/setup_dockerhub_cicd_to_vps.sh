# Đăng nhập vào máy chủ từ xa qua SSH
ssh root@103.126.161.94

#
cd app

# github - settings - keys - SSH and GPG keys - new SSH - Add SSH key
# clone ssh vao vps
#

git clone git@github.com:phongluong8901/microservice_springboot_library_manager.git

cd microservice_springboot_library_manager
ls

# check cac nhanh
git fetch -a

cd ~/.ssh

ls
#
cat authorized_keys

# lay key tai may tinh local - pass them vao vps key - wq


