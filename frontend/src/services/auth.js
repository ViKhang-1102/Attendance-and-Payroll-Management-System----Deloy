import api from './api';

export const login = async (credentials) => {
  const response = await api.post('/api/auth/login', credentials);
  return response.data;
};

export const getProfile = async () => {
  const response = await api.get('/api/auth/profile');
  return response.data;
};

export const updateProfile = async (data) => {
  const response = await api.put('/api/auth/profile', data);
  return response.data;
};

export const registerUser = async (data) => {
  const response = await api.post('/api/auth/register', data);
  return response.data;
};

// User/Employee APIs
export const getUsers = async () => {
  const response = await api.get('/api/users');
  return response.data;
};

export const createUser = async (data) => {
  const response = await api.post('/api/users', data);
  return response.data;
};

export const updateUser = async (id, data) => {
  const response = await api.put(`/api/users/${id}`, data);
  return response.data;
};

export const deleteUser = async (id) => {
  const response = await api.delete(`/api/users/${id}`);
  return response.data;
};

// Attendance APIs
export const getAttendances = async () => {
  const response = await api.get('/api/attendances');
  return response.data;
};

export const getAttendancesByDateRange = async (startDate, endDate) => {
  const params = { startDate, endDate };
  const response = await api.get('/api/attendances/date-range', { params });
  return response.data;
};

export const getMyAttendances = async () => {
  const response = await api.get('/api/attendances/my-attendances');
  return response.data;
};

export const createAttendance = async (data) => {
  const response = await api.post('/api/attendances', data);
  return response.data;
};

export const checkInOut = async (notes) => {
  const params = notes ? { notes } : {};
  const response = await api.post('/api/attendances/check-in-out', null, { params });
  return response.data;
};

export const getTodayStats = async (date) => {
  const params = date ? { date } : {};
  const response = await api.get('/api/attendances/today-stats', { params });
  return response.data;
};

export const getPositions = async () => {
  const response = await api.get('/api/positions');
  return response.data;
};

export const getDepartments = async () => {
  const response = await api.get('/api/departments');
  return response.data;
};

// Leave Request APIs
export const getLeaveRequests = async () => {
  const response = await api.get('/api/leave-requests');
  return response.data;
};

export const getMyLeaveRequests = async () => {
  const response = await api.get('/api/leave-requests/my-requests');
  return response.data;
};

export const createLeaveRequest = async (data) => {
  const response = await api.post('/api/leave-requests', data);
  return response.data;
};

export const approveLeaveRequest = async (id, approve, rejectionReason) => {
  const response = await api.put(`/api/leave-requests/${id}/approve?approve=${approve}`, { rejectionReason });
  return response.data;
};

// Payroll APIs
export const getPayrolls = async () => {
  const response = await api.get('/api/payrolls');
  return response.data;
};

export const getMyPayrolls = async () => {
  const response = await api.get('/api/payrolls/my-payrolls');
  return response.data;
};

export const generatePayrollForAll = async (month, year) => {
  const response = await api.post(`/api/payrolls/generate/all?month=${month}&year=${year}`);
  return response.data;
};

export const updatePayroll = async (id, data) => {
  const response = await api.put(`/api/payrolls/${id}`, data);
  return response.data;
};

// Work Shift APIs
export const getWorkShifts = async () => {
  const response = await api.get('/api/work-shifts');
  return response.data;
};

export const createWorkShift = async (data) => {
  const response = await api.post('/api/work-shifts', data);
  return response.data;
};

export const updateWorkShift = async (id, data) => {
  const response = await api.put(`/api/work-shifts/${id}`, data);
  return response.data;
};

export const deleteWorkShift = async (id) => {
  const response = await api.delete(`/api/work-shifts/${id}`);
  return response.data;
};

// Correction Request APIs
export const getCorrectionRequests = async () => {
  const response = await api.get('/api/correction-requests');
  return response.data;
};

export const getMyCorrectionRequests = async () => {
  const response = await api.get('/api/correction-requests/my');
  return response.data;
};

export const createCorrectionRequest = async (data) => {
  const response = await api.post('/api/correction-requests', data);
  return response.data;
};

export const approveCorrectionRequest = async (id, adminNotes) => {
  const response = await api.post(`/api/correction-requests/${id}/approve?adminNotes=${adminNotes}`);
  return response.data;
};

export const rejectCorrectionRequest = async (id, rejectionReason) => {
  const response = await api.post(`/api/correction-requests/${id}/reject?rejectionReason=${rejectionReason}`);
  return response.data;
};

// Payroll Config APIs
export const getPayrollConfigs = async () => {
  const response = await api.get('/api/payroll-configs');
  return response.data;
};

export const getActivePayrollConfig = async () => {
  const response = await api.get('/api/payroll-configs/active');
  return response.data;
};

export const createPayrollConfig = async (data) => {
  const response = await api.post('/api/payroll-configs', data);
  return response.data;
};

export const updatePayrollConfig = async (id, data) => {
  const response = await api.put(`/api/payroll-configs/${id}`, data);
  return response.data;
};

export const setPayrollConfigActive = async (id) => {
  const response = await api.post(`/api/payroll-configs/${id}/set-active`);
  return response.data;
};

// File Upload API
export const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await api.post('/api/files/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
  
  return response.data;
};
