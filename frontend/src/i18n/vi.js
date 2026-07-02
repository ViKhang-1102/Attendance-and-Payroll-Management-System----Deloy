
export default {
  common: {
    loading: 'Đang tải...',
    save: 'Lưu',
    cancel: 'Hủy',
    delete: 'Xóa',
    edit: 'Sửa',
    add: 'Thêm',
    confirm: 'Xác nhận',
    success: 'Thành công',
    error: 'Lỗi',
    submit: 'Gửi',
    close: 'Đóng',
    search: 'Tìm kiếm',
    filter: 'Lọc',
    reset: 'Đặt lại',
    actions: 'Hành động',
    requests: 'đơn',
    yes: 'Có',
    no: 'Không'
  },
  auth: {
    login: 'Đăng nhập',
    register: 'Đăng ký',
    email: 'Email / Mã nhân viên',
    password: 'Mật khẩu',
    fullName: 'Họ và tên',
    role: 'Vai trò',
    loginSuccess: 'Đăng nhập thành công!',
    loginError: 'Đăng nhập thất bại. Kiểm tra lại email hoặc mật khẩu.',
    registerSuccess: 'Đăng ký thành công. Vui lòng đăng nhập để tiếp tục.',
    registerError: 'Đăng ký thất bại. Vui lòng thử lại.',
    logout: 'Đăng xuất',
    forgotPassword: 'Quên mật khẩu?',
    backToHome: 'Về trang chủ'
  },
  landing: {
    title: 'Hệ thống Quản lý Chấm công và Tính lương',
    titleShort: 'Chấm Công & Lương',
    subtitle: 'Giải pháp toàn diện cho doanh nghiệp',
    getStarted: 'Bắt đầu',
    home: 'Trang chủ',
    features: {
      onlineAttendance: 'Chấm công trực tuyến',
      leaveRequests: 'Quản lý đơn xin nghỉ phép',
      autoPayroll: 'Tính lương tự động',
      reports: 'Báo cáo & thống kê'
    }
  },
  home: {
    loggedInMessage: 'Bạn đã đăng nhập, chuyển sang Dashboard để xem thông tin người dùng.',
    unauthenticatedMessage: 'Đăng nhập hoặc đăng ký để bắt đầu sử dụng ứng dụng.',
    loginPrompt: 'Đăng nhập vào hệ thống để quản lý chấm công và bảng lương.',
    createAccount: 'Tạo tài khoản',
    createAccountDescription: 'Đăng ký tài khoản Admin hoặc Employee mới.',
    start: 'Bắt đầu'
  },
  admin: {
    dashboard: 'Trang quản trị',
    dashboardShort: 'Trang Admin',
    employees: 'Quản lý nhân viên',
    attendance: 'Bảng công',
    requests: 'Đơn từ',
    payroll: 'Bảng lương',
    workShifts: 'Ca làm',
    payrollConfig: 'Cấu hình lương',
    welcome: 'Chào mừng',
    todayStats: 'Thống kê hôm nay',
    statisticsOverview: 'Tổng quan thống kê',
    filterDateTimeSubtitle: 'Lọc xu hướng chấm công theo ngày và khung giờ.',
    startDate: 'Ngày bắt đầu',
    endDate: 'Ngày kết thúc',
    startTime: 'Giờ bắt đầu',
    endTime: 'Giờ kết thúc',
    totalStats: 'Thống kê tổng',
    totalEmployees: 'Tổng nhân viên',
    present: 'Đi làm',
    onLeave: 'Nghỉ phép',
    absent: 'Vắng không phép',
    pendingRequests: 'Đơn chờ duyệt',
    totalPayroll: 'Bảng lương',
    currentMonth: 'Tháng hiện tại'
  },
  adminPayroll: {
    title: 'Quản lý bảng lương',
    generate: 'Tạo bảng lương',
    month: 'Tháng {{month}}',
    generatedSuccess: 'Đã tạo {{count}} bảng lương thành công!',
    generatedNone: 'Không có bảng lương mới được tạo. Hãy kiểm tra xem đã có nhân viên chưa hoặc bảng lương tháng này đã được tạo rồi.',
    generatedError: 'Tạo bảng lương thất bại.',
    approveSuccess: 'Đã duyệt bảng lương thành công!',
    approveError: 'Duyệt bảng lương thất bại.',
    markPaidSuccess: 'Đã đánh dấu bảng lương đã thanh toán!',
    markPaidError: 'Đánh dấu thất bại.',
    status: {
      pending: 'Chờ xử lý',
      approved: 'Đã duyệt',
      paid: 'Đã thanh toán'
    },
    table: {
      employee: 'Nhân viên',
      month: 'Tháng',
      baseSalary: 'Lương cơ bản',
      overtimePay: 'Lương làm thêm',
      allowances: 'Phụ cấp',
      insurance: 'Bảo hiểm',
      tax: 'Thuế',
      deductions: 'Khấu trừ',
      netSalary: 'Thực nhận',
      status: 'Trạng thái'
    },
    actions: {
      approve: 'Duyệt',
      markPaid: 'Đánh dấu đã thanh toán'
    }
  },
  adminPayrollConfig: {
    title: 'Quản lý cấu hình tính lương',
    addConfig: 'Thêm cấu hình',
    table: {
      overtimeMultiplier: 'Hệ số tăng ca',
      insuranceRate: 'Phí bảo hiểm (%)',
      taxRate: 'Thuế (%)',
      status: 'Trạng thái'
    },
    status: {
      active: 'Đang dùng',
      inactive: 'Không dùng'
    },
    actions: {
      setActive: 'Đặt làm mặc định'
    },
    modal: {
      addTitle: 'Thêm cấu hình',
      editTitle: 'Sửa cấu hình',
      overtimeMultiplier: 'Hệ số tăng ca',
      insuranceRate: 'Phí bảo hiểm (%)',
      taxRate: 'Thuế (%)',
      add: 'Thêm',
      update: 'Cập nhật'
    }
  },
  adminLeaveRequests: {
    title: 'Quản lý đơn từ',
    promptRejectionReason: 'Nhập lý do từ chối:',
    table: {
      employee: 'Nhân viên',
      leaveType: 'Loại đơn',
      startDate: 'Ngày bắt đầu',
      endDate: 'Ngày kết thúc',
      reason: 'Lý do',
      status: 'Trạng thái'
    },
    status: {
      pending: 'Chờ duyệt',
      approved: 'Đã duyệt',
      rejected: 'Từ chối'
    },
    actions: {
      approve: 'Duyệt',
      reject: 'Từ chối'
    }
  },
  adminEmployees: {
    title: 'Quản lý nhân viên',
    addEmployee: 'Thêm nhân viên',
    confirmDelete: 'Bạn có chắc chắn muốn xóa nhân viên này?',
    status: {
      active: 'Hoạt động',
      inactive: 'Không hoạt động'
    },
    selectPosition: 'Chọn chức vụ',
    selectDepartment: 'Chọn phòng ban',
    selectWorkShift: 'Chọn ca làm',
    cccdLabel: 'CCCD (URL hoặc upload file)',
    contractLabel: 'Hợp đồng (URL hoặc upload file)',
    viewCccd: 'Xem CCCD',
    viewContract: 'Xem hợp đồng',
    activeLabel: 'Hoạt động',
    cancel: 'Hủy',
    save: 'Lưu',
    update: 'Cập nhật',
    modal: {
      addTitle: 'Thêm nhân viên',
      editTitle: 'Sửa nhân viên'
    },
    uploadFailed: 'Tải lên {{field}} thất bại.'
  },
  roles: {
    employee: 'Nhân viên',
    admin: 'Admin'
  },
  employee: {
    dashboard: 'Trang nhân viên',
    dashboardShort: 'Trang Nhân viên',
    dashboardSubtitle: 'Tóm tắt chấm công và lương cá nhân của bạn.',
    checkInOut: 'Chấm công',
    checkIn: 'Check In',
    checkOut: 'Check Out',
    remainingLeave: 'Ngày nghỉ còn lại',
    totalHours: 'Tổng giờ làm',
    pendingRequests: 'Đơn chờ duyệt',
    hours: 'giờ',
    days: 'ngày',
    myAttendance: 'Lịch sử chấm công',
    myRequests: 'Đơn của tôi',
    myPayroll: 'Phiếu lương',
    myProfile: 'Hồ sơ của tôi',
    checkInSuccess: 'Check-in thành công!',
    checkOutSuccess: 'Check-out thành công!',
    alreadyCheckedIn: 'Bạn đã check-in rồi!',
    notCheckedInYet: 'Bạn chưa check-in!',
    attendanceFilterTitle: 'Bộ lọc chấm công',
    attendanceFilterSubtitle: 'Chọn ngày và khung giờ để xem hoạt động chấm công gần đây.'
  },
  employeeRequests: {
    title: 'Đơn từ của tôi',
    newRequest: 'Tạo đơn mới',
    successCreate: 'Tạo đơn nghỉ phép thành công!',
    errorCreate: 'Tạo đơn nghỉ phép thất bại!',
    noRequests: 'Bạn chưa có đơn từ nào',
    columns: {
      leaveType: 'Loại đơn',
      startDate: 'Ngày bắt đầu',
      endDate: 'Ngày kết thúc',
      reason: 'Lý do',
      status: 'Trạng thái',
      rejectionReason: 'Lý do từ chối'
    },
    modal: {
      title: 'Tạo đơn nghỉ phép',
      startDate: 'Ngày bắt đầu',
      endDate: 'Ngày kết thúc',
      leaveType: 'Loại nghỉ',
      reason: 'Lý do',
      reasonPlaceholder: 'Nhập lý do nghỉ...',
      create: 'Tạo đơn'
    },
    status: {
      pending: 'Chờ duyệt',
      approved: 'Đã duyệt',
      rejected: 'Từ chối'
    },
    leaveType: {
      annual: 'Nghỉ phép hàng năm',
      sick: 'Nghỉ bệnh',
      personal: 'Nghỉ riêng',
      maternity: 'Nghỉ thai sản'
    }
  },
  employeeProfile: {
    title: 'Hồ sơ cá nhân',
    basicInfo: 'Thông tin cơ bản',
    workInfo: 'Thông tin công việc',
    fullName: 'Họ và tên',
    email: 'Email',
    staffId: 'Mã nhân viên',
    phone: 'Số điện thoại',
    address: 'Địa chỉ',
    dateOfBirth: 'Ngày sinh',
    role: 'Vai trò',
    position: 'Chức vụ',
    department: 'Phòng ban',
    leaveBalance: 'Ngày nghỉ phép',
    hourlyRate: 'Lương theo giờ',
    baseSalary: 'Lương cơ bản',
    days: 'ngày',
    perHour: 'VND/giờ',
    perMonth: 'VND/tháng',
    editProfile: 'Cập nhật hồ sơ',
    editProfileSuccess: 'Cập nhật hồ sơ thành công!',
    editProfileError: 'Cập nhật hồ sơ thất bại.',
    modal: {
      title: 'Cập nhật hồ sơ cá nhân',
      phone: 'Số điện thoại',
      address: 'Địa chỉ',
      dateOfBirth: 'Ngày sinh',
      save: 'Cập nhật',
      cancel: 'Hủy'
    }
  },
  employeePayslips: {
    title: 'Phiếu lương của tôi',
    noPayslips: 'Bạn chưa có phiếu lương nào',
    payslipMonth: 'Phiếu lương tháng',
    baseSalary: 'Lương cơ bản',
    overtimePay: 'Lương làm thêm',
    allowances: 'Phụ cấp',
    insurance: 'Bảo hiểm',
    tax: 'Thuế',
    deductions: 'Khấu trừ',
    netSalary: 'Thực nhận',
    status: {
      pending: 'Chờ xử lý',
      approved: 'Đã duyệt',
      paid: 'Đã thanh toán'
    }
  },
  employeeAttendance: {
    title: 'Lịch sử chấm công',
    filterMonth: 'Tháng',
    totalMonthlyHours: 'Tổng giờ làm trong tháng',
    noRecords: 'Không có bản ghi chấm công nào trong tháng này',
    date: 'Ngày',
    checkIn: 'Giờ vào',
    checkOut: 'Giờ ra',
    totalHours: 'Tổng giờ',
    status: 'Trạng thái',
    notes: 'Ghi chú',
    hoursLabel: 'giờ'
  },
  attendanceStatus: {
    present: 'Có mặt',
    late: 'Đi muộn',
    absent: 'Vắng mặt',
    onLeave: 'Nghỉ phép'
  },
  adminWorkShifts: {
    title: 'Quản lý ca làm việc',
    addShift: 'Thêm ca làm',
    editShift: 'Sửa ca làm',
    deleteConfirm: 'Bạn có chắc chắn muốn xóa ca làm này?',
    name: 'Tên ca',
    checkInTime: 'Giờ vào',
    checkOutTime: 'Giờ ra',
    graceMinutes: 'Thời gian chờ (phút)',
    overtimeHourlyRate: 'Lương OT/giờ (VNĐ)',
    actions: 'Hành động',
    cancel: 'Hủy',
    save: 'Lưu',
    update: 'Cập nhật',
    delete: 'Xóa'
  },
  adminAttendance: {
    title: 'Quản lý bảng công',
    employee: 'Nhân viên',
    date: 'Ngày',
    checkIn: 'Giờ vào',
    checkOut: 'Giờ ra',
    totalHours: 'Tổng giờ',
    overtimeHours: 'Giờ OT',
    status: 'Trạng thái',
    notes: 'Ghi chú',
    noRecords: 'Không có bản ghi chấm công nào'
  },
  employeeForm: {
    titleAdd: 'Thêm nhân viên',
    titleEdit: 'Sửa nhân viên',
    staffId: 'Mã nhân viên',
    fullName: 'Họ tên',
    phone: 'Số điện thoại',
    address: 'Địa chỉ',
    dateOfBirth: 'Ngày sinh',
    role: 'Vai trò',
    hourlyRate: 'Lương theo giờ',
    baseSalary: 'Lương cơ bản',
    leaveBalance: 'Ngày nghỉ phép',
    position: 'Chức vụ',
    department: 'Phòng ban',
    workShift: 'Ca làm',
    avatarUrl: 'URL ảnh đại diện',
    avatarFile: 'Tải ảnh đại diện',
    cccdUrl: 'URL CCCD',
    cccdFile: 'Tải CCCD',
    contractUrl: 'URL hợp đồng',
    contractFile: 'Tải hợp đồng',
    selectFile: 'Chọn file',
    preview: 'Xem trước'
  },
  employeeTable: {
    staffId: 'Mã NV',
    fullName: 'Họ tên',
    email: 'Email',
    role: 'Vai trò',
    position: 'Chức vụ',
    department: 'Phòng ban',
    workShift: 'Ca làm',
    leaveBalance: 'Ngày nghỉ còn lại',
    status: 'Trạng thái',
    active: 'Hoạt động',
    inactive: 'Không hoạt động',
    addEmployee: 'Thêm nhân viên'
  },
  attendance: {
    date: 'Ngày',
    checkIn: 'Giờ vào',
    checkOut: 'Giờ ra',
    status: 'Trạng thái',
    notes: 'Ghi chú',
    present: 'Đi làm',
    absent: 'Vắng',
    late: 'Đi muộn',
    halfDay: 'Nửa ngày',
    onLeave: 'Nghỉ phép',
    errorData: 'Lỗi dữ liệu',
    generatePayroll: 'Tạo bảng lương',
    month: 'Tháng',
    year: 'Năm',
    approveCorrection: 'Duyệt đơn giải trình',
    rejectCorrection: 'Từ chối đơn giải trình',
    adminNotes: 'Ghi chú admin'
  },
  leaveRequest: {
    type: 'Loại đơn',
    startDate: 'Ngày bắt đầu',
    endDate: 'Ngày kết thúc',
    reason: 'Lý do',
    status: 'Trạng thái',
    pending: 'Chờ duyệt',
    approved: 'Đã duyệt',
    rejected: 'Đã từ chối',
    paidLeave: 'Nghỉ phép có lương',
    unpaidLeave: 'Nghỉ không lương',
    addRequest: 'Tạo đơn nghỉ',
    approve: 'Duyệt',
    reject: 'Từ chối',
    rejectionReason: 'Lý do từ chối',
    noRequests: 'Chưa có đơn từ nào',
    newRequest: 'Tạo đơn mới',
    annualLeave: 'Nghỉ phép hàng năm',
    sickLeave: 'Nghỉ bệnh',
    personalLeave: 'Nghỉ riêng',
    maternityLeave: 'Nghỉ thai sản'
  },
  overtimeRequest: {
    date: 'Ngày tăng ca',
    startTime: 'Giờ bắt đầu',
    endTime: 'Giờ kết thúc',
    reason: 'Lý do',
    addRequest: 'Tạo đơn tăng ca'
  },
  correctionRequest: {
    attendanceDate: 'Ngày chấm công',
    reason: 'Lý do giải trình',
    addRequest: 'Tạo đơn giải trình'
  },
  payroll: {
    monthYear: 'Tháng/Năm',
    basicSalary: 'Lương cơ bản',
    allowances: 'Phụ cấp',
    deductions: 'Khấu trừ',
    overtimePay: 'Tiền tăng ca',
    insurance: 'Bảo hiểm',
    tax: 'Thuế',
    totalSalary: 'Tổng lương',
    status: 'Trạng thái',
    draft: 'Nháp',
    finalized: 'Đã chốt',
    generate: 'Tạo bảng lương',
    finalize: 'Chốt lương',
    exportExcel: 'Xuất Excel',
    exportPDF: 'Xuất PDF',
    approve: 'Duyệt bảng lương',
    markPaid: 'Đánh dấu đã thanh toán'
  },
  workShift: {
    name: 'Tên ca',
    checkInTime: 'Giờ vào',
    checkOutTime: 'Giờ ra',
    graceMinutes: 'Thời gian chờ (phút)',
    overtimeHourlyRate: 'Lương OT/giờ (VNĐ)',
    addShift: 'Thêm ca làm',
    editShift: 'Sửa ca làm',
    shiftList: 'Danh sách ca làm'
  },
  payrollConfig: {
    overtimeMultiplier: 'Hệ số tăng ca',
    insuranceRate: 'Phí bảo hiểm (%)',
    taxRate: 'Thuế (%)',
    isActive: 'Trạng thái',
    addConfig: 'Thêm cấu hình',
    editConfig: 'Sửa cấu hình',
    setActive: 'Đặt làm mặc định',
    active: 'Đang hoạt động',
    inactive: 'Không hoạt động'
  },
  userRoles: {
    admin: 'Admin',
    employee: 'Nhân viên'
  }
};
