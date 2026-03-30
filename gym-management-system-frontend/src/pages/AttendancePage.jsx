import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';
import { useAuth } from '../app/AuthContext';

function formatDate(d) {
  if (!d) return '';
  // ISO string -> YYYY-MM-DD
  return String(d).slice(0, 10);
}

export default function AttendancePage() {
  const { userId } = useAuth();

  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [checkInDate, setCheckInDate] = useState('');

  const loadRecords = async () => {
    const res = await api.get(`/attendance/member/${userId}`);
    const list = unwrapApi(res.data) || [];
    setRecords(list);
  };

  useEffect(() => {
    const run = async () => {
      setLoading(true);
      setError(null);
      try {
        await loadRecords();
      } catch (err) {
        setError(err?.response?.data?.message || err?.message || 'Failed to load attendance');
      } finally {
        setLoading(false);
      }
    };
    if (!userId) return;
    run();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  const onCheckIn = async (e) => {
    e.preventDefault();
    try {
      await api.post('/attendance/checkin', {
        attendanceDate: checkInDate ? checkInDate : null,
        memberId: userId,
      });
      toast.success('Checked in successfully');
      setCheckInDate('');
      await loadRecords();
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || 'Check-in failed');
    }
  };

  const onCheckOut = async (attendanceId) => {
    try {
      await api.post(`/attendance/checkout/${attendanceId}`);
      toast.success('Checked out successfully');
      await loadRecords();
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || 'Check-out failed');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Attendance</h1>
        <div className="muted">Check in and check out</div>
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-title">Check In</div>
          <form onSubmit={onCheckIn} className="form mt-8">
            <label className="label">
              Date (optional)
              <input
                className="input"
                type="date"
                value={checkInDate}
                onChange={(e) => setCheckInDate(e.target.value)}
              />
            </label>

            <div className="muted mt-4">If you leave it blank, server uses today.</div>

            <button className="btn btn-primary mt-12" type="submit">
              Check In
            </button>
          </form>
        </div>

        <div className="card">
          <div className="card-title">My Records</div>
          {loading ? <div className="muted mt-8">Loading...</div> : null}
          {error ? <div className="alert alert-error mt-8">{error}</div> : null}

          <div className="mt-8">
            {records.length === 0 && !loading ? <div className="muted">No attendance records.</div> : null}

            {records.map((r) => (
              <div key={r.attendanceId} className="record-row">
                <div className="record-title">
                  {r.attendanceDate ? formatDate(r.attendanceDate) : '—'} | {r.status}
                </div>
                <div className="muted">
                  In: {r.checkInTime ? new Date(r.checkInTime).toLocaleString() : '—'}
                  {r.checkOutTime ? ` | Out: ${new Date(r.checkOutTime).toLocaleString()}` : ''}
                </div>

                {r.status === 'CHECKED_IN' ? (
                  <button className="btn btn-secondary mt-8" type="button" onClick={() => onCheckOut(r.attendanceId)}>
                    Check Out
                  </button>
                ) : null}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

