import { useState } from 'react';
import toast from 'react-hot-toast';
import { api } from '../app/apiClient';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const onSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await api.post('/password/forgot', { email, phone, newPassword });
      toast.success('Password reset successful. Please login.');
      setNewPassword('');
    } catch (err) {
      const msg = err?.response?.data?.message || err?.message || 'Reset failed';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <div className="card page-card">
        <h1 className="page-title">Forgot Password</h1>
        <div className="muted mt-8">
          Enter your registered email + phone, then set a new password.
        </div>

        <form onSubmit={onSubmit} className="form mt-12">
          <label className="label">
            Email
            <input
              className="input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="user@gym.com"
              required
            />
          </label>
          <label className="label">
            Phone
            <input
              className="input"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="9999999999"
              required
            />
          </label>
          <label className="label">
            New Password
            <input
              className="input"
              type="password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              placeholder="••••••••"
              required
            />
          </label>

          {error ? <div className="alert alert-error">{error}</div> : null}

          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? 'Resetting...' : 'Reset Password'}
          </button>
        </form>

        <div className="muted mt-12">
          <a className="link" href="/login">
            Back to login
          </a>
        </div>
      </div>
    </div>
  );
}

