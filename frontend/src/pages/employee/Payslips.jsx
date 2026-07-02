
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { getMyPayrolls } from '../../services/auth';

export default function EmployeePayslips() {
  const { t } = useTranslation();
  const [payrolls, setPayrolls] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchPayrolls();
  }, []);

  const fetchPayrolls = async () => {
    try {
      setLoading(true);
      const data = await getMyPayrolls();
      setPayrolls(data);
    } catch (err) {
      console.error('Failed to fetch payrolls:', err);
    } finally {
      setLoading(false);
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
        return <span className="badge bg-warning text-dark"><i className="bi bi-clock me-1"></i> {t('employeePayslips.status.pending')}</span>;
      case 'APPROVED':
        return <span className="badge bg-success"><i className="bi bi-check-circle me-1"></i> {t('employeePayslips.status.approved')}</span>;
      case 'PAID':
        return <span className="badge bg-info"><i className="bi bi-check2-circle me-1"></i> {t('employeePayslips.status.paid')}</span>;
      default:
        return <span className="badge bg-secondary">{status}</span>;
    }
  };

  return (
    <div>
      <h2 className="mb-4"><i className="bi bi-wallet2 me-2 text-success"></i> {t('employeePayslips.title')}</h2>

      {loading ? (
        <div className="d-flex justify-content-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        <div className="row g-4">
          {payrolls.length > 0 ? payrolls.map((pay) => (
            <div key={pay.id} className="col-md-6">
              <div className="card shadow-sm border-0 h-100">
                <div className="card-header bg-light d-flex justify-content-between align-items-center">
                  <h5 className="mb-0"><i className="bi bi-calendar3 me-2 text-primary"></i> {t('employeePayslips.payslipMonth')} {pay.month}/{pay.year}</h5>
                  {getStatusBadge(pay.status)}
                </div>
                <div className="card-body">
                  <div className="mb-3">
                    <span className="text-muted"><i className="bi bi-cash-coin me-1"></i> {t('employeePayslips.baseSalary')}</span>
                    <span className="float-end">{formatCurrency(pay.baseSalary)}</span>
                  </div>
                  <div className="mb-3">
                    <span className="text-muted"><i className="bi bi-clock-history me-1"></i> {t('employeePayslips.overtimePay')}</span>
                    <span className="float-end">{formatCurrency(pay.overtimePay)}</span>
                  </div>
                  <div className="mb-3">
                    <span className="text-muted"><i className="bi bi-gift me-1"></i> {t('employeePayslips.allowances')}</span>
                    <span className="float-end">{formatCurrency(pay.allowances)}</span>
                  </div>
                  <div className="mb-3">
                    <span className="text-muted"><i className="bi bi-shield-check me-1"></i> {t('employeePayslips.insurance')}</span>
                    <span className="float-end text-danger">{formatCurrency(pay.insurance)}</span>
                  </div>
                  <div className="mb-3">
                    <span className="text-muted"><i className="bi bi-file-earmark-text me-1"></i> {t('employeePayslips.tax')}</span>
                    <span className="float-end text-danger">{formatCurrency(pay.tax)}</span>
                  </div>
                  <hr />
                  <div className="mb-3">
                    <span className="text-muted"><i className="bi bi-dash-circle me-1"></i> {t('employeePayslips.deductions')}</span>
                    <span className="float-end text-danger">{formatCurrency(pay.deductions)}</span>
                  </div>
                  <hr />
                  <div>
                    <strong className="fs-5"><i className="bi bi-wallet me-1"></i> {t('employeePayslips.netSalary')}</strong>
                    <strong className="float-end fs-5 text-success">{formatCurrency(pay.netSalary)}</strong>
                  </div>
                </div>
              </div>
            </div>
          )) : (
            <div className="col-12">
              <div className="card shadow-sm border-0">
                <div className="card-body text-center py-5 text-muted">
                  <i className="bi bi-inbox fs-3"></i>
                  <div className="mt-2">{t('employeePayslips.noPayslips')}</div>
                </div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
