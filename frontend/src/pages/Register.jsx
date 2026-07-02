import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { registerUser } from '../services/auth.js';

export default function Register() {
  const { t } = useTranslation();
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('EMPLOYEE');
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    setError(null);
    setMessage(null);

    try {
      await registerUser({ fullName, email, password, role });
      setMessage(t('auth.registerSuccess'));
      setFullName('');
      setEmail('');
      setPassword('');
      setRole('EMPLOYEE');
    } catch (err) {
      setError(err.response?.data?.message || t('auth.registerError'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-lg-6">
        <div className="card shadow-sm">
          <div className="card-body">
            <h2 className="card-title mb-4">{t('auth.register')}</h2>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">{t('auth.fullName')}</label>
                <input
                  className="form-control"
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">{t('auth.email')}</label>
                <input
                  type="email"
                  className="form-control"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">{t('auth.password')}</label>
                <input
                  type="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <div className="mb-3">
                <label className="form-label">{t('auth.role')}</label>
                <select className="form-select" value={role} onChange={(e) => setRole(e.target.value)}>
                  <option value="EMPLOYEE">EMPLOYEE</option>
                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>
              {message && <div className="alert alert-success">{message}</div>}
              {error && <div className="alert alert-danger">{error}</div>}
              <button className="btn btn-primary" type="submit" disabled={loading}>
                {loading ? t('common.loading') : t('auth.register')}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
