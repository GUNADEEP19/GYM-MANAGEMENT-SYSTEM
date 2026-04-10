import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { useAuth } from '../app/AuthContext';
import { api, unwrapApi } from '../app/apiClient';

export default function TrainerMembersPage() {
  const { auth } = useAuth();
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMembers = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await api.get('/api/trainers/me/members');
        const list = unwrapApi(response.data) || [];
        setMembers(list);
      } catch (err) {
        console.error('Error fetching members:', err);
        setError(err.response?.data?.message || 'Error loading assigned members');
        toast.error('Failed to load members');
      } finally {
        setLoading(false);
      }
    };

    fetchMembers();
  }, [auth?.token]);

  if (loading) {
    return (
      <div className="page">
        <div className="card page-card">
          <p>Loading assigned members...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="card page-card">
        <h1 className="page-title">My Assigned Members</h1>
        
        {error && (
          <div className="alert alert-error" style={{ marginBottom: '1rem' }}>
            {error}
          </div>
        )}

        {members.length === 0 ? (
          <div className="muted">
            <p>You have not been assigned any members yet.</p>
          </div>
        ) : (
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>Member Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                </tr>
              </thead>
              <tbody>
                {members.map((member) => (
                  <tr key={member.userId}>
                    <td>{member.name}</td>
                    <td>{member.email}</td>
                    <td>{member.phone}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
