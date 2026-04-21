import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';

export default function AdminCreateUserPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [userType, setUserType] = useState('MEMBER');
  const [password, setPassword] = useState('');
  const [trainerUserId, setTrainerUserId] = useState('');
  const [trainers, setTrainers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingTrainers, setLoadingTrainers] = useState(false);
  const [error, setError] = useState(null);

  // Fetch trainers on component mount
  useEffect(() => {
    const fetchTrainers = async () => {
      try {
        setLoadingTrainers(true);
        const res = await api.get('/api/admin/trainers');
        const list = unwrapApi(res.data) || [];
        setTrainers(list);
      } catch (err) {
        console.error('Error fetching trainers:', err);
        // Don't show error toast as it's not critical
      } finally {
        setLoadingTrainers(false);
      }
    };

    fetchTrainers();
  }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const payload = {
        name,
        email,
        phone,
        userType,
        password,
      };

      // Add trainer ID if member is selected and trainer is chosen
      if (userType === 'MEMBER' && trainerUserId) {
        payload.trainerUserId = trainerUserId;
      }

      const res = await api.post('/api/admin/users', payload);
      unwrapApi(res.data);
      toast.success(`${userType} created successfully${trainerUserId ? ' with trainer assigned' : ''}`);

      // Reset form
      setName('');
      setEmail('');
      setPhone('');
      setPassword('');
      setUserType('MEMBER');
      setTrainerUserId('');
    } catch (err) {
      const msg = err?.response?.data?.message || err?.message || 'Create user failed';
      setError(msg);
      toast.error(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Create User / Trainer</h1>
        <div className="muted">Only ADMIN can create new users and assign trainers to members</div>
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
            <select className="input" value={userType} onChange={(e) => {
              setUserType(e.target.value);
              setTrainerUserId(''); // Reset trainer when role changes
            }}>
              <option selected disabled value="">Select Role</option>
              <option value="MEMBER">MEMBER</option>
              <option value="TRAINER">TRAINER</option>
            </select>
          </label>

          {userType === 'MEMBER' && (
            <label className="label">
              Assign Trainer (Optional)
              <select
                className="input"
                value={trainerUserId}
                onChange={(e) => setTrainerUserId(e.target.value)}
                disabled={loadingTrainers}
              >
                <option disabled value="">-- No Trainer --</option>
                {trainers.map((trainer) => (
                  <option key={trainer.userId} value={trainer.userId}>
                    {trainer.name} ({trainer.email})
                  </option>
                ))}
              </select>
              {loadingTrainers && <div className="muted" style={{ fontSize: '0.85rem', marginTop: '0.25rem' }}>Loading trainers...</div>}
            </label>
          )}

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

          <button className="btn btn-primary" type="submit" disabled={loading || loadingTrainers}>
            {loading ? 'Creating...' : 'Create'}
          </button>
        </form>
      </div>
    </div>
  );
}

