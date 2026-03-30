import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';

export default function AdminReportPage() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [report, setReport] = useState(null);

  useEffect(() => {
    const run = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await api.get('/api/report/dashboard');
        setReport(unwrapApi(res.data));
      } catch (err) {
        const msg = err?.response?.data?.message || err?.message || 'Failed to load report';
        setError(msg);
        toast.error(msg);
      } finally {
        setLoading(false);
      }
    };
    run();
  }, []);

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Admin Dashboard</h1>
        <div className="muted">Gym overview metrics</div>
      </div>

      {loading ? <div className="muted">Loading...</div> : null}
      {error ? <div className="alert alert-error">{error}</div> : null}

      {report ? (
        <div className="grid-2">
          <div className="card">
            <div className="card-title">Members</div>
            <div className="metric">{report.totalMembers}</div>
            <div className="muted mt-4">Total members registered</div>
          </div>

          <div className="card">
            <div className="card-title">Trainers</div>
            <div className="metric">{report.activeTrainers}</div>
            <div className="muted mt-4">Active trainers</div>
          </div>

          <div className="card">
            <div className="card-title">Revenue</div>
            <div className="metric">{report.totalRevenue}</div>
            <div className="muted mt-4">Total revenue</div>
          </div>

          <div className="card">
            <div className="card-title">Today Attendance</div>
            <div className="metric">{report.todayAttendanceCount}</div>
            <div className="muted mt-4">Check-ins today</div>
          </div>
        </div>
      ) : null}
    </div>
  );
}

