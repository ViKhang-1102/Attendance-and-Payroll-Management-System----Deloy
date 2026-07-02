
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getAttendances, getUsers, getAttendancesByDateRange } from '../../services/auth';

export default function AdminAttendance() {
  const { t } = useTranslation();
  const [attendances, setAttendances] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [startDate, setStartDate] = useState(() => {
    const date = new Date();
    date.setDate(date.getDate() - 30);
    return date.toISOString().split('T')[0];
  });
  const [endDate, setEndDate] = useState(new Date().toISOString().split('T')[0]);

  useEffect(() => {
    fetchAllData();
  }, []);

  useEffect(() => {
    fetchAttendances();
  }, [startDate, endDate]);

  const fetchAllData = async () => {
    try {
      setLoading(true);
      const [attData, userData] = await Promise.all([
        getAttendancesByDateRange(startDate, endDate),
        getUsers()
      ]);
      setAttendances(attData);
      setUsers(userData);
    } catch (err) {
      console.error('Failed to fetch data:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchAttendances = async () => {
    try {
      setLoading(true);
      const attData = await getAttendancesByDateRange(startDate, endDate);
      setAttendances(attData);
    } catch (err) {
      console.error('Failed to fetch attendances:', err);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'PRESENT':
        return <span className="badge bg-success">{t('attendanceStatus.present')}</span>;
      case 'LATE':
        return <span className="badge bg-warning text-dark">{t('attendanceStatus.late')}</span>;
      case 'ABSENT':
        return <span className="badge bg-danger">{t('attendanceStatus.absent')}</span>;
      case 'ON_LEAVE':
        return <span className="badge bg-info">{t('attendanceStatus.onLeave')}</span>;
      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  const getUserAvatar = (userId) => {
    const user = users.find(u => u.id === userId);
    return user?.avatarUrl || null;
  };

  return (
    <div>
      <h2 className="mb-4">{t('adminAttendance.title')}</h2>

      <div className="card shadow-sm border-0 mb-4">
        <div className="card-body">
          <div className="d-flex flex-wrap gap-3 align-items-center">
            <div className="form-group">
              <label className="form-label small text-secondary mb-1">Start Date</label>
              <input
                type="date"
                className="form-control form-control-sm"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label className="form-label small text-secondary mb-1">End Date</label>
              <input
                type="date"
                className="form-control form-control-sm"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
            </div>
          </div>
        </div>
      </div>

      {loading ? (
        <div className="d-flex justify-content-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        <div className="card shadow-sm border-0">
          <div className="card-body p-0">
            <div className="table-responsive">
              <table className="table table-hover mb-0">
                <thead className="table-light">
                  <tr>
                    <th>{t('adminAttendance.employee')}</th>
                    <th>{t('adminAttendance.date')}</th>
                    <th>{t('adminAttendance.checkIn')}</th>
                    <th>{t('adminAttendance.checkOut')}</th>
                    <th>{t('adminAttendance.totalHours')}</th>
                    <th>{t('adminAttendance.overtimeHours')}</th>
                    <th>{t('adminAttendance.status')}</th>
                    <th>{t('adminAttendance.notes')}</th>
                  </tr>
                </thead>
                <tbody>
                  {attendances.map((att) => (
                    <tr key={att.id}>
                      <td>
                        <div className="d-flex align-items-center">
                          {getUserAvatar(att.userId) ? (
                            <img src={getUserAvatar(att.userId)} alt="Avatar" className="rounded-circle me-2" style={{ width: 32, height: 32, objectFit: 'cover' }} />
                          ) : (
                            <div className="rounded-circle bg-secondary me-2 d-flex align-items-center justify-content-center" style={{ width: 32, height: 32 }}>
                              <i className="bi bi-person text-white"></i>
                            </div>
                          )}
                          {att.userName}
                        </div>
                      </td>
                      <td>{new Date(att.date).toLocaleDateString('vi-VN')}</td>
                      <td>{att.checkIn?.toString() || '-'}</td>
                      <td>{att.checkOut?.toString() || '-'}</td>
                      <td>{att.hoursWorked?.toFixed(2) || 0} {t('employeeAttendance.hoursLabel')}</td>
                      <td>{att.overtimeHours?.toFixed(2) || 0} {t('employeeAttendance.hoursLabel')}</td>
                      <td>{getStatusBadge(att.status)}</td>
                      <td className="text-wrap-column">{att.notes || '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
