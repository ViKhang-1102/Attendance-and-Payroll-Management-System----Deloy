
# Attendance &amp; Payroll Management System

## Mục đích dự án
Dự án này được xây dựng để tự động hóa quy trình quản lý chấm công và tính lương cho doanh nghiệp, giúp giảm thiểu công sức thủ công, nâng cao độ chính xác, và cung cấp báo cáo chi tiết cho quản lý và nhân viên.

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.0.6
- Spring Security + JWT (Authentication &amp; Authorization)
- Spring Data JPA
- H2 Database (Embedded, for demo purposes)
- Lombok
- MapStruct

### Frontend
- React 18
- Vite
- Bootstrap 5
- React Router DOM
- Axios
- react-i18next (Đa ngôn ngữ)

## Bảo mật
1. **Xác thực và phân quyền**
   - Sử dụng Spring Security và JWT để xác thực người dùng
   - Phân quyền dựa trên vai trò (ADMIN / EMPLOYEE)
   - Mật khẩu được mã hóa bằng BCrypt
2. **Bảo vệ dữ liệu người dùng**
   - Không cho phép hard delete, chỉ cho soft delete (đặt `isActive = false`) để giữ lại lịch sử
   - File upload được lưu trong thư mục riêng, không thực thi trực tiếp
3. **CORS &amp; CSRF**
   - Cấu hình CORS để cho phép frontend truy cập backend
   - CSRF disabled cho các API dùng JWT

## Chức năng chính

### Phân hệ Admin
- Quản lý nhân viên (Thêm, sửa, xóa mềm, xem chi tiết)
- Quản lý chấm công (Xem bảng tổng hợp, duyệt đơn giải trình)
- Quản lý đơn từ (Xem, duyệt/từ chối đơn nghỉ phép, tăng ca)
- Quản lý lương (Tạo bảng lương, xem chi tiết, xuất file)
- Quản lý ca làm việc (Thêm, sửa, xóa ca)
- Cấu hình lương (Thêm/sửa/xóa cấu hình hệ số, tỷ lệ)

### Phân hệ Employee
- Chấm công (Check-in/Check-out)
- Xem lịch sử chấm công
- Tạo đơn từ (Đơn nghỉ phép, đơn tăng ca, đơn giải trình công)
- Xem phiếu lương
- Xem hồ sơ cá nhân

## Đa ngôn ngữ (i18n)
Hệ thống hỗ trợ 2 ngôn ngữ: Tiếng Việt (VI) và Tiếng Anh (EN), có thể chuyển đổi ngay trên navbar.

## Cài đặt và chạy dự án

> [!TIP]
> Để tìm hiểu chi tiết về cách cơ sở dữ liệu của dự án được lưu trữ ở đâu, cách reset dữ liệu và cách nạp DB mẫu, vui lòng tham khảo file [database_guide.md](file:///C:/xampp/htdocs/Projects/My/Attendance%20and%20Payroll%20Management%20System/database_guide.md).

### Yêu cầu hệ thống
- Java 21+
- Node.js 18+
- npm (hoặc yarn)
- Maven

### Chạy Backend
1. Vào thư mục backend:
   ```bash
   cd attendance
   ```
2. Biên dịch và chạy:
   ```bash
   mvn clean install -DskipTests
   mvn spring-boot:run
   ```
3. Backend sẽ chạy trên `http://localhost:8080`
4. Swagger UI (API docs) có thể truy cập tại `http://localhost:8080/swagger-ui.html`

> Mặc định backend chạy với H2 embedded để phát triển nhanh. Nếu bạn muốn chạy toàn bộ stack với Docker Compose, hãy đảm bảo môi trường `VITE_API_URL` và các biến `SPRING_DATASOURCE_*` trong `docker-compose.yml` đã được cấu hình đúng.

### Chạy với Docker Compose (tùy chọn)
1. Khởi động Docker Compose:
   ```bash
   docker compose up --build
   ```
2. Frontend sẽ truy cập tại `http://localhost:5173`
3. Backend sẽ truy cập tại `http://localhost:8080`
4. Cơ sở dữ liệu PostgreSQL được tạo trong Docker volume `postgres-data`

### Chạy Frontend local
1. Vào thư mục frontend:
   ```bash
   cd frontend
   ```
2. Cài đặt dependencies:
   ```bash
   npm install
   ```
3. Chạy dev server:
   ```bash
   npm run dev
   ```
4. Frontend sẽ chạy trên `http://localhost:5173` (hoặc cổng khác nếu cổng đã được dùng)

### Môi trường và biến cấu hình
Dự án đã chuyển sang dùng biến môi trường cho backend và frontend.
- Backend: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS`, `CORS_ALLOWED_ORIGINS`
- Frontend: `VITE_API_URL`

Bạn có thể sao chép file mẫu `.env.example` vào `.env` để chạy local.

### Triển khai production
#### 1) Frontend lên Vercel
- Chọn folder `frontend` làm source.
- Build command: `npm install && npm run build`
- Output directory: `dist`
- Thiết lập biến môi trường:
  - `VITE_API_URL=https://<backend-url>`

#### 2) Backend lên Render hoặc dịch vụ Docker
- Sử dụng `attendance/Dockerfile` hoặc build jar bằng Maven.
- Với Render (Docker):
  - Build command: `./mvnw -DskipTests package`
  - Start command: `java $JAVA_OPTS -jar target/attendance-0.0.1-SNAPSHOT.jar`
- Biến môi trường quan trọng:
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<database>`
  - `SPRING_DATASOURCE_USERNAME=<username>`
  - `SPRING_DATASOURCE_PASSWORD=<password>`
  - `JWT_SECRET=<secure-secret>`
  - `JWT_EXPIRATION_MS=86400000`
  - `CORS_ALLOWED_ORIGINS=https://<frontend-url>`

#### 3) Database lên Neon hoặc Dynamic Postgres
- Tạo database mới trên Neon / PostgreSQL.
- Dùng URL kết nối kiểu:
  ```text
  jdbc:postgresql://<host>:5432/<database>?sslmode=require
  ```
- Thiết lập `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` tương ứng.

#### 4) Khi dùng Vercel + Render + Neon
- Frontend trên Vercel: `VITE_API_URL=https://<backend-url>`
- Backend trên Render: cấu hình kết nối Neon bằng biến `SPRING_DATASOURCE_URL`
- Backend chỉ cho phép CORS từ frontend URL bằng `CORS_ALLOWED_ORIGINS`.

#### 5) Lưu ý deploy
- Không dùng H2 trong production.
- Cài `JWT_SECRET` đủ dài và bí mật.
- Thực thi `npm run build` cho frontend trước khi deploy lên host static.
- Với Docker Compose, cả stack chạy local qua `docker compose up --build`.

## Tài khoản mặc định để thử nghiệm
- **Admin**:
  - Email: `admin@company.com`
  - Mật khẩu: `admin123`
- **Nhân viên**:
  - Email: `employee1@company.com` đến `employee6@company.com`
  - Mật khẩu: `employee123`

> Lưu ý: Trường đăng nhập chấp nhận cả Email và Mã nhân viên.

## Dữ liệu mặc định ban đầu
- Chức vụ: Nhân viên, Quản lý, Giám đốc, Kế toán, Kỹ sư
- Phòng ban: IT, Kế toán, Marketing, Nhân sự
- Ca làm việc: Ca hành chính (08:00-17:00), Ca chiều (13:00-22:00)
- Cấu hình lương mặc định: Hệ số tăng ca 1.5, BH 10.5%, Thuế 20%

## Triển khai tương lai
1. **Thay đổi Database**: Chuyển sang PostgreSQL/MySQL để sử dụng trong môi trường sản xuất.
2. **Gửi email tự động**: Gửi email thông báo trạng thái đơn, phiếu lương, nhắc check-in/check-out.
3. **Xem biểu đồ thống kê**: Thêm các biểu đồ chấm công, lương, nghỉ phép cho admin và nhân viên.
4. **Đồng bộ dữ liệu**: Đồng bộ với các hệ thống ERP/HRMS khác.
5. **Đầu vào chấm công**: Thêm hỗ trợ chấm công bằng QR Code, NFC.
6. **Quản lý thời gian làm việc linh hoạt**: Hỗ trợ làm việc từ xa, tính lương theo dự án.
7. **Kiểm tra bảo mật nâng cao**: Thêm MFA (Xác thực hai yếu tố), kiểm tra IP truy cập.
8. **Triển khai nội bộ cho nhân viên**: Triển khai ứng dụng trong mạng nội bộ hoặc VPN để chỉ nhân viên trong công ty truy cập.
   - Dùng mạng riêng, private IP hoặc VPN thay vì mở cổng ra Internet.
   - Cấu hình firewall, reverse proxy và ACL để chặn truy cập từ bên ngoài.
   - Kết hợp xác thực nội bộ như LDAP/Active Directory hoặc SSO để chỉ user công ty được phép.
   - Tắt chức năng đăng ký mở/guest access và chỉ cho phép tài khoản nhân viên đã được tạo sẵn.
   - Nếu dùng Docker/Kubernetes, đặt ứng dụng trên mạng nội bộ và không expose dịch vụ ra public.

## Tối ưu hóa tài nguyên và dọn dẹp bộ nhớ (Windows)

Khi chạy hệ thống trên môi trường Windows, nếu gặp tình trạng tiêu tốn RAM hoặc ổ đĩa (ổ C bị đầy do WSL2 và Docker):

1. **Dọn dẹp tài nguyên Docker (nếu dùng Docker Compose)**:
   - Khởi động Docker Desktop.
   - Chạy lệnh dọn dẹp cache và các volumes không dùng để lấy lại hàng chục GB dung lượng:
     ```bash
     docker system prune -a --volumes -f
     ```
   - Tắt WSL2 để giải phóng RAM của tiến trình `vmmem` khi không sử dụng:
     ```bash
     wsl --shutdown
     ```

2. **Dọn dẹp và reset cơ sở dữ liệu mẫu (nếu chạy cục bộ)**:
   - Dữ liệu mẫu cục bộ (H2 Database) được lưu dưới dạng file tại thư mục `attendance/data/`.
   - Để xóa toàn bộ dữ liệu này và khởi tạo lại từ đầu, bạn chỉ cần tắt ứng dụng backend và xóa thư mục `data` này:
     ```bash
     # Trên Windows PowerShell
     Remove-Item -Recurse -Force ./attendance/data
     ```
