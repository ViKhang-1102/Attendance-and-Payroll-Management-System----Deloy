import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getWorkShifts, createWorkShift, updateWorkShift, deleteWorkShift } from '../../services/auth';

export default function AdminWorkShifts() {
  const { t } = useTranslation();
  const [workShifts, setWorkShifts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingShift, setEditingShift] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    checkInTime: '',
    checkOutTime: '',
    graceMinutes: 0,
    overtimeHourlyRate: 0
  });

  useEffect(() => {
    fetchWorkShifts();
  }, []);

  const fetchWorkShifts = async () => {
    try {
      setLoading(true);
      const data = await getWorkShifts();
      setWorkShifts(data);
    } catch (err) {
      console.error('Failed to fetch work shifts:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingShift) {
        await updateWorkShift(editingShift.id, formData);
      } else {
        await createWorkShift(formData);
      }
      setShowModal(false);
      resetForm();
      fetchWorkShifts();
    } catch (err) {
      console.error('Failed to save work shift:', err);
    }
  };

  const handleEdit = (shift) => {
    setEditingShift(shift);
    setFormData({
      name: shift.name,
      checkInTime: shift.checkInTime,
      checkOutTime: shift.checkOutTime,
      graceMinutes: shift.graceMinutes,
      overtimeHourlyRate: shift.overtimeHourlyRate || 0
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm(t('adminWorkShifts.deleteConfirm')) ) {
      try {
        await deleteWorkShift(id);
        fetchWorkShifts();
      } catch (err) {
        console.error('Failed to delete work shift:', err);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      checkInTime: '',
      checkOutTime: '',
      graceMinutes: 0,
      overtimeHourlyRate: 0
    });
    setEditingShift(null);
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>{t('adminWorkShifts.title')}</h2>
        <button 
          className="btn btn-primary"
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
        >
          {t('adminWorkShifts.addShift')}
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
                    <th>{t('adminWorkShifts.name')}</th>
                    <th>{t('adminWorkShifts.checkInTime')}</th>
                    <th>{t('adminWorkShifts.checkOutTime')}</th>
                    <th>{t('adminWorkShifts.graceMinutes')}</th>
                    <th>{t('adminWorkShifts.overtimeHourlyRate')}</th>
                    <th>{t('adminWorkShifts.actions')}</th>
                  </tr>
                </thead>
                <tbody>
                  {workShifts.map((shift) => (
                    <tr key={shift.id}>
                      <td>{shift.name}</td>
                      <td>{shift.checkInTime}</td>
                      <td>{shift.checkOutTime}</td>
                      <td>{shift.graceMinutes}</td>
                      <td>{new Intl.NumberFormat('vi-VN').format(shift.overtimeHourlyRate || 0)}</td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary me-2"
                          onClick={() => handleEdit(shift)}
                        >
                          {t('common.edit')}
                        </button>
                        <button
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => handleDelete(shift.id)}
                        >
                          {t('common.delete')}
                        </button>
                      </td>
                    </tr>
                  ))}
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
                <h5 className="modal-title">
                  {editingShift ? t('adminWorkShifts.editShift') : t('adminWorkShifts.addShift')}
                </h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>
              <div className="modal-body">
                <form id="workshift-form" onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label className="form-label">{t('adminWorkShifts.name')}</label>
                    <input
                      type="text"
                      className="form-control"
                      value={formData.name}
                      onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                      required
                    />
                  </div>
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('adminWorkShifts.checkInTime')}</label>
                      <input
                        type="time"
                        className="form-control"
                        value={formData.checkInTime}
                        onChange={(e) => setFormData({ ...formData, checkInTime: e.target.value })}
                        required
                      />
                    </div>
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('adminWorkShifts.checkOutTime')}</label>
                      <input
                        type="time"
                        className="form-control"
                        value={formData.checkOutTime}
                        onChange={(e) => setFormData({ ...formData, checkOutTime: e.target.value })}
                        required
                      />
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('adminWorkShifts.graceMinutes')}</label>
                      <input
                        type="number"
                        className="form-control"
                        value={formData.graceMinutes}
                        onChange={(e) => setFormData({ ...formData, graceMinutes: Number(e.target.value) })}
                        required
                      />
                    </div>
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('adminWorkShifts.overtimeHourlyRate')}</label>
                      <input
                        type="number"
                        className="form-control"
                        value={formData.overtimeHourlyRate}
                        onChange={(e) => setFormData({ ...formData, overtimeHourlyRate: Number(e.target.value) })}
                        required
                      />
                    </div>
                  </div>
                </form>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  {t('adminWorkShifts.cancel')}
                </button>
                <button type="submit" className="btn btn-primary" form="workshift-form">
                  {editingShift ? t('adminWorkShifts.update') : t('adminWorkShifts.save')}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}