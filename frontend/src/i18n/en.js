
export default {
  common: {
    loading: 'Loading...',
    save: 'Save',
    cancel: 'Cancel',
    delete: 'Delete',
    edit: 'Edit',
    add: 'Add',
    confirm: 'Confirm',
    success: 'Success',
    error: 'Error',
    submit: 'Submit',
    close: 'Close',
    search: 'Search',
    filter: 'Filter',
    reset: 'Reset',
    actions: 'Actions',
    requests: 'requests',
    yes: 'Yes',
    no: 'No'
  },
  auth: {
    login: 'Login',
    register: 'Register',
    email: 'Email / Employee ID',
    password: 'Password',
    fullName: 'Full Name',
    role: 'Role',
    loginSuccess: 'Login successful!',
    loginError: 'Login failed. Please check your email or password.',
    registerSuccess: 'Registration successful. Please login to continue.',
    registerError: 'Registration failed. Please try again.',
    logout: 'Logout',
    forgotPassword: 'Forgot Password?',
    backToHome: 'Back to Home'
  },
  landing: {
    title: 'Attendance & Payroll Management System',
    titleShort: 'APMS HR',
    subtitle: 'Comprehensive solution for businesses',
    getStarted: 'Get Started',
    home: 'Home',
    features: {
      onlineAttendance: 'Online attendance tracking',
      leaveRequests: 'Leave request management',
      autoPayroll: 'Automated payroll calculation',
      reports: 'Reports & insights'
    }
  },
  home: {
    loggedInMessage: 'You are logged in. Go to the dashboard to view your information.',
    unauthenticatedMessage: 'Login or register to start using the app.',
    loginPrompt: 'Login to manage attendance and payroll.',
    createAccount: 'Create an account',
    createAccountDescription: 'Register a new Admin or Employee account.',
    start: 'Start'
  },
  admin: {
    dashboard: 'Admin Dashboard',
    dashboardShort: 'Admin',
    employees: 'Employees',
    attendance: 'Attendance',
    requests: 'Requests',
    payroll: 'Payroll',
    workShifts: 'Work Shifts',
    payrollConfig: 'Payroll Config',
    welcome: 'Welcome',
    todayStats: 'Today\'s Stats',
    statisticsOverview: 'Statistics Overview',
    filterDateTimeSubtitle: 'Filter attendance trends by date and time window.',
    startDate: 'Start date',
    endDate: 'End date',
    startTime: 'Start time',
    endTime: 'End time',
    totalStats: 'Total Stats',
    totalEmployees: 'Total Employees',
    present: 'Present',
    onLeave: 'On Leave',
    absent: 'Absent',
    pendingRequests: 'Pending Requests',
    totalPayroll: 'Payroll',
    currentMonth: 'Current Month'
  },
  adminPayroll: {
    title: 'Payroll Management',
    generate: 'Generate Payroll',
    month: 'Month {{month}}',
    generatedSuccess: 'Generated {{count}} payroll records successfully!',
    generatedNone: 'No new payroll was created. Make sure employees exist and payroll for this month has not been generated yet.',
    generatedError: 'Failed to generate payroll.',
    approveSuccess: 'Payroll approved successfully!',
    approveError: 'Failed to approve payroll.',
    markPaidSuccess: 'Payroll marked as paid successfully!',
    markPaidError: 'Failed to mark payroll as paid.',
    status: {
      pending: 'Pending',
      approved: 'Approved',
      paid: 'Paid'
    },
    table: {
      employee: 'Employee',
      month: 'Month',
      baseSalary: 'Base Salary',
      overtimePay: 'Overtime Pay',
      allowances: 'Allowances',
      insurance: 'Insurance',
      tax: 'Tax',
      deductions: 'Deductions',
      netSalary: 'Net Salary',
      status: 'Status'
    },
    actions: {
      approve: 'Approve',
      markPaid: 'Mark Paid'
    }
  },
  adminPayrollConfig: {
    title: 'Payroll Config Management',
    addConfig: 'Add Config',
    table: {
      overtimeMultiplier: 'Overtime multiplier',
      insuranceRate: 'Insurance rate (%)',
      taxRate: 'Tax rate (%)',
      status: 'Status'
    },
    status: {
      active: 'Active',
      inactive: 'Inactive'
    },
    actions: {
      setActive: 'Set as default'
    },
    modal: {
      addTitle: 'Add payroll config',
      editTitle: 'Edit payroll config',
      overtimeMultiplier: 'Overtime multiplier',
      insuranceRate: 'Insurance rate (%)',
      taxRate: 'Tax rate (%)',
      add: 'Add',
      update: 'Update'
    }
  },
  adminLeaveRequests: {
    title: 'Manage leave requests',
    promptRejectionReason: 'Enter the rejection reason:',
    table: {
      employee: 'Employee',
      leaveType: 'Leave type',
      startDate: 'Start date',
      endDate: 'End date',
      reason: 'Reason',
      status: 'Status'
    },
    status: {
      pending: 'Pending',
      approved: 'Approved',
      rejected: 'Rejected'
    },
    actions: {
      approve: 'Approve',
      reject: 'Reject'
    }
  },
  adminEmployees: {
    title: 'Manage employees',
    addEmployee: 'Add Employee',
    confirmDelete: 'Are you sure you want to delete this employee?',
    status: {
      active: 'Active',
      inactive: 'Inactive'
    },
    selectPosition: 'Select a position',
    selectDepartment: 'Select a department',
    selectWorkShift: 'Select a work shift',
    cccdLabel: 'ID card URL or upload',
    contractLabel: 'Contract URL or upload',
    viewCccd: 'View ID card',
    viewContract: 'View contract',
    activeLabel: 'Active',
    cancel: 'Cancel',
    save: 'Save',
    update: 'Update',
    modal: {
      addTitle: 'Add employee',
      editTitle: 'Edit employee'
    },
    uploadFailed: 'Upload failed for {{field}}.'
  },
  roles: {
    employee: 'Employee',
    admin: 'Admin'
  },
  employee: {
    dashboard: 'Employee Dashboard',
    dashboardShort: 'Employee',
    dashboardSubtitle: 'Your personal attendance and payroll overview.',
    checkInOut: 'Check In/Out',
    checkIn: 'Check In',
    checkOut: 'Check Out',
    remainingLeave: 'Remaining Leave',
    totalHours: 'Total Hours Worked',
    pendingRequests: 'Pending Requests',
    hours: 'hours',
    days: 'days',
    myAttendance: 'My Attendance',
    myRequests: 'My Requests',
    myPayroll: 'My Payroll',
    myProfile: 'My Profile',
    checkInSuccess: 'Check-in successful!',
    checkOutSuccess: 'Check-out successful!',
    alreadyCheckedIn: 'You already checked in!',
    notCheckedInYet: 'You haven\'t checked in yet!',
    attendanceFilterTitle: 'Attendance Filters',
    attendanceFilterSubtitle: 'Choose a date and time range to review your recent attendance activity.'
  },
  employeeRequests: {
    title: 'My Leave Requests',
    newRequest: 'New Request',
    successCreate: 'Leave request created successfully!',
    errorCreate: 'Failed to create leave request.',
    noRequests: 'You have no leave requests yet.',
    columns: {
      leaveType: 'Leave type',
      startDate: 'Start date',
      endDate: 'End date',
      reason: 'Reason',
      status: 'Status',
      rejectionReason: 'Rejection reason'
    },
    modal: {
      title: 'Create Leave Request',
      startDate: 'Start date',
      endDate: 'End date',
      leaveType: 'Leave type',
      reason: 'Reason',
      reasonPlaceholder: 'Enter your reason...',
      create: 'Create Request'
    },
    status: {
      pending: 'Pending',
      approved: 'Approved',
      rejected: 'Rejected'
    },
    leaveType: {
      annual: 'Annual leave',
      sick: 'Sick leave',
      personal: 'Personal leave',
      maternity: 'Maternity leave'
    }
  },
  employeeProfile: {
    title: 'Profile',
    basicInfo: 'Basic Information',
    workInfo: 'Work Information',
    fullName: 'Full Name',
    email: 'Email',
    staffId: 'Employee ID',
    phone: 'Phone',
    address: 'Address',
    dateOfBirth: 'Date of Birth',
    role: 'Role',
    position: 'Position',
    department: 'Department',
    leaveBalance: 'Leave Balance',
    hourlyRate: 'Hourly Rate',
    baseSalary: 'Base Salary',
    days: 'days',
    perHour: 'VND/hour',
    perMonth: 'VND/month',
    editProfile: 'Update Profile',
    editProfileSuccess: 'Profile updated successfully!',
    editProfileError: 'Failed to update profile.',
    modal: {
      title: 'Update Personal Profile',
      phone: 'Phone',
      address: 'Address',
      dateOfBirth: 'Date of Birth',
      save: 'Update',
      cancel: 'Cancel'
    }
  },
  employeePayslips: {
    title: 'My Payslips',
    noPayslips: 'You have no payslips yet.',
    payslipMonth: 'Payslip for',
    baseSalary: 'Base Salary',
    overtimePay: 'Overtime Pay',
    allowances: 'Allowances',
    insurance: 'Insurance',
    tax: 'Tax',
    deductions: 'Deductions',
    netSalary: 'Net Salary',
    status: {
      pending: 'Pending',
      approved: 'Approved',
      paid: 'Paid'
    }
  },
  employeeAttendance: {
    title: 'Attendance History',
    filterMonth: 'Month',
    totalMonthlyHours: 'Total hours this month',
    noRecords: 'No attendance records for this month',
    date: 'Date',
    checkIn: 'Check In',
    checkOut: 'Check Out',
    totalHours: 'Total Hours',
    status: 'Status',
    notes: 'Notes',
    hoursLabel: 'hours'
  },
  attendanceStatus: {
    present: 'Present',
    late: 'Late',
    absent: 'Absent',
    onLeave: 'On leave'
  },
  adminWorkShifts: {
    title: 'Work Shift Management',
    addShift: 'Add Work Shift',
    editShift: 'Edit Work Shift',
    deleteConfirm: 'Are you sure you want to delete this shift?',
    name: 'Shift Name',
    checkInTime: 'Check In Time',
    checkOutTime: 'Check Out Time',
    graceMinutes: 'Grace Minutes',
    overtimeHourlyRate: 'Overtime Hourly Rate (VND)',
    actions: 'Actions',
    cancel: 'Cancel',
    save: 'Save',
    update: 'Update',
    delete: 'Delete'
  },
  adminAttendance: {
    title: 'Attendance Management',
    employee: 'Employee',
    date: 'Date',
    checkIn: 'Check In',
    checkOut: 'Check Out',
    totalHours: 'Total Hours',
    overtimeHours: 'Overtime Hours',
    status: 'Status',
    notes: 'Notes',
    noRecords: 'No attendance records available.'
  },
  employeeForm: {
    titleAdd: 'Add Employee',
    titleEdit: 'Edit Employee',
    staffId: 'Employee ID',
    fullName: 'Full Name',
    phone: 'Phone',
    address: 'Address',
    dateOfBirth: 'Date of Birth',
    role: 'Role',
    hourlyRate: 'Hourly Rate',
    baseSalary: 'Base Salary',
    leaveBalance: 'Leave Balance',
    position: 'Position',
    department: 'Department',
    workShift: 'Work Shift',
    avatarUrl: 'Avatar URL',
    avatarFile: 'Upload Avatar',
    cccdUrl: 'ID Card URL',
    cccdFile: 'Upload ID Card',
    contractUrl: 'Contract URL',
    contractFile: 'Upload Contract',
    selectFile: 'Select File',
    preview: 'Preview'
  },
  employeeTable: {
    staffId: 'Emp ID',
    fullName: 'Full Name',
    email: 'Email',
    role: 'Role',
    position: 'Position',
    department: 'Department',
    workShift: 'Work Shift',
    leaveBalance: 'Leave Balance',
    status: 'Status',
    active: 'Active',
    inactive: 'Inactive',
    addEmployee: 'Add Employee'
  },
  attendance: {
    date: 'Date',
    checkIn: 'Check In',
    checkOut: 'Check Out',
    status: 'Status',
    notes: 'Notes',
    present: 'Present',
    absent: 'Absent',
    late: 'Late',
    halfDay: 'Half Day',
    onLeave: 'On Leave',
    errorData: 'Data Error',
    generatePayroll: 'Generate Payroll',
    month: 'Month',
    year: 'Year',
    approveCorrection: 'Approve Correction',
    rejectCorrection: 'Reject Correction',
    adminNotes: 'Admin Notes'
  },
  leaveRequest: {
    type: 'Request Type',
    startDate: 'Start Date',
    endDate: 'End Date',
    reason: 'Reason',
    status: 'Status',
    pending: 'Pending',
    approved: 'Approved',
    rejected: 'Rejected',
    paidLeave: 'Paid Leave',
    unpaidLeave: 'Unpaid Leave',
    addRequest: 'Create Leave Request',
    approve: 'Approve',
    reject: 'Reject',
    rejectionReason: 'Rejection Reason',
    noRequests: 'No requests yet',
    newRequest: 'Create Request',
    annualLeave: 'Annual Leave',
    sickLeave: 'Sick Leave',
    personalLeave: 'Personal Leave',
    maternityLeave: 'Maternity Leave'
  },
  overtimeRequest: {
    date: 'Overtime Date',
    startTime: 'Start Time',
    endTime: 'End Time',
    reason: 'Reason',
    addRequest: 'Create Overtime Request'
  },
  correctionRequest: {
    attendanceDate: 'Attendance Date',
    reason: 'Correction Reason',
    addRequest: 'Create Correction Request'
  },
  payroll: {
    monthYear: 'Month/Year',
    basicSalary: 'Basic Salary',
    allowances: 'Allowances',
    deductions: 'Deductions',
    overtimePay: 'Overtime Pay',
    insurance: 'Insurance',
    tax: 'Tax',
    totalSalary: 'Total Salary',
    status: 'Status',
    draft: 'Draft',
    finalized: 'Finalized',
    generate: 'Generate Payroll',
    finalize: 'Finalize Payroll',
    exportExcel: 'Export to Excel',
    exportPDF: 'Export to PDF',
    approve: 'Approve Payroll',
    markPaid: 'Mark as Paid'
  },
  workShift: {
    name: 'Shift Name',
    checkInTime: 'Check In Time',
    checkOutTime: 'Check Out Time',
    graceMinutes: 'Grace Period (minutes)',
    overtimeHourlyRate: 'Overtime Hourly Rate (VND)',
    addShift: 'Add Shift',
    editShift: 'Edit Shift',
    shiftList: 'Shift List'
  },
  payrollConfig: {
    overtimeMultiplier: 'Overtime Multiplier',
    insuranceRate: 'Insurance Rate (%)',
    taxRate: 'Tax Rate (%)',
    isActive: 'Status',
    addConfig: 'Add Config',
    editConfig: 'Edit Config',
    setActive: 'Set as Default',
    active: 'Active',
    inactive: 'Inactive'
  },
  userRoles: {
    admin: 'Admin',
    employee: 'Employee'
  }
};
