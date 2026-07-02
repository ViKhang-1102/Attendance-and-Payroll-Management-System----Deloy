
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { login } from '../services/auth';

export default function Login({ onLogin }) {
  const { t } = useTranslation();
  const [identifier, setIdentifier] = useState('admin@company.com');
  const [password, setPassword] = useState('admin123');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const result = await login({ identifier, password });
      localStorage.setItem('token', result.token);
      localStorage.setItem('userEmail', identifier);
      onLogin(result);
    } catch (err) {
      setError(err.response?.data?.message || t('auth.loginError'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gradient min-vh-100 d-flex align-items-center justify-content-center">
      <div className="card shadow-lg border-0" style={{ width: '100%', maxWidth: '450px', borderRadius: '16px' }}>
        <div className="card-body p-5">
          <div className="text-center mb-4">
            <h2 className="fw-bold">{t('auth.login')}</h2>
            <p className="text-muted">Attendance & Payroll Management System</p>
          </div>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">{t('auth.email')}</label>
              <input
                type="text"
                className="form-control"
                value={identifier}
                onChange={(e) => setIdentifier(e.target.value)}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">{t('auth.password')}</label>
              <div className="input-group">
                <input
                  type={showPassword ? 'text' : 'password'}
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <button
                  type="button"
                  className="btn btn-outline-secondary"
                  onClick={() => setShowPassword((prev) => !prev)}
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                >
                  <i className={`bi ${showPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                </button>
              </div>
            </div>
            <div className="mb-3 d-flex justify-content-end">
              <Link to="/forgot-password" className="text-decoration-none small text-primary">
                {t('auth.forgotPassword')}
              </Link>
            </div>
            {error && <div className="alert alert-danger">{error}</div>}
            <button className="btn btn-gradient w-100" type="submit" disabled={loading}>
              {loading ? <span className="spinner-border spinner-border-sm me-2"></span> : null}
              {t('auth.login')}
            </button>
          </form>
          <div className="text-center mt-4">
            <Link to="/" className="text-decoration-none small text-primary">
              &larr; {t('auth.backToHome')}
            </Link>
          </div>
        </div>
      </div>
      <style>{`
        .bg-gradient {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .btn-gradient {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border: none;
          color: white;
          font-weight: 600;
        }
        .btn-gradient:hover {
          background: linear-gradient(135deg, #5a6fd6 0%, #6a3d8f 100%);
        }
      `}</style>
    </div>
  );
}
