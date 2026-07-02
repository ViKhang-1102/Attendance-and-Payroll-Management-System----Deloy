
import { Link, Routes, Route, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { getMyAttendances, getMyLeaveRequests, checkInOut } from '../services/auth';
import EmployeeAttendance from './employee/Attendance';
import EmployeeRequests from './employee/Requests';
import EmployeePayslips from './employee/Payslips';
import EmployeeProfile from './employee/Profile';

export default function EmployeeDashboard({ user, onLogout, onProfileUpdate }) {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const [stats, setStats] = useState({ leaveBalance: 0, totalHours: 0, pendingRequests: 0 });
  const [todayAttendance, setTodayAttendance] = useState(null);
  const [checkingInOut, setCheckingInOut] = useState(false);
  const [filterDate, setFilterDate] = useState(new Date().toISOString().split('T')[0]);
  const [filterStartTime, setFilterStartTime] = useState('08:00');
  const [filterEndTime, setFilterEndTime] = useState('17:00');
  
  const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
  };

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const [attendances, requests] = await Promise.all([
        getMyAttendances(),
        getMyLeaveRequests()
      ]);
      const totalHours = attendances.reduce((sum, a) => sum + (a.hoursWorked || 0), 0);
      const today = new Date().toISOString().split('T')[0];
      const todayAtt = attendances.find(a => {
        const attDate = new Date(a.date).toISOString().split('T')[0];
        return attDate === today;
      });
      setTodayAttendance(todayAtt);
      setStats({
        leaveBalance: user?.leaveBalance || 0,
        totalHours: totalHours,
        pendingRequests: requests.filter(r => r.status === 'PENDING').length
      });
    } catch (err) {
      console.error('Failed to fetch stats:', err);
    }
  };

  const handleCheckInOut = async () => {
    try {
      setCheckingInOut(true);
      await checkInOut('');
      await fetchStats();
      alert(t('employee.checkInSuccess'));
    } catch (err) {
      console.error('Failed to check in/out:', err);
      alert(err.response?.data?.message || t('common.error'));
    } finally {
      setCheckingInOut(false);
    }
  };

  const handleLogout = () => {
    onLogout();
  };

  return (
    <div className="bg-light d-flex flex-column flex-grow-1">
      <nav className="navbar navbar-expand-lg navbar-dark bg-gradient shadow flex-shrink-0">
        <div className="container-fluid d-flex align-items-center justify-content-between">
          <Link className="navbar-brand fw-bold d-flex align-items-center" to="/employee">
            <i className="bi bi-building me-2 fs-4"></i>
            <span className="d-none d-sm-inline">{t('employee.dashboard')}</span>
            <span className="d-inline d-sm-none">{t('employee.dashboardShort')}</span>
          </Link>
          <button className="navbar-toggler ms-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav ms-auto align-items-center gap-2 flex-wrap">
              <li className="nav-item">
                <Link className="nav-link" to="/employee">
                  <i className="bi bi-house me-1"></i> {t('employee.dashboard')}
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/employee/attendance">
                  <i className="bi bi-calendar-check me-1"></i> {t('employee.myAttendance')}
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/employee/requests">
                  <i className="bi bi-file-earmark-text me-1"></i> {t('employee.myRequests')}
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/employee/payslips">
                  <i className="bi bi-wallet2 me-1"></i> {t('employee.myPayroll')}
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/employee/profile">
                  <i className="bi bi-person me-1"></i> {t('employee.myProfile')}
                </Link>
              </li>
              <li className="nav-item">
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
              <li className="nav-item">
                <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>
                  <i className="bi bi-box-arrow-left me-1"></i> {t('auth.logout')}
                </button>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <main className="container-fluid py-4 flex-grow-1">
        <Routes>
          <Route path="/" element={
            <div>
              <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4">
                <div className="d-flex align-items-center">
                  {user?.avatarUrl ? (
                    <img src={user.avatarUrl} alt="Avatar" className="rounded-circle me-3" style={{ width: 50, height: 50, objectFit: 'cover' }} />
                  ) : (
                    <div className="rounded-circle bg-primary me-3 d-flex align-items-center justify-content-center" style={{ width: 50, height: 50 }}>
                      <i className="bi bi-person text-white fs-3"></i>
                    </div>
                  )}
                  <div>
                    <h1 className="mb-1">
                      <i className="bi bi-emoji-smile me-2 text-primary"></i>
                      {t('admin.welcome')} {user?.fullName}!
                    </h1>
                    <p className="text-muted mb-0">{t('employee.dashboardSubtitle')}</p>
                  </div>
                </div>
                <div className="mt-3 mt-md-0 text-muted">
                  <i className="bi bi-calendar me-1"></i>
                  {new Date().toLocaleDateString(i18n.language === 'vi' ? 'vi-VN' : 'en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
                </div>
              </div>

              <div className="card shadow-sm border-0 mb-4">
                <div className="card-body">
                  <div className="d-flex flex-column flex-md-row justify-content-between align-items-start gap-3">
                    <div>
                      <h5 className="mb-2">{t('employee.attendanceFilterTitle')}</h5>
                      <p className="text-muted mb-3">{t('employee.attendanceFilterSubtitle')}</p>
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
                </div>
              </div>

              {/* Quick Check-in/Check-out Card */}
      <div className="card shadow-sm border-0 mb-5">
        <div className="card-body p-4">
          <div className="d-flex flex-column flex-md-row align-items-center justify-content-between">
            <div>
              <h4 className="mb-1">{t('employee.checkInOut')}</h4>
              {todayAttendance ? (
                <div className="text-muted">
                  {todayAttendance.checkIn && <span className="me-3"><i className="bi bi-box-arrow-in-right me-1 text-success"></i> {t('attendance.checkIn')}: {todayAttendance.checkIn.toString()}</span>}
                  {todayAttendance.checkOut && <span><i className="bi bi-box-arrow-right me-1 text-danger"></i> {t('attendance.checkOut')}: {todayAttendance.checkOut.toString()}</span>}
                </div>
              ) : (
                <div className="text-muted">{t('employee.notCheckedInYet')}</div>
              )}
            </div>
            <div className="mt-3 mt-md-0 d-flex gap-2">
              {!todayAttendance?.checkIn ? (
                <button
                  className="btn btn-success btn-lg"
                  onClick={handleCheckInOut}
                  disabled={checkingInOut}
                >
                  {checkingInOut ? (
                    <span className="spinner-border spinner-border-sm me-2"></span>
                  ) : (
                    <i className="bi bi-box-arrow-in-right me-2"></i>
                  )}
                  Check In
                </button>
              ) : !todayAttendance?.checkOut ? (
                <button
                  className="btn btn-danger btn-lg"
                  onClick={handleCheckInOut}
                  disabled={checkingInOut}
                >
                  {checkingInOut ? (
                    <span className="spinner-border spinner-border-sm me-2"></span>
                  ) : (
                    <i className="bi bi-box-arrow-right me-2"></i>
                  )}
                  Check Out
                </button>
              ) : (
                <button className="btn btn-secondary btn-lg" disabled>
                  <i className="bi bi-check-lg me-2"></i>
                  {t('common.success')}
                </button>
              )}
            </div>
          </div>
        </div>
      </div>

              {/* Stats Cards */}
              <div className="row g-4 mb-5">
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <h5 className="text-muted mb-0">{t('employee.remainingLeave')}</h5>
                        <i className="bi bi-calendar-x text-primary fs-3"></i>
                      </div>
                      <h2 className="text-primary fw-bold">{stats.leaveBalance} <small className="fs-6 text-muted">{t('employee.days')}</small></h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <h5 className="text-muted mb-0">{t('employee.totalHours')}</h5>
                        <i className="bi bi-clock-history text-success fs-3"></i>
                      </div>
                      <h2 className="text-success fw-bold">{stats.totalHours.toFixed(2)} <small className="fs-6 text-muted">{t('employee.hours')}</small></h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <h5 className="text-muted mb-0">{t('employee.pendingRequests')}</h5>
                        <i className="bi bi-exclamation-circle text-warning fs-3"></i>
                      </div>
                      <h2 className="text-warning fw-bold">{stats.pendingRequests} <small className="fs-6 text-muted">{t('common.actions')}</small></h2>
                    </div>
                  </div>
                </div>
                <div className="col-md-3">
                  <div className="card shadow-sm border-0 h-100">
                    <div className="card-body">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <h5 className="text-muted mb-0">{t('admin.currentMonth')}</h5>
                        <i className="bi bi-calendar3 text-info fs-3"></i>
                      </div>
                      <h2 className="text-info fw-bold">{new Date().getMonth() + 1}/{new Date().getFullYear()}</h2>
                    </div>
                  </div>
                </div>
              </div>

              {/* Quick Actions */}
              <div className="row g-4">
                <div className="col-md-4">
                  <Link to="/employee/attendance" className="text-decoration-none">
                    <div className="card shadow-sm h-100 hover-shadow">
                      <div className="card-body text-center p-5">
                        <i className="bi bi-calendar-check fs-1 text-primary mb-3"></i>
                        <h4 className="card-title mb-0">{t('employee.myAttendance')}</h4>
                      </div>
                    </div>
                  </Link>
                </div>
                <div className="col-md-4">
                  <Link to="/employee/requests" className="text-decoration-none">
                    <div className="card shadow-sm h-100 hover-shadow">
                      <div className="card-body text-center p-5">
                        <i className="bi bi-file-earmark-text fs-1 text-warning mb-3"></i>
                        <h4 className="card-title mb-0">{t('employee.myRequests')}</h4>
                      </div>
                    </div>
                  </Link>
                </div>
                <div className="col-md-4">
                  <Link to="/employee/payslips" className="text-decoration-none">
                    <div className="card shadow-sm h-100 hover-shadow">
                      <div className="card-body text-center p-5">
                        <i className="bi bi-wallet2 fs-1 text-success mb-3"></i>
                        <h4 className="card-title mb-0">{t('employee.myPayroll')}</h4>
                      </div>
                    </div>
                  </Link>
                </div>
              </div>
            </div>
          } />
          <Route path="/attendance" element={<EmployeeAttendance />} />
          <Route path="/requests" element={<EmployeeRequests />} />
          <Route path="/payslips" element={<EmployeePayslips />} />
          <Route path="/profile" element={<EmployeeProfile user={user} onProfileUpdate={onProfileUpdate} />} />
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
