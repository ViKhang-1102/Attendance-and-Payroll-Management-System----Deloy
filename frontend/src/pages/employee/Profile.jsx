import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { updateProfile } from '../../services/auth';

export default function EmployeeProfile({ user, onProfileUpdate }) {
  const { t } = useTranslation();
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    phone: '',
    address: '',
    dateOfBirth: ''
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user) {
      setFormData({
        phone: user.phone || '',
        address: user.address || '',
        dateOfBirth: user.dateOfBirth || ''
      });
    }
  }, [user]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      await updateProfile(formData);
      if (onProfileUpdate) {
        await onProfileUpdate();
      }
      setShowModal(false);
      alert(t('employeeProfile.editProfileSuccess'));
    } catch (err) {
      console.error('Failed to update profile:', err);
      setError(err.response?.data?.message || t('employeeProfile.editProfileError'));
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
        <h2 className="mb-0"><i className="bi bi-person me-2 text-primary"></i> {t('employeeProfile.title')}</h2>
        <button className="btn btn-primary shadow-sm px-3 d-flex align-items-center gap-2" onClick={() => setShowModal(true)}>
          <i className="bi bi-pencil-square"></i>
          <span>{t('employeeProfile.editProfile')}</span>
        </button>
      </div>

      <div className="card shadow-sm border-0">
        <div className="card-body p-4">
          <div className="row">
            <div className="col-md-6 mb-4">
              <div className="h-100">
                <h5 className="text-muted mb-3 border-bottom pb-2"><i className="bi bi-person-badge me-2"></i> {t('employeeProfile.basicInfo')}</h5>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.fullName')}</div>
                  <div className="fw-bold fs-5">{user?.fullName || '-'}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.email')}</div>
                  <div className="fw-bold">{user?.email || '-'}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.staffId')}</div>
                  <div className="fw-bold">{user?.staffId || '-'}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.phone')}</div>
                  <div className="fw-bold">{user?.phone || '-'}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.address')}</div>
                  <div className="fw-bold">{user?.address || '-'}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.dateOfBirth')}</div>
                  <div className="fw-bold">{user?.dateOfBirth || '-'}</div>
                </div>
              </div>
            </div>
            <div className="col-md-6 mb-4">
              <div className="h-100">
                <h5 className="text-muted mb-3 border-bottom pb-2"><i className="bi bi-briefcase me-2"></i> {t('employeeProfile.workInfo')}</h5>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.role')}</div>
                  <div className="fw-bold">
                    <span className={`badge ${user?.role === 'ADMIN' ? 'bg-danger' : 'bg-primary'}`}>{user?.role || '-'}</span>
                  </div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.position')}</div>
                  <div className="fw-bold">{user?.positionName || '-'}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.department')}</div>
                  <div className="fw-bold">{user?.departmentName || '-'}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.leaveBalance')}</div>
                  <div className="fw-bold text-primary fs-5">{user?.leaveBalance || 0} {t('employeeProfile.days')}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.hourlyRate')}</div>
                  <div className="fw-bold">{user?.hourlyRate || 0} {t('employeeProfile.perHour')}</div>
                </div>
                <div className="mb-3">
                  <div className="text-muted">{t('employeeProfile.baseSalary')}</div>
                  <div className="fw-bold">{user?.baseSalary || 0} {t('employeeProfile.perMonth')}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {showModal && (
        <div className="modal show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content border-0 shadow-lg">
              <div className="modal-header bg-gradient text-white" style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
                <h5 className="modal-title fw-bold"><i className="bi bi-person-fill-gear me-2"></i>{t('employeeProfile.modal.title')}</h5>
                <button type="button" className="btn-close btn-close-white" onClick={() => setShowModal(false)} aria-label="Close"></button>
              </div>
              <form onSubmit={handleSubmit}>
                <div className="modal-body p-4">
                  {error && <div className="alert alert-danger">{error}</div>}
                  <div className="mb-3">
                    <label htmlFor="phone" className="form-label fw-semibold text-secondary">{t('employeeProfile.modal.phone')}</label>
                    <input
                      type="text"
                      className="form-control"
                      id="phone"
                      name="phone"
                      value={formData.phone}
                      onChange={handleChange}
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="address" className="form-label fw-semibold text-secondary">{t('employeeProfile.modal.address')}</label>
                    <textarea
                      className="form-control"
                      id="address"
                      name="address"
                      rows="3"
                      value={formData.address}
                      onChange={handleChange}
                    ></textarea>
                  </div>
                  <div className="mb-3">
                    <label htmlFor="dateOfBirth" className="form-label fw-semibold text-secondary">{t('employeeProfile.modal.dateOfBirth')}</label>
                    <input
                      type="date"
                      className="form-control"
                      id="dateOfBirth"
                      name="dateOfBirth"
                      value={formData.dateOfBirth}
                      onChange={handleChange}
                    />
                  </div>
                </div>
                <div className="modal-footer border-top-0 px-4 pb-4">
                  <button type="button" className="btn btn-outline-secondary px-3" onClick={() => setShowModal(false)} disabled={saving}>
                    {t('employeeProfile.modal.cancel')}
                  </button>
                  <button type="submit" className="btn btn-primary px-4 border-0" style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }} disabled={saving}>
                    {saving ? <span className="spinner-border spinner-border-sm me-2"></span> : null}
                    {t('employeeProfile.modal.save')}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
