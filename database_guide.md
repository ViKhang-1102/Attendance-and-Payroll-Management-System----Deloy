# Hướng Dẫn Cơ Sở Dữ Liệu (Database Guide)

Tài liệu này giải thích chi tiết cách hệ thống quản lý, lưu trữ cơ sở dữ liệu (DB) ở cả hai môi trường chạy cục bộ và chạy bằng Docker, cách reset dữ liệu và các quy tắc nạp dữ liệu mẫu để bạn không bị bối rối.

---

## 1. Hệ thống dùng Cơ sở dữ liệu nào và Lưu ở đâu?

Hệ thống có hai môi trường cấu hình DB độc lập tương ứng với cách bạn chạy ứng dụng:

### Môi trường A: Chạy cục bộ (Chạy trực tiếp bằng Java Maven/IDE)
* **Loại cơ sở dữ liệu**: H2 Database (File-based) - là hệ quản trị cơ sở dữ liệu nhúng siêu nhẹ.
* **Nơi lưu trữ**: Dữ liệu được lưu dưới dạng file trực tiếp trong thư mục dự án tại đường dẫn:
  `attendance/data/attendance.mv.db`
* **Xóa dữ liệu được không?**
  **ĐƯỢC PHÉP XÓA HOÀN TOÀN.** 
  Khi bạn muốn reset toàn bộ hệ thống hoặc gặp lỗi đăng nhập, hãy thực hiện:
  1. Tắt backend Spring Boot (Java).
  2. Xóa toàn bộ thư mục `attendance/data/` đi.
  3. Khởi động lại backend, hệ thống sẽ tự động tạo lại tệp cơ sở dữ liệu mới tinh và chạy lại class khởi tạo dữ liệu mẫu.

### Môi trường B: Chạy bằng Docker (`docker-compose`)
* **Loại cơ sở dữ liệu**: PostgreSQL 15.
* **Nơi lưu trữ**: Dữ liệu được lưu trong **Docker Volume** tên là `postgres-data` trên máy ảo Docker (không lưu trực tiếp dạng file trong thư mục dự án của bạn).
* **Cách xóa/reset dữ liệu**: 
  Bạn không thể xóa file thủ công như chạy cục bộ. Thay vào đó, hãy chạy lệnh sau trong thư mục dự án:
  ```bash
  docker compose down -v
  ```
  *(Tham số `-v` sẽ xóa sạch phân vùng đĩa ảo Postgres của hệ thống. Lần sau chạy `docker compose up` sẽ tạo lại DB mới tinh)*.

---

## 2. Dữ liệu mẫu (DB mẫu) được tạo ra và lưu trữ như thế nào?

Khi cơ sở dữ liệu trống hoàn toàn (sau khi bạn xóa thư mục `data` hoặc reset docker volume):
1. Lần khởi động ứng dụng đầu tiên, class [DataInitializer.java](file:///C:/xampp/htdocs/Projects/My/Attendance%20and%20Payroll%20Management%20System/attendance/src/main/java/com/company/attendance/config/DataInitializer.java) ở backend sẽ tự động kích hoạt.
2. Nó kiểm tra nếu chưa có bản ghi chấm công nào (`attendanceRepository.count() == 0`), nó sẽ tự động tạo:
   * **Tài khoản**: 1 Admin (`admin@company.com` / `admin123`) và 6 Nhân viên mẫu (`employee1@company.com` đến `employee6@company.com` / mật khẩu đều là `employee123`).
   * **Cấu hình ca làm và phòng ban**: IT Department, Ca hành chính, v.v.
   * **Lịch sử chấm công (15 ngày gần nhất đến HÔM NAY)**: Tạo ra các trạng thái Đi làm đầy đủ, Đi muộn, Tăng ca, Vắng mặt không phép và Nghỉ phép.
   * **Lịch sử đơn từ**: Tạo đơn xin nghỉ phép và đơn giải trình chấm công mẫu (Đã duyệt, Đã từ chối, và Đang chờ duyệt).
   * **Phiếu lương lịch sử**: Tạo sẵn bảng lương đã chi trả cho Tháng 4 và Tháng 5 năm 2026.
3. Toàn bộ dữ liệu mẫu này sau khi tạo xong sẽ được lưu vĩnh viễn vào tệp DB nhúng (H2) hoặc volume (PostgreSQL).
