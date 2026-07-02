
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getMyLeaveRequests, createLeaveRequest } from '../../services/auth';

export default function EmployeeRequests() {
  const { t } = useTranslation();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    startDate: '',
    endDate: '',
    leaveType: 'ANNUAL',
    reason: ''
  });

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    try {
      setLoading(true);
      const data = await getMyLeaveRequests();
      setRequests(data);
    } catch (err) {
      console.error('Failed to fetch requests:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createLeaveRequest(formData);
      setShowModal(false);
      resetForm();
      fetchRequests();
      alert(t('employeeRequests.successCreate'));
    } catch (err) {
      console.error('Failed to create request:', err);
      alert(t('employeeRequests.errorCreate'));
    }
  };

  const resetForm = () => {
    setFormData({
      startDate: '',
      endDate: '',
      leaveType: 'ANNUAL',
      reason: ''
    });
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'PENDING':
        return <span className="badge bg-warning text-dark"><i className="bi bi-clock me-1"></i> {t('employeeRequests.status.pending')}</span>;
      case 'APPROVED':
        return <span className="badge bg-success"><i className="bi bi-check-circle me-1"></i> {t('employeeRequests.status.approved')}</span>;
      case 'REJECTED':
        return <span className="badge bg-danger"><i className="bi bi-x-circle me-1"></i> {t('employeeRequests.status.rejected')}</span>;
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
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center mb-4">
        <h2><i className="bi bi-file-earmark-text me-2 text-warning"></i> {t('employeeRequests.title')}</h2>
        <button
          className="btn btn-primary mt-3 mt-md-0"
          onClick={() => setShowModal(true)}
        >
          <i className="bi bi-plus-lg me-2"></i>
          {t('employeeRequests.newRequest')}
        </button>
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
                    <th><i className="bi bi-clipboard-check me-1"></i> {t('employeeRequests.columns.leaveType')}</th>
                    <th><i className="bi bi-calendar-check me-1"></i> {t('employeeRequests.columns.startDate')}</th>
                    <th><i className="bi bi-calendar-x me-1"></i> {t('employeeRequests.columns.endDate')}</th>
                    <th><i className="bi bi-chat-left-text me-1"></i> {t('employeeRequests.columns.reason')}</th>
                    <th><i className="bi bi-info-circle me-1"></i> {t('employeeRequests.columns.status')}</th>
                    <th><i className="bi bi-chat-dots me-1"></i> {t('employeeRequests.columns.rejectionReason')}</th>
                  </tr>
                </thead>
                <tbody>
                  {requests.length > 0 ? requests.map((req) => (
                    <tr key={req.id}>
                      <td>{getLeaveTypeText(req.leaveType)}</td>
                      <td>{new Date(req.startDate).toLocaleDateString()}</td>
                      <td>{new Date(req.endDate).toLocaleDateString()}</td>
                      <td className="text-wrap-column">{req.reason || '-'}</td>
                      <td>{getStatusBadge(req.status)}</td>
                      <td className="text-wrap-column">{req.rejectionReason || '-'}</td>
                    </tr>
                  )) : (
                    <tr>
                      <td colSpan={6} className="text-center py-5 text-muted">
                        <i className="bi bi-inbox fs-3"></i>
                        <div className="mt-2">{t('employeeRequests.noRequests')}</div>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {showModal && (
        <div className="modal fade show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title"><i className="bi bi-file-earmark-plus me-2 text-primary"></i> {t('employeeRequests.modal.title')}</h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>
              <div className="modal-body">
                <form id="leave-request-form" onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label className="form-label fw-bold"><i className="bi bi-calendar-check me-1"></i> {t('employeeRequests.modal.startDate')}</label>
                    <input
                      type="date"
                      className="form-control"
                      value={formData.startDate}
                      onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label fw-bold"><i className="bi bi-calendar-x me-1"></i> {t('employeeRequests.modal.endDate')}</label>
                    <input
                      type="date"
                      className="form-control"
                      value={formData.endDate}
                      onChange={(e) => setFormData({ ...formData, endDate: e.target.value })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label fw-bold"><i className="bi bi-clipboard-check me-1"></i> {t('employeeRequests.modal.leaveType')}</label>
                    <select
                      className="form-select"
                      value={formData.leaveType}
                      onChange={(e) => setFormData({ ...formData, leaveType: e.target.value })}
                    >
                      <option value="ANNUAL">{t('employeeRequests.leaveType.annual')}</option>
                      <option value="SICK">{t('employeeRequests.leaveType.sick')}</option>
                      <option value="PERSONAL">{t('employeeRequests.leaveType.personal')}</option>
                      <option value="MATERNITY">{t('employeeRequests.leaveType.maternity')}</option>
                    </select>
                  </div>
                  <div className="mb-3">
                    <label className="form-label fw-bold"><i className="bi bi-chat-left-text me-1"></i> {t('employeeRequests.modal.reason')}</label>
                    <textarea
                      className="form-control"
                      rows={3}
                      value={formData.reason}
                      onChange={(e) => setFormData({ ...formData, reason: e.target.value })}
                      placeholder={t('employeeRequests.modal.reasonPlaceholder')}
                    ></textarea>
                  </div>
                </form>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  <i className="bi bi-x-lg me-1"></i> {t('common.cancel')}
                </button>
                <button type="submit" className="btn btn-primary" form="leave-request-form">
                  <i className="bi bi-check-lg me-1"></i> {t('employeeRequests.modal.create')}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
