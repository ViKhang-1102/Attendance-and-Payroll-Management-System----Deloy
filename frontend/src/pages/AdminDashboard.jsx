
import { Link, Routes, Route, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { getUsers, getLeaveRequests, getPayrolls, getTodayStats } from '../services/auth';
import AdminEmployees from './admin/Employees';
import AdminAttendance from './admin/Attendance';
import AdminLeaveRequests from './admin/LeaveRequests';
import AdminPayroll from './admin/Payroll';
import AdminWorkShifts from './admin/WorkShifts';
import AdminPayrollConfig from './admin/PayrollConfig';

export default function AdminDashboard({ user, onLogout }) {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const [stats, setStats] = useState({ employees: 0, pendingRequests: 0, payrollMonths: 0, todayStats: null });
  const [filterDate, setFilterDate] = useState(new Date().toISOString().slice(0, 10));
  const [filterStartTime, setFilterStartTime] = useState('08:00');
  const [filterEndTime, setFilterEndTime] = useState('17:00');
  
  const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
  };

  useEffect(() => {
    fetchStats();
  }, []);

  useEffect(() => {
    fetchTodayStats();
  }, [filterDate]);

  const fetchStats = async () => {
    try {
      const [users, requests, payrolls, todayStats] = await Promise.all([
        getUsers(),
        getLeaveRequests(),
        getPayrolls(),
        getTodayStats(filterDate)
      ]);
      setStats({
        employees: users.filter(u => u.role !== 'ADMIN').length,
        pendingRequests: requests.filter(r => r.status === 'PENDING').length,
        payrollMonths: payrolls.length,
        todayStats: todayStats
      });
    } catch (err) {
      console.error('Failed to fetch stats:', err);
    }
  };

  const fetchTodayStats = async () => {
    try {
      const todayStats = await getTodayStats(filterDate);
      setStats(prev => ({ ...prev, todayStats }));
    } catch (err) {
      console.error('Failed to fetch today stats:', err);
    }
  };

  const handleLogout = () => {
    onLogout();
  };

  return (
    <div className="bg-light d-flex flex-column flex-grow-1">
      <nav className="navbar navbar-expand-lg navbar-dark bg-gradient shadow flex-shrink-0">
        <div className="container-fluid d-flex align-items-center justify-content-between">
          <Link className="navbar-brand fw-bold d-flex align-items-center" to="/admin">
            <i className="bi bi-shield-lock me-2 fs-4"></i>
            <span className="d-none d-sm-inline">{t('admin.dashboard')}</span>
            <span className="d-inline d-sm-none">{t('admin.dashboardShort')}</span>
          </Link>
          <button className="navbar-toggler ms-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav ms-auto align-items-center gap-2 flex-wrap">
              <li className="nav-item">
                <Link className="nav-link" to="/admin">{t('admin.dashboard')}</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin/employees">{t('admin.employees')}</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin/attendance">{t('admin.attendance')}</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin/requests">{t('admin.requests')}</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin/payroll">{t('admin.payroll')}</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin/work-shifts">{t('admin.workShifts')}</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin/payroll-config">{t('admin.payrollConfig')}</Link>
              </li>
              <li className="nav-item ms-2">
                <div className="btn-group btn-group-sm" role="group">
                  <button 
                    type="button" 
                    className={`btn ${i18n.language === 'vi' ? 'btn-light' : 'btn-outline-light'}`}
                    onClick={() => changeLanguage('vi')}
                  >
                    VI
                  </button>
                  <button 
                    type="button" 
                    className={`btn ${i18n.language === 'en' ? 'btn-light' : 'btn-outline-light'}`}
                    onClick={() => changeLanguage('en')}
                  >
                    EN
                  </button>
                </div>
              </li>
              <li className="nav-item ms-2">
                <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>{t('auth.logout')}</button>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <main className="container-fluid py-4 flex-grow-1">
        <Routes>
          <Route path="/" element={
            <div>
              <h1 className="mb-4">{t('admin.welcome')} {user?.fullName}!</h1>

              <h3 className="mb-3 text-secondary">
                {t('admin.todayStats')} ({new Date().toLocaleDateString(i18n.language === 'vi' ? 'vi-VN' : 'en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })})
              </h3>
              <div className="row g-4 mb-5">
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.totalEmployees')}</h5>
                      <h2 className="text-primary">{stats.todayStats?.totalEmployees || 0}</h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.present')}</h5>
                      <h2 className="text-success">{stats.todayStats?.presentCount || 0}</h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.onLeave')}</h5>
                      <h2 className="text-info">{stats.todayStats?.leaveCount || 0}</h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.absent')}</h5>
                      <h2 className="text-danger">{stats.todayStats?.absentCount || 0}</h2>
                    </div>
                  </div>
                </div>
              </div>

              <div className="card shadow-sm border-0 mb-5">
                <div className="card-body">
                  <div className="d-flex flex-column flex-md-row justify-content-between align-items-start gap-3">
                    <div>
                      <h5 className="mb-2">{t('admin.statisticsOverview')}</h5>
                      <p className="text-muted mb-3">{t('admin.filterDateTimeSubtitle')}</p>
                    </div>
                    <div className="d-flex flex-wrap gap-2 align-items-center">
                      <div className="form-group">
                        <label className="form-label small text-secondary mb-1">{t('admin.startDate')}</label>
                        <input type="date" className="form-control form-control-sm" value={filterDate} onChange={(e) => setFilterDate(e.target.value)} />
                      </div>
                      <div className="form-group">
                        <label className="form-label small text-secondary mb-1">{t('admin.startTime')}</label>
                        <input type="time" className="form-control form-control-sm" value={filterStartTime} onChange={(e) => setFilterStartTime(e.target.value)} />
                      </div>
                      <div className="form-group">
                        <label className="form-label small text-secondary mb-1">{t('admin.endTime')}</label>
                        <input type="time" className="form-control form-control-sm" value={filterEndTime} onChange={(e) => setFilterEndTime(e.target.value)} />
                      </div>
                    </div>
                  </div>

                  <div className="row g-3 mt-4">
                    <div className="col-md-4">
                      <div className="p-3 bg-light rounded-3 border">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                          <span className="text-muted">{t('admin.present')}</span>
                          <span className="fw-semibold">{stats.todayStats?.presentCount || 0}</span>
                        </div>
                        <div className="progress rounded-pill" style={{ height: '10px' }}>
                          <div className="progress-bar bg-primary" role="progressbar" style={{ width: `${Math.round(((stats.todayStats?.presentCount || 0) / Math.max((stats.todayStats?.presentCount || 0) + (stats.todayStats?.leaveCount || 0) + (stats.todayStats?.absentCount || 0), 1)) * 100)}%` }} aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-4">
                      <div className="p-3 bg-light rounded-3 border">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                          <span className="text-muted">{t('admin.onLeave')}</span>
                          <span className="fw-semibold">{stats.todayStats?.leaveCount || 0}</span>
                        </div>
                        <div className="progress rounded-pill" style={{ height: '10px' }}>
                          <div className="progress-bar bg-info" role="progressbar" style={{ width: `${Math.round(((stats.todayStats?.leaveCount || 0) / Math.max((stats.todayStats?.presentCount || 0) + (stats.todayStats?.leaveCount || 0) + (stats.todayStats?.absentCount || 0), 1)) * 100)}%` }} aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-4">
                      <div className="p-3 bg-light rounded-3 border">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                          <span className="text-muted">{t('admin.absent')}</span>
                          <span className="fw-semibold">{stats.todayStats?.absentCount || 0}</span>
                        </div>
                        <div className="progress rounded-pill" style={{ height: '10px' }}>
                          <div className="progress-bar bg-danger" role="progressbar" style={{ width: `${Math.round(((stats.todayStats?.absentCount || 0) / Math.max((stats.todayStats?.presentCount || 0) + (stats.todayStats?.leaveCount || 0) + (stats.todayStats?.absentCount || 0), 1)) * 100)}%` }} aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <h3 className="mb-3 text-secondary">{t('admin.totalStats')}</h3>
              <div className="row g-4 mb-5">
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.totalEmployees')}</h5>
                      <h2 className="text-primary">{stats.employees}</h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.pendingRequests')}</h5>
                      <h2 className="text-warning">{stats.pendingRequests}</h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.totalPayroll')}</h5>
                      <h2 className="text-success">{stats.payrollMonths}</h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <h5 className="text-muted">{t('admin.currentMonth')}</h5>
                      <h2 className="text-info">{new Date().getMonth() + 1}/{new Date().getFullYear()}</h2>
                    </div>
                  </div>
                </div>
              </div>

              <div className="row g-4">
                <div className="col-md-4">
                  <Link to="/admin/employees" className="text-decoration-none">
                    <div className="card shadow-sm h-100 hover-shadow">
                      <div className="card-body text-center p-4">
                        <i className="bi bi-people fs-1 text-primary mb-2"></i>
                        <h5 className="card-title mb-0">{t('admin.employees')}</h5>
                      </div>
                    </div>
                  </Link>
                </div>
                <div className="col-md-4">
                  <Link to="/admin/requests" className="text-decoration-none">
                    <div className="card shadow-sm h-100 hover-shadow">
                      <div className="card-body text-center p-4">
                        <i className="bi bi-file-earmark-check fs-1 text-warning mb-2"></i>
                        <h5 className="card-title mb-0">{t('admin.requests')}</h5>
                      </div>
                    </div>
                  </Link>
                </div>
                <div className="col-md-4">
                  <Link to="/admin/payroll" className="text-decoration-none">
                    <div className="card shadow-sm h-100 hover-shadow">
                      <div className="card-body text-center p-4">
                        <i className="bi bi-cash fs-1 text-success mb-2"></i>
                        <h5 className="card-title mb-0">{t('admin.payroll')}</h5>
                      </div>
                    </div>
                  </Link>
                </div>
              </div>
            </div>
          } />
          <Route path="/employees" element={<AdminEmployees />} />
          <Route path="/attendance" element={<AdminAttendance />} />
          <Route path="/requests" element={<AdminLeaveRequests />} />
          <Route path="/payroll" element={<AdminPayroll />} />
          <Route path="/work-shifts" element={<AdminWorkShifts />} />
          <Route path="/payroll-config" element={<AdminPayrollConfig />} />
        </Routes>
      </main>

      <style>{`
        .hover-shadow:hover {
          box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
          transform: translateY(-2px);
          transition: all 0.2s ease-in-out;
        }
        .bg-gradient {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
      `}</style>
    </div>
  );
}
