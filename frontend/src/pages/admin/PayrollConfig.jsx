import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getPayrollConfigs, createPayrollConfig, updatePayrollConfig, setPayrollConfigActive, getActivePayrollConfig } from '../../services/auth';

export default function AdminPayrollConfig() {
  const { t } = useTranslation();
  const [configs, setConfigs] = useState([]);
  const [activeConfig, setActiveConfig] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingConfig, setEditingConfig] = useState(null);
  const [formData, setFormData] = useState({
    overtimeMultiplier: 1.5,
    insuranceRate: 10.5,
    taxRate: 20.0
  });

  useEffect(() => {
    fetchAllData();
  }, []);

  const fetchAllData = async () => {
    try {
      setLoading(true);
      const [data, active] = await Promise.all([
        getPayrollConfigs(),
        getActivePayrollConfig()
      ]);
      setConfigs(data);
      setActiveConfig(active);
    } catch (err) {
      console.error('Failed to fetch payroll configs:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingConfig) {
        await updatePayrollConfig(editingConfig.id, formData);
      } else {
        await createPayrollConfig(formData);
      }
      setShowModal(false);
      resetForm();
      fetchAllData();
    } catch (err) {
      console.error('Failed to save config:', err);
    }
  };

  const handleEdit = (config) => {
    setEditingConfig(config);
    setFormData({
      overtimeMultiplier: config.overtimeMultiplier,
      insuranceRate: config.insuranceRate,
      taxRate: config.taxRate
    });
    setShowModal(true);
  };

  const handleSetActive = async (id) => {
    try {
      await setPayrollConfigActive(id);
      fetchAllData();
    } catch (err) {
      console.error('Failed to set active config:', err);
    }
  };

  const resetForm = () => {
    setFormData({
      overtimeMultiplier: 1.5,
      insuranceRate: 10.5,
      taxRate: 20.0
    });
    setEditingConfig(null);
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>{t('adminPayrollConfig.title')}</h2>
        <button 
          className="btn btn-primary"
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
        >
          {t('adminPayrollConfig.addConfig')}
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
                    <th>{t('adminPayrollConfig.table.overtimeMultiplier')}</th>
                    <th>{t('adminPayrollConfig.table.insuranceRate')}</th>
                    <th>{t('adminPayrollConfig.table.taxRate')}</th>
                    <th>{t('adminPayrollConfig.table.status')}</th>
                    <th>{t('common.actions')}</th>
                  </tr>
                </thead>
                <tbody>
                  {configs.map((config) => (
                    <tr key={config.id}>
                      <td>{config.overtimeMultiplier}x</td>
                      <td>{config.insuranceRate}%</td>
                      <td>{config.taxRate}%</td>
                      <td>
                        <span className={`badge ${config.isActive ? 'bg-success' : 'bg-secondary'}`}>
                          {config.isActive ? t('adminPayrollConfig.status.active') : t('adminPayrollConfig.status.inactive')}
                        </span>
                      </td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary me-2"
                          onClick={() => handleEdit(config)}
                        >
                          {t('common.edit')}
                        </button>
                        {!config.isActive && (
                          <button
                            className="btn btn-sm btn-outline-success"
                            onClick={() => handleSetActive(config.id)}
                          >
                            {t('adminPayrollConfig.actions.setActive')}
                          </button>
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

      {showModal && (
        <div className="modal fade show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  {editingConfig ? t('adminPayrollConfig.modal.editTitle') : t('adminPayrollConfig.modal.addTitle')}
                </h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>
              <div className="modal-body">
                <form id="config-form" onSubmit={handleSubmit}>
                  <div className="mb-3">
                    <label className="form-label">{t('adminPayrollConfig.modal.overtimeMultiplier')}</label>
                    <input
                      type="number"
                      step="0.1"
                      className="form-control"
                      value={formData.overtimeMultiplier}
                      onChange={(e) => setFormData({ ...formData, overtimeMultiplier: Number(e.target.value) })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">{t('adminPayrollConfig.modal.insuranceRate')}</label>
                    <input
                      type="number"
                      step="0.1"
                      className="form-control"
                      value={formData.insuranceRate}
                      onChange={(e) => setFormData({ ...formData, insuranceRate: Number(e.target.value) })}
                      required
                    />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">{t('adminPayrollConfig.modal.taxRate')}</label>
                    <input
                      type="number"
                      step="0.1"
                      className="form-control"
                      value={formData.taxRate}
                      onChange={(e) => setFormData({ ...formData, taxRate: Number(e.target.value) })}
                      required
                    />
                  </div>
                </form>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  {t('common.cancel')}
                </button>
                <button type="submit" className="btn btn-primary" form="config-form">
                  {editingConfig ? t('adminPayrollConfig.modal.update') : t('adminPayrollConfig.modal.add')}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}