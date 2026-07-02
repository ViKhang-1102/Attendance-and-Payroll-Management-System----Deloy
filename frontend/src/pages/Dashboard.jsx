import { useEffect, useState } from 'react';
import { getProfile } from '../services/auth.js';

export default function Dashboard() {
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const data = await getProfile();
        setProfile(data);
      } catch (err) {
        setError(err.response?.data?.message || 'Không thể tải thông tin người dùng.');
      }
    };

    loadProfile();
  }, []);

  return (
    <div className="row justify-content-center">
      <div className="col-lg-8">
        <div className="card shadow-sm">
          <div className="card-body">
            <h2 className="card-title mb-4">Dashboard</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            {!profile ? (
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
            ) : (
              <div>
                <p>Xin chào, <strong>{profile.fullName || profile.email}</strong></p>
                <p><strong>Email:</strong> {profile.email}</p>
                <p><strong>Role:</strong> {profile.role}</p>
                <p><strong>Department:</strong> {profile.department || 'Chưa có'}</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
