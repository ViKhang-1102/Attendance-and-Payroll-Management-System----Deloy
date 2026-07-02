
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getLeaveRequests, approveLeaveRequest } from '../../services/auth';

export default function AdminLeaveRequests() {
  const { t } = useTranslation();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    try {
      setLoading(true);
      const data = await getLeaveRequests();
      setRequests(data);
    } catch (err) {
      console.error('Failed to fetch leave requests:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (id, approve) => {
    let rejectionReason = null;
    if (!approve) {
      rejectionReason = prompt(t('adminLeaveRequests.promptRejectionReason'));
      if (rejectionReason === null) return;
    }
    
    try {
      await approveLeaveRequest(id, approve, rejectionReason);
      fetchRequests();
    } catch (err) {
      console.error('Failed to process request:', err);
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'PENDING':
        return <span className="badge bg-warning">{t('adminLeaveRequests.status.pending')}</span>;
      case 'APPROVED':
        return <span className="badge bg-success">{t('adminLeaveRequests.status.approved')}</span>;
      case 'REJECTED':
        return <span className="badge bg-danger">{t('adminLeaveRequests.status.rejected')}</span>;
      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  const getLeaveTypeText = (type) => {
    switch (type) {
      case 'ANNUAL':
        return t('employeeRequests.leaveType.annual');
      case 'SICK':
        return t('employeeRequests.leaveType.sick');
      case 'PERSONAL':
        return t('employeeRequests.leaveType.personal');
      case 'MATERNITY':
        return t('employeeRequests.leaveType.maternity');
      default:
        return type;
    }
  };

  return (
    <div>
      <h2 className="mb-4">{t('adminLeaveRequests.title')}</h2>

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
                    <th>{t('adminLeaveRequests.table.employee')}</th>
                    <th>{t('adminLeaveRequests.table.leaveType')}</th>
                    <th>{t('adminLeaveRequests.table.startDate')}</th>
                    <th>{t('adminLeaveRequests.table.endDate')}</th>
                    <th>{t('adminLeaveRequests.table.reason')}</th>
                    <th>{t('adminLeaveRequests.table.status')}</th>
                    <th>{t('common.actions')}</th>
                  </tr>
                </thead>
                <tbody>
                  {requests.map((req) => (
                    <tr key={req.id}>
                      <td>{req.userName}</td>
                      <td>{getLeaveTypeText(req.leaveType)}</td>
                      <td>{req.startDate}</td>
                      <td>{req.endDate}</td>
                      <td className="text-wrap-column">{req.reason || '-'}</td>
                      <td>{getStatusBadge(req.status)}</td>
                      <td>
                        {req.status === 'PENDING' && (
                          <>
                            <button
                              className="btn btn-sm btn-success me-2"
                              onClick={() => handleApprove(req.id, true)}
                            >
                              {t('adminLeaveRequests.actions.approve')}
                            </button>
                            <button
                              className="btn btn-sm btn-danger"
                              onClick={() => handleApprove(req.id, false)}
                            >
                              {t('adminLeaveRequests.actions.reject')}
                            </button>
                          </>
                        )}
                      </td>
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
