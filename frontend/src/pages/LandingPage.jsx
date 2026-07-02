
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export default function LandingPage() {
  const { t, i18n } = useTranslation();
  
  const changeLanguage = (lng) => {
    i18n.changeLanguage(lng);
  };

  return (
    <div className="bg-light d-flex flex-column flex-grow-1">
      <nav className="navbar navbar-expand-lg navbar-dark bg-gradient shadow flex-shrink-0">
        <div className="container-fluid px-3 px-lg-5 d-flex align-items-center justify-content-between">
          <Link className="navbar-brand fw-bold d-flex align-items-center" to="/">
            <i className="bi bi-clock-history me-2 fs-4"></i>
            <span className="d-none d-sm-inline">{t('landing.title')}</span>
            <span className="d-inline d-sm-none">{t('landing.titleShort')}</span>
          </Link>
          <button
            className="navbar-toggler ms-2"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav ms-auto align-items-center gap-2 flex-wrap">

              <li className="nav-item">
                <div className="btn-group btn-group-sm" role="group">
                  <button
                    type="button"
                    className={`btn ${i18n.language === 'vi' ? 'btn-light' : 'btn-outline-light'}`}
                    onClick={() => changeLanguage('vi')}
                  >
                    VI
                  </button>
                  <button
                    type="button"
                    className={`btn ${i18n.language === 'en' ? 'btn-light' : 'btn-outline-light'}`}
                    onClick={() => changeLanguage('en')}
                  >
                    EN
                  </button>
                </div>
              </li>
              <li className="nav-item">
                <Link className="btn btn-outline-light btn-sm" to="/login">
                  {t('auth.login')}
                </Link>
              </li>
            </ul>
          </div>
        </div>
      </nav>

      <main className="container-fluid px-3 px-lg-5 py-5 flex-grow-1">
        <div className="row align-items-center g-5 justify-content-center">
          <div className="col-12 col-xl-6">
            <h1 className="display-4 fw-bold mb-4">
              {t('landing.title')}
              <br />
              <span className="text-primary">{t('landing.subtitle')}</span>
            </h1>
            <p className="lead mb-4 text-muted">
              {t('landing.subtitle')}
            </p>
            <div className="d-flex gap-3 flex-wrap">
              <Link to="/login" className="btn btn-gradient btn-lg px-4">
                {t('landing.getStarted')}
              </Link>
            </div>
          </div>
          <div className="col-12 col-xl-6">
            <div className="card shadow-lg p-4 bg-white">
              <div className="card-body">
                <h4 className="card-title mb-4">{t('home.start')}</h4>
                <div className="row g-3">
                  <div className="col-6">
                    <div className="d-flex align-items-center gap-2">
                      <div className="bg-primary bg-opacity-10 p-2 rounded">
                        <span className="text-primary">✅</span>
                      </div>
                      <span>{t('landing.features.onlineAttendance')}</span>
                    </div>
                  </div>
                  <div className="col-6">
                    <div className="d-flex align-items-center gap-2">
                      <div className="bg-primary bg-opacity-10 p-2 rounded">
                        <span className="text-primary">✅</span>
                      </div>
                      <span>{t('landing.features.leaveRequests')}</span>
                    </div>
                  </div>
                  <div className="col-6">
                    <div className="d-flex align-items-center gap-2">
                      <div className="bg-primary bg-opacity-10 p-2 rounded">
                        <span className="text-primary">✅</span>
                      </div>
                      <span>{t('landing.features.autoPayroll')}</span>
                    </div>
                  </div>
                  <div className="col-6">
                    <div className="d-flex align-items-center gap-2">
                      <div className="bg-primary bg-opacity-10 p-2 rounded">
                        <span className="text-primary">✅</span>
                      </div>
                      <span>{t('landing.features.reports')}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>

      <footer className="bg-dark text-light py-4 flex-shrink-0">
        <div className="container-fluid px-3 px-lg-5 text-center">
          <p className="mb-0">&copy; 2026 Attendance & Payroll Management System. All rights reserved.</p>
        </div>
      </footer>

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
