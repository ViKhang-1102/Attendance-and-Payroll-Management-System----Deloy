import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export default function Home({ authenticated }) {
  const { t } = useTranslation();

  return (
    <div className="text-center">
      <div className="p-5 mb-4 bg-white rounded-3 shadow-sm">
        <h1 className="display-6">Attendance & Payroll System</h1>
        <p className="lead">{t('home.loginPrompt')}</p>
        <hr className="my-4" />
        {authenticated ? (
          <p>{t('home.loggedInMessage')}</p>
        ) : (
          <p>{t('home.unauthenticatedMessage')}</p>
        )}
      </div>
      <div className="row g-4 justify-content-center">
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">{t('home.start')}</h5>
              <p className="card-text">{t('home.loginPrompt')}</p>
              <Link className="btn btn-primary" to="/login">{t('auth.login')}</Link>
            </div>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">{t('home.createAccount')}</h5>
              <p className="card-text">{t('home.createAccountDescription')}</p>
              <Link className="btn btn-outline-primary" to="/register">{t('home.createAccount')}</Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
