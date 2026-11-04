# 🚀 NAS Cloud Drive & Secure Vault (ServerNAS)

> **Hệ thống Lưu trữ Đám mây Cá nhân Tốc độ cao** với giao diện phong cách **Google Drive**, tích hợp **Két sắt Bí mật chuẩn mã hóa quân đội AES-256-GCM (Zero-Knowledge)**, Trình phát Video/Ảnh trực tiếp, Tải tệp phân đoạn lớn (Chunked Resumable Upload) và **Telegram Bot Watchdog** giám sát 24/7.

[![Node.js](https://img.shields.io/badge/Node.js-20+-green.svg)](https://nodejs.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Security](https://img.shields.io/badge/Encryption-AES--256--GCM-red.svg)]()
[![UI](https://img.shields.io/badge/Theme-Dark_Glassmorphism-9cf.svg)]()

---

## 🌟 Tính Năng Nổi Bật

### 1. 🗄️ Lưu trữ & Quản lý Tệp chuẩn Google Drive
- **Giao diện Glassmorphic**: Thiết kế tối ưu cho cả Desktop và Mobile với hiệu ứng kính mờ (Backdrop Blur), chuyển đổi linh hoạt dạng Lưới (Grid) và Danh sách (List).
- **Infinite Scrolling & Tải nhanh (0ms UI)**: Tối ưu hóa DOM, render mượt mà hàng nghìn tệp tin với bộ đệm bộ nhớ cục bộ.
- **Tải lên Siêu tốc (Chunked & Direct Stream)**:
  - Tự động nhận diện mạng LAN Gigabit để đẩy trực tiếp 100% băng thông.
  - Tự động chia khối (chunking 25MB) khi upload qua Cloudflare Tunnel để vượt giới hạn Cloudflare 100MB body limit.
  - Thanh tiến trình thời gian thực, tính toán tốc độ MB/s chính xác.
- **Xem trước & Trình phát đa phương tiện**:
  - Hỗ trợ Stream video MP4, MKV, MOV kèm tua thời gian thực (HTTP Range Requests 206).
  - Trình xem ảnh Lightbox độ phân giải cao.
  - Trình xem file văn bản, mã nguồn trực tiếp.

### 2. 🔐 Két sắt Bí mật (Encrypted Vault - Zero-Knowledge)
- **Chuẩn mã hóa NIST AES-256-GCM**:
  - Mã hóa toàn vẹn cả nội dung dữ liệu lẫn siêu dữ liệu (tên tệp gốc, kích thước, định dạng MIME).
  - Trên đĩa vật lý của máy chủ, toàn bộ tệp trong Két sắt chỉ là các khối ciphertext nhị phân mang ID ngẫu nhiên (`.enc`), không thể bị đọc trộm ngay cả khi máy chủ hoặc ổ cứng bị đánh cắp.
- **Chìa khóa bộ nhớ RAM (RAM-Only & Auto-Lock)**:
  - Chìa khóa giải mã 256-bit được sinh qua thuật toán **Scrypt** (`N=32768, r=8, p=1`) từ mật khẩu Két sắt riêng biệt của bạn.
  - Chìa khóa chỉ tồn tại tạm thời trong RAM của máy chủ và **tự động xóa sạch sau 15 phút không hoạt động** hoặc khi bấm nút "Khóa Két sắt".
- **Xem & Tải giải mã On-The-Fly**: Stream và giải mã trực tiếp trong RAM khi phát video hoặc xem ảnh mà không ghi file giải mã tạm xuống đĩa.

### 3. 🤖 Telegram Bot Giám sát Máy chủ (Server Watchdog)
- Tự động khởi chạy song song dưới dạng Systemd service.
- **Thông báo thời gian thực**: Cảnh báo tức thì khi máy chủ khởi động lại, dịch vụ NAS bị crash hoặc dung lượng ổ đĩa vượt quá 90%.
- **Báo cáo định kỳ hàng ngày**: Tự động gửi báo cáo sức khỏe hệ thống (CPU, RAM, Disk, Uptime) vào 08:00 sáng mỗi ngày.
- **Tự động phục hồi (Self-Healing)**: Tự động khởi động lại service chính nếu phát hiện tiến trình dừng đột ngột.

### 4. 📱 Ứng dụng Android (APK đi kèm)
- Đi kèm tệp cài đặt `frontend/NASDrive.apk` cho điện thoại Android, kết nối trực tiếp vào máy chủ lưu trữ.

---

## 🏗️ Cấu Trúc Dự Án

```
servernas/
├── backend/
│   ├── server.mjs                    # Máy chủ chính (API, Auth, Streaming, Vault Engine)
│   ├── telegram-bot.mjs              # Bot Telegram giám sát & cảnh báo máy chủ
│   ├── telegram-config.example.json  # File cấu hình mẫu cho Telegram Bot
│   └── package.json                  # Cấu hình backend
├── frontend/
│   ├── index.html                    # Giao diện chính (HTML5 Dark Glassmorphic)
│   ├── app.js                        # Logic xử lý giao diện, Auth, Upload & Vault
│   ├── style.css                     # Hệ thống Design Tokens & Styles
│   ├── NASDrive.apk                  # Ứng dụng Android client
│   └── package.json                  # Cấu hình frontend
├── services/
│   ├── nas-drive.service             # Systemd service cho máy chủ NAS
│   └── nas-telegram-bot.service      # Systemd service cho Telegram Bot
├── telegram-config.example.json      # File cấu hình mẫu bot Telegram
├── package.json                      # Cấu hình root dự án
└── README.md                         # Tài liệu hướng dẫn sử dụng
```

---

## ⚡ Hướng Dẫn Cài Đặt & Chạy

### 1. Yêu cầu Hệ thống
- Hệ điều hành: Linux (Debian, Ubuntu, v.v.) hoặc Windows/macOS
- Node.js >= 20.0.0
- Ổ đĩa lưu trữ (mặc định `/mnt/nas/drive` hoặc tùy chỉnh qua biến môi trường)

### 2. Cài đặt & Khởi động Nhanh

```bash
# Clone dự án
git clone https://github.com/nguyennam1234/servernas.git
cd servernas

# Cài đặt (nếu cần thiết, dự án dùng thuần Node.js native không phụ thuộc thư viện ngoài)
cd backend

# Khởi chạy máy chủ
STORAGE_ROOT=/path/to/storage PORT=8080 node server.mjs
```

Sau khi chạy, truy cập vào: `http://localhost:8080`.

> **Tài khoản Quản trị mặc định**:
> - Tên đăng nhập: `admin`
> - Mật khẩu: `Admin@NAS2026!` *(Vui lòng đổi mật khẩu ngay sau lần đăng nhập đầu tiên tại menu góc trên bên phải)*.

---

## 🛠️ Cài đặt Dịch vụ Hệ thống (Systemd trên Linux)

Để máy chủ NAS tự động chạy nền và khởi động cùng hệ thống:

```bash
# 1. Sao chép các file service vào systemd
sudo cp services/nas-drive.service /etc/systemd/system/
sudo cp services/nas-telegram-bot.service /etc/systemd/system/

# 2. Cấu hình Telegram Bot (nếu sử dụng)
cp backend/telegram-config.example.json backend/telegram-config.json
nano backend/telegram-config.json # Điền botToken và chatId

# 3. Kích hoạt và khởi động
sudo systemctl daemon-reload
sudo systemctl enable --now nas-drive.service
sudo systemctl enable --now nas-telegram-bot.service

# 4. Kiểm tra trạng thái
sudo systemctl status nas-drive.service
sudo systemctl status nas-telegram-bot.service
```

---

## 🌐 Triển khai Public qua Cloudflare Tunnel

Để truy cập an toàn từ Internet mà không cần mở port router (NAT/Port Forwarding):

```bash
# Cài đặt cloudflared
curl -L --output cloudflared.deb https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-linux-amd64.deb
sudo dpkg -i cloudflared.deb

# Kết nối tunnel tới cổng local 8080
cloudflared tunnel run <TÊN_TUNNEL>
```

---

## 🔒 Bảo Mật & An Ninh

1. **Không phụ thuộc thư viện bên ngoài (Zero-Dependency)**: Toàn bộ engine backend sử dụng các module tiêu chuẩn của Node.js (`node:crypto`, `node:fs`, `node:http`, `node:stream`), triệt tiêu 100% rủi ro từ lỗ hổng chuỗi cung ứng npm (Supply Chain Attacks).
2. **Xác thực phiên làm việc bền vững**: Session token được ký băm mã hóa, lưu phân quyền chặt chẽ `0600` trên ổ đĩa.
3. **Phân vùng tệp mã hóa**: Két sắt (Vault) phân lập hoàn toàn với thư mục Drive chính.

---

## 📄 Bản quyền (License)

Dự án được phân phối dưới giấy phép **MIT License**.
Phát triển bởi **Nguyen Nam**.
