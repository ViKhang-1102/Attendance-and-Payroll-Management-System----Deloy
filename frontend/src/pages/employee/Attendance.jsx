
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getMyAttendances } from '../../services/auth';

export default function EmployeeAttendance() {
  const { t } = useTranslation();
  const [attendances, setAttendances] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterMonth, setFilterMonth] = useState(new Date().toISOString().slice(0, 7));

  useEffect(() => {
    fetchAttendances();
  }, []);

  const fetchAttendances = async () => {
    try {
      setLoading(true);
      const data = await getMyAttendances();
      setAttendances(data);
    } catch (err) {
      console.error('Failed to fetch attendances:', err);
    } finally {
      setLoading(false);
    }
  };

  const filteredAttendances = attendances.filter(att => att.date.toString().startsWith(filterMonth));

  const getStatusBadge = (status) => {
    switch (status) {
      case 'PRESENT':
        return <span className="badge bg-success"><i className="bi bi-check-circle me-1"></i> {t('attendanceStatus.present')}</span>;
      case 'LATE':
        return <span className="badge bg-warning text-dark"><i className="bi bi-exclamation-circle me-1"></i> {t('attendanceStatus.late')}</span>;
      case 'ABSENT':
        return <span className="badge bg-danger"><i className="bi bi-x-circle me-1"></i> {t('attendanceStatus.absent')}</span>;
      case 'ON_LEAVE':
        return <span className="badge bg-info"><i className="bi bi-calendar-x me-1"></i> {t('attendanceStatus.onLeave')}</span>;
      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  const totalHours = filteredAttendances.reduce((sum, a) => sum + (a.hoursWorked || 0), 0);

  return (
    <div>
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4">
        <h2><i className="bi bi-calendar-check me-2 text-primary"></i> {t('employeeAttendance.title')}</h2>
        <div className="mt-3 mt-md-0">
          <label className="form-label me-2 fw-bold">{t('employeeAttendance.filterMonth')}:</label>
          <input
            type="month"
            className="form-control d-inline-block"
            style={{ width: '100%', maxWidth: '200px' }}
            value={filterMonth}
            onChange={(e) => setFilterMonth(e.target.value)}
          />
        </div>
      </div>

      {loading ? (
        <div className="d-flex justify-content-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        <div>
          <div className="card shadow-sm border-0 mb-4">
            <div className="card-body">
              <div className="d-flex align-items-center gap-3">
                <i className="bi bi-clock-history fs-3 text-success"></i>
                <div>
                  <h5 className="mb-0">{t('employeeAttendance.totalMonthlyHours')}</h5>
                  <h3 className="text-success fw-bold mt-1">{totalHours.toFixed(2)} {t('employeeAttendance.hoursLabel')}</h3>
                </div>
              </div>
            </div>
          </div>

          <div className="card shadow-sm border-0">
            <div className="card-body p-0">
              <div className="table-responsive">
                <table className="table table-hover mb-0">
                  <thead className="table-light">
                    <tr>
                      <th><i className="bi bi-calendar me-1"></i> {t('employeeAttendance.date')}</th>
                      <th><i className="bi bi-box-arrow-in-right me-1"></i> {t('employeeAttendance.checkIn')}</th>
                      <th><i className="bi bi-box-arrow-right me-1"></i> {t('employeeAttendance.checkOut')}</th>
                      <th><i className="bi bi-clock me-1"></i> {t('employeeAttendance.totalHours')}</th>
                      <th><i className="bi bi-info-circle me-1"></i> {t('employeeAttendance.status')}</th>
                      <th><i className="bi bi-chat-left-text me-1"></i> {t('employeeAttendance.notes')}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredAttendances.length > 0 ? filteredAttendances.map((att) => (
                      <tr key={att.id}>
                        <td>{new Date(att.date).toLocaleDateString('vi-VN')}</td>
                        <td>{att.checkIn?.toString() || '-'}</td>
                        <td>{att.checkOut?.toString() || '-'}</td>
                        <td><strong>{att.hoursWorked?.toFixed(2) || 0} {t('employeeAttendance.hoursLabel')}</strong></td>
                        <td>{getStatusBadge(att.status)}</td>
                        <td className="text-wrap-column">{att.notes || '-'}</td>
                      </tr>
                    )) : (
                      <tr>
                        <td colSpan={6} className="text-center py-5 text-muted">
                          <i className="bi bi-inbox fs-3"></i>
                          <div className="mt-2">{t('employeeAttendance.noRecords')}</div>
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
