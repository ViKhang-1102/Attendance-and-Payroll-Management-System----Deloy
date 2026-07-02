
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getUsers, createUser, updateUser, deleteUser, getPositions, getDepartments, getWorkShifts, uploadFile } from '../../services/auth';

export default function AdminEmployees() {
  const { t } = useTranslation();
  const [employees, setEmployees] = useState([]);
  const [positions, setPositions] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [workShifts, setWorkShifts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    fullName: '',
    staffId: '',
    phone: '',
    address: '',
    role: 'EMPLOYEE',
    hourlyRate: 0,
    baseSalary: 0,
    leaveBalance: 12,
    isActive: true,
    positionId: null,
    departmentId: null,
    workShiftId: null,
    avatarUrl: '',
    cccdUrl: '',
    contractUrl: ''
  });
  const [uploading, setUploading] = useState(false);
  
  const handleFileUpload = async (file, fieldName) => {
    if (!file) return;
    setUploading(true);
    try {
      const response = await uploadFile(file);
      setFormData({ ...formData, [fieldName]: response.url });
    } catch (err) {
      console.error(`Failed to upload ${fieldName}:`, err);
      alert(t('adminEmployees.uploadFailed', { field: fieldName }));
    } finally {
      setUploading(false);
    }
  };

  useEffect(() => {
    fetchAllData();
  }, []);

  const fetchAllData = async () => {
    try {
      setLoading(true);
      const [users, pos, deps, shifts] = await Promise.all([
        getUsers(),
        getPositions(),
        getDepartments(),
        getWorkShifts()
      ]);
      setEmployees(users);
      setPositions(pos);
      setDepartments(deps);
      setWorkShifts(shifts);
    } catch (err) {
      console.error('Failed to fetch data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingEmployee) {
        await updateUser(editingEmployee.id, formData);
      } else {
        await createUser(formData);
      }
      setShowModal(false);
      resetForm();
      fetchAllData();
    } catch (err) {
      console.error('Failed to save employee:', err);
    }
  };

  const handleEdit = (emp) => {
    setEditingEmployee(emp);
    setFormData({
      email: emp.email,
      password: '',
      fullName: emp.fullName,
      staffId: emp.staffId,
      phone: emp.phone,
      address: emp.address,
      role: emp.role,
      hourlyRate: emp.hourlyRate,
      baseSalary: emp.baseSalary,
      leaveBalance: emp.leaveBalance,
      isActive: emp.isActive,
      positionId: emp.positionId,
      departmentId: emp.departmentId,
      workShiftId: emp.workShiftId,
      avatarUrl: emp.avatarUrl,
      cccdUrl: emp.cccdUrl,
      contractUrl: emp.contractUrl
    });
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm(t('adminEmployees.confirmDelete'))) {
      try {
        await deleteUser(id);
        fetchAllData();
      } catch (err) {
        console.error('Failed to delete employee:', err);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      email: '',
      password: '',
      fullName: '',
      staffId: '',
      phone: '',
      address: '',
      role: 'EMPLOYEE',
      hourlyRate: 0,
      baseSalary: 0,
      leaveBalance: 12,
      isActive: true,
      positionId: null,
      departmentId: null,
      workShiftId: null,
      avatarUrl: '',
      cccdUrl: '',
      contractUrl: ''
    });
    setEditingEmployee(null);
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>{t('adminEmployees.title')}</h2>
        <button 
          className="btn btn-primary"
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
        >
          {t('adminEmployees.addEmployee')}
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
                    <th>{t('employeeTable.staffId')}</th>
                    <th>{t('employeeTable.fullName')}</th>
                    <th>{t('employeeTable.email')}</th>
                    <th>{t('employeeTable.role')}</th>
                    <th>{t('employeeTable.position')}</th>
                    <th>{t('employeeTable.department')}</th>
                    <th>{t('employeeTable.workShift')}</th>
                    <th>{t('employeeTable.leaveBalance')}</th>
                    <th>{t('employeeTable.status')}</th>
                    <th>{t('common.actions')}</th>
                  </tr>
                </thead>
                <tbody>
                  {employees.map((emp) => (
                    <tr key={emp.id}>
                      <td>{emp.staffId}</td>
                      <td>
                        <div className="d-flex align-items-center">
                          {emp.avatarUrl ? (
                            <img src={emp.avatarUrl} alt="Avatar" className="rounded-circle me-2" style={{ width: 32, height: 32, objectFit: 'cover' }} />
                          ) : (
                            <div className="rounded-circle bg-secondary me-2 d-flex align-items-center justify-content-center" style={{ width: 32, height: 32 }}>
                              <i className="bi bi-person text-white"></i>
                            </div>
                          )}
                          {emp.fullName}
                        </div>
                      </td>
                      <td>{emp.email}</td>
                      <td>
                        <span className={`badge ${emp.role === 'ADMIN' ? 'bg-danger' : 'bg-primary'}`}>
                          {emp.role}
                        </span>
                      </td>
                      <td>{emp.positionName || '-'}</td>
                      <td>{emp.departmentName || '-'}</td>
                      <td>{emp.workShiftName || '-'}</td>
                      <td>{emp.leaveBalance} {t('employee.days')}</td>
                      <td>
                        <span className={`badge ${emp.isActive ? 'bg-success' : 'bg-secondary'}`}>
                          {emp.isActive ? t('adminEmployees.status.active') : t('adminEmployees.status.inactive')}
                        </span>
                      </td>
                      <td>
                        <div className="d-flex flex-wrap gap-2 employee-action-buttons">
                          <button
                            className="btn btn-sm btn-outline-primary employee-action-btn"
                            onClick={() => handleEdit(emp)}
                          >
                            {t('common.edit')}
                          </button>
                          <button
                            className="btn btn-sm btn-outline-danger employee-action-btn"
                            onClick={() => handleDelete(emp.id)}
                          >
                            {t('common.delete')}
                          </button>
                        </div>
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
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  {editingEmployee ? t('adminEmployees.modal.editTitle') : t('adminEmployees.modal.addTitle')}
                </h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>
              <div className="modal-body">
                <form id="employee-form" onSubmit={handleSubmit}>
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('employeeForm.email')}</label>
                      <input
                        type="email"
                        className="form-control"
                        value={formData.email}
                        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                        required
                      />
                    </div>
                    {!editingEmployee && (
                      <div className="col-md-6 mb-3">
                        <label className="form-label">{t('auth.password')}</label>
                        <input
                          type="password"
                          className="form-control"
                          value={formData.password}
                          onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                          required
                        />
                      </div>
                    )}
                  </div>
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('employeeForm.fullName')}</label>
                      <input
                        type="text"
                        className="form-control"
                        value={formData.fullName}
                        onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                        required
                      />
                    </div>
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('employeeForm.staffId')}</label>
                      <input
                        type="text"
                        className="form-control"
                        value={formData.staffId}
                        onChange={(e) => setFormData({ ...formData, staffId: e.target.value })}
                      />
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('employeeForm.phone')}</label>
                      <input
                        type="text"
                        className="form-control"
                        value={formData.phone}
                        onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                      />
                    </div>
                    <div className="col-md-6 mb-3">
                      <label className="form-label">{t('employeeForm.role')}</label>
                      <select
                        className="form-select"
                        value={formData.role}
                        onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                      >
                        <option value="EMPLOYEE">{t('roles.employee')}</option>
                        <option value="ADMIN">{t('roles.admin')}</option>
                      </select>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('employeeForm.hourlyRate')}</label>
                      <input
                        type="number"
                        className="form-control"
                        value={formData.hourlyRate}
                        onChange={(e) => setFormData({ ...formData, hourlyRate: Number(e.target.value) })}
                      />
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('employeeForm.baseSalary')}</label>
                      <input
                        type="number"
                        className="form-control"
                        value={formData.baseSalary}
                        onChange={(e) => setFormData({ ...formData, baseSalary: Number(e.target.value) })}
                      />
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('employeeForm.leaveBalance')}</label>
                      <input
                        type="number"
                        className="form-control"
                        value={formData.leaveBalance}
                        onChange={(e) => setFormData({ ...formData, leaveBalance: Number(e.target.value) })}
                      />
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('employeeForm.position')}</label>
                      <select
                        className="form-select"
                        value={formData.positionId}
                        onChange={(e) => setFormData({ ...formData, positionId: e.target.value || null })}
                      >
                        <option value="">{t('adminEmployees.selectPosition')}</option>
                        {positions.map((pos) => (
                          <option key={pos.id} value={pos.id}>
                            {pos.name}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('employeeForm.department')}</label>
                      <select
                        className="form-select"
                        value={formData.departmentId}
                        onChange={(e) => setFormData({ ...formData, departmentId: e.target.value || null })}
                      >
                        <option value="">{t('adminEmployees.selectDepartment')}</option>
                        {departments.map((dep) => (
                          <option key={dep.id} value={dep.id}>
                            {dep.name}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('employeeForm.workShift')}</label>
                      <select
                        className="form-select"
                        value={formData.workShiftId}
                        onChange={(e) => setFormData({ ...formData, workShiftId: e.target.value || null })}
                      >
                        <option value="">{t('adminEmployees.selectWorkShift')}</option>
                        {workShifts.map((shift) => (
                          <option key={shift.id} value={shift.id}>
                            {shift.name}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-md-4 mb-3">
                      <label className="form-label">Avatar (URL hoặc upload file)</label>
                      <input
                        type="url"
                        className="form-control mb-2"
                        value={formData.avatarUrl}
                        onChange={(e) => setFormData({ ...formData, avatarUrl: e.target.value })}
                      />
                      <input
                        type="file"
                        className="form-control"
                        accept="image/*"
                        disabled={uploading}
                        onChange={(e) => handleFileUpload(e.target.files[0], 'avatarUrl')}
                      />
                      {formData.avatarUrl && (
                        <div className="mt-2">
                          <img src={formData.avatarUrl} alt="Preview" className="rounded" style={{ width: 64, height: 64, objectFit: 'cover' }} />
                        </div>
                      )}
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('adminEmployees.cccdLabel')}</label>
                      <input
                        type="url"
                        className="form-control mb-2"
                        value={formData.cccdUrl}
                        onChange={(e) => setFormData({ ...formData, cccdUrl: e.target.value })}
                      />
                      <input
                        type="file"
                        className="form-control"
                        accept="image/*,.pdf"
                        disabled={uploading}
                        onChange={(e) => handleFileUpload(e.target.files[0], 'cccdUrl')}
                      />
                      {formData.cccdUrl && (
                        <div className="mt-2">
                          <a href={formData.cccdUrl} target="_blank" rel="noopener noreferrer">
                            <i className="bi bi-file-earmark-arrow-down me-1"></i>{t('adminEmployees.viewCccd')}
                          </a>
                        </div>
                      )}
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">{t('adminEmployees.contractLabel')}</label>
                      <input
                        type="url"
                        className="form-control mb-2"
                        value={formData.contractUrl}
                        onChange={(e) => setFormData({ ...formData, contractUrl: e.target.value })}
                      />
                      <input
                        type="file"
                        className="form-control"
                        accept=".pdf,.doc,.docx"
                        disabled={uploading}
                        onChange={(e) => handleFileUpload(e.target.files[0], 'contractUrl')}
                      />
                      {formData.contractUrl && (
                        <div className="mt-2">
                          <a href={formData.contractUrl} target="_blank" rel="noopener noreferrer">
                            <i className="bi bi-file-earmark-arrow-down me-1"></i>{t('adminEmployees.viewContract')}
                          </a>
                        </div>
                      )}
                    </div>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">{t('employeeProfile.address')}</label>
                    <textarea
                      className="form-control"
                      rows={2}
                      value={formData.address}
                      onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                    ></textarea>
                  </div>
                  <div className="mb-3 form-check">
                    <input
                      type="checkbox"
                      className="form-check-input"
                      checked={formData.isActive}
                      onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
                      id="activeCheck"
                    />
                    <label className="form-check-label" htmlFor="activeCheck">
                      {t('adminEmployees.activeLabel')}
                    </label>
                  </div>
                </form>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  {t('adminEmployees.cancel')}
                </button>
                <button type="submit" className="btn btn-primary" form="employee-form">
                  {editingEmployee ? t('adminEmployees.update') : t('adminEmployees.save')}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
