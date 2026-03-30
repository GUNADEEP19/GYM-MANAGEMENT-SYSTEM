import { useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';

export default function AdminCreateUserPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [userType, setUserType] = useState('MEMBER');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const onSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const res = await api.post('/register', {
        name,
        email,
        phone,
        userType,
        password,
      });
      unwrapApi(res.data);
      toast.success(`${userType} created successfully`);
      setName('');
      setEmail('');
      setPhone('');
      setPassword('');
      setUserType('MEMBER');
    } catch (err) {
      const msg = err?.response?.data?.message || err?.message || 'Create user failed';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Create User / Trainer</h1>
        <div className="muted">Only ADMIN can create new users</div>
      </div>

      <div className="card">
        <form onSubmit={onSubmit} className="form">
          <label className="label">
            Name
            <input className="input" value={name} onChange={(e) => setName(e.target.value)} required />
          </label>

          <label className="label">
            Email
            <input className="input" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </label>

          <label className="label">
            Phone
            <input className="input" value={phone} onChange={(e) => setPhone(e.target.value)} required />
          </label>

          <label className="label">
            Role
            <select className="input" value={userType} onChange={(e) => setUserType(e.target.value)}>
              <option value="MEMBER">MEMBER</option>
              <option value="TRAINER">TRAINER</option>
            </select>
          </label>

          <label className="label">
            Temporary Password
            <input
              className="input"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>

          {error ? <div className="alert alert-error">{error}</div> : null}

          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? 'Creating...' : 'Create'}
          </button>
        </form>
      </div>
    </div>
  );
}

