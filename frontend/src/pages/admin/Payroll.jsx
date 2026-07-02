
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getPayrolls, generatePayrollForAll, updatePayroll, getUsers } from '../../services/auth';

export default function AdminPayroll() {
  const { t } = useTranslation();
  const [payrolls, setPayrolls] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

  useEffect(() => {
    fetchAllData();
  }, []);

  const fetchAllData = async () => {
    try {
      setLoading(true);
      const [payrollData, userData] = await Promise.all([
        getPayrolls(),
        getUsers()
      ]);
      setPayrolls(payrollData);
      setUsers(userData);
    } catch (err) {
      console.error('Failed to fetch data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleGenerate = async () => {
    try {
      const generated = await generatePayrollForAll(selectedMonth, selectedYear);
      await fetchAllData();
      if (generated.length > 0) {
        alert(t('adminPayroll.generatedSuccess', { count: generated.length }));
      } else {
        alert(t('adminPayroll.generatedNone'));
      }
    } catch (err) {
      console.error('Failed to generate payroll:', err);
      alert(err.response?.data?.message || t('adminPayroll.generatedError'));
    }
  };

  const handleApprove = async (payrollId) => {
    try {
      await updatePayroll(payrollId, { status: 'APPROVED' });
      await fetchAllData();
      alert(t('adminPayroll.approveSuccess'));
    } catch (err) {
      console.error('Failed to approve payroll:', err);
      alert(err.response?.data?.message || t('adminPayroll.approveError'));
    }
  };

  const handleMarkPaid = async (payrollId) => {
    try {
      await updatePayroll(payrollId, { isPaid: true, status: 'PAID' });
      await fetchAllData();
      alert(t('adminPayroll.markPaidSuccess'));
    } catch (err) {
      console.error('Failed to mark payroll as paid:', err);
      alert(err.response?.data?.message || t('adminPayroll.markPaidError'));
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount);
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'PENDING':
        return <span className="badge bg-warning">{t('adminPayroll.status.pending')}</span>;
      case 'APPROVED':
        return <span className="badge bg-success">{t('adminPayroll.status.approved')}</span>;
      case 'PAID':
        return <span className="badge bg-info">{t('adminPayroll.status.paid')}</span>;
      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>{t('adminPayroll.title')}</h2>
        <div className="d-flex gap-2 flex-wrap">
          <select
            className="form-select"
            style={{ width: '100%', maxWidth: '120px' }}
            value={selectedMonth}
            onChange={(e) => setSelectedMonth(Number(e.target.value))}
          >
            {[1,2,3,4,5,6,7,8,9,10,11,12].map(m => (
              <option key={m} value={m}>{t('adminPayroll.month', { month: m })}</option>
            ))}
          </select>
          <select
            className="form-select"
            style={{ width: '100%', maxWidth: '120px' }}
            value={selectedYear}
            onChange={(e) => setSelectedYear(Number(e.target.value))}
          >
            {[2024,2025,2026,2027].map(y => (
              <option key={y} value={y}>{y}</option>
            ))}
          </select>
          <button className="btn btn-primary" onClick={handleGenerate}>
            {t('adminPayroll.generate')}
          </button>
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
                    <th>{t('adminPayroll.table.employee')}</th>
                    <th>{t('adminPayroll.table.month')}</th>
                    <th>{t('adminPayroll.table.baseSalary')}</th>
                    <th>{t('adminPayroll.table.overtimePay')}</th>
                    <th>{t('adminPayroll.table.allowances')}</th>
                    <th>{t('adminPayroll.table.insurance')}</th>
                    <th>{t('adminPayroll.table.tax')}</th>
                    <th>{t('adminPayroll.table.deductions')}</th>
                    <th>{t('adminPayroll.table.netSalary')}</th>
                    <th>{t('adminPayroll.table.status')}</th>
                    <th>{t('common.actions')}</th>
                  </tr>
                </thead>
                <tbody>
                  {payrolls.map((pay) => {
                    const user = users.find(u => u.id === pay.userId);
                    return (
                      <tr key={pay.id}>
                        <td>
                          <div className="d-flex align-items-center">
                            {user?.avatarUrl ? (
                              <img src={user.avatarUrl} alt="Avatar" className="rounded-circle me-2" style={{ width: 32, height: 32, objectFit: 'cover' }} />
                            ) : (
                              <div className="rounded-circle bg-secondary me-2 d-flex align-items-center justify-content-center" style={{ width: 32, height: 32 }}>
                                <i className="bi bi-person text-white"></i>
                              </div>
                            )}
                            {pay.userName}
                          </div>
                        </td>
                        <td>{pay.month}/{pay.year}</td>
                        <td>{formatCurrency(pay.baseSalary)}</td>
                        <td>{formatCurrency(pay.overtimePay)}</td>
                        <td>{formatCurrency(pay.allowances)}</td>
                        <td>{formatCurrency(pay.insurance)}</td>
                        <td>{formatCurrency(pay.tax)}</td>
                        <td>{formatCurrency(pay.deductions)}</td>
                        <td><strong>{formatCurrency(pay.netSalary)}</strong></td>
                        <td>{getStatusBadge(pay.status)}</td>
                        <td>
                          <div className="d-flex gap-1 flex-wrap">
                            {pay.status === 'PENDING' && (
                              <button
                                className="btn btn-success btn-sm"
                                onClick={() => handleApprove(pay.id)}
                              >
                                {t('adminPayroll.actions.approve')}
                              </button>
                            )}
                            {pay.status === 'APPROVED' && !pay.isPaid && (
                              <button
                                className="btn btn-info btn-sm"
                                onClick={() => handleMarkPaid(pay.id)}
                              >
                                {t('adminPayroll.actions.markPaid')}
                              </button>
                            )}
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
