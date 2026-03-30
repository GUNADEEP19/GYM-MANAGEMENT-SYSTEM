import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';
import { useAuth } from '../app/AuthContext';

export default function ProgressPage() {
  const { userId } = useAuth();

  const [plans, setPlans] = useState([]);
  const [records, setRecords] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [form, setForm] = useState({
    weekNumber: 1,
    exercisesDone: 0,
    weight: '',
    bmi: '',
    progressNotes: '',
    planId: '',
  });

  const loadPlans = async () => {
    const res = await api.get(`/workout/member/${userId}`);
    const list = unwrapApi(res.data) || [];
    setPlans(list);
    if (!form.planId && list.length > 0) {
      setForm((f) => ({ ...f, planId: list[0].planId }));
    }
  };

  const loadRecords = async () => {
    const res = await api.get(`/progress/member/${userId}`);
    const list = unwrapApi(res.data) || [];
    setRecords(list);
  };

  useEffect(() => {
    const run = async () => {
      setLoading(true);
      setError(null);
      try {
        await Promise.all([loadPlans(), loadRecords()]);
      } catch (err) {
        setError(err?.response?.data?.message || err?.message || 'Failed to load progress');
      } finally {
        setLoading(false);
      }
    };
    if (!userId) return;
    run();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  const onSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      const payload = {
        weekNumber: Number(form.weekNumber),
        exercisesDone: Number(form.exercisesDone),
        weight: form.weight === '' ? null : Number(form.weight),
        bmi: form.bmi === '' ? null : Number(form.bmi),
        progressNotes: form.progressNotes || '',
        memberId: userId,
        planId: form.planId,
      };

      // backend uses @Positive for weight/bmi, so send numbers.
      if (payload.weight == null || payload.bmi == null) {
        throw new Error('Weight and BMI are required');
      }

      const res = await api.post('/progress/update', payload);
      unwrapApi(res.data);
      toast.success('Progress updated');
      await loadRecords();
      // keep plan selected; reset note only
      setForm((f) => ({ ...f, progressNotes: '' }));
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || 'Update failed');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Progress</h1>
        <div className="muted">Update your weekly workout metrics</div>
      </div>

      {loading ? <div className="muted">Loading progress...</div> : null}
      {error ? <div className="alert alert-error">{error}</div> : null}

      <div className="grid-2">
        <div className="card">
          <div className="card-title">Update Progress</div>
          <form onSubmit={onSubmit} className="form mt-8">
            <label className="label">
              Plan
              <select
                className="input"
                value={form.planId}
                onChange={(e) => setForm((f) => ({ ...f, planId: e.target.value }))}
                required
              >
                {plans.map((p) => (
                  <option key={p.planId} value={p.planId}>
                    {p.planName}
                  </option>
                ))}
              </select>
            </label>

            <label className="label">
              Week Number
              <input
                className="input"
                type="number"
                min="1"
                value={form.weekNumber}
                onChange={(e) => setForm((f) => ({ ...f, weekNumber: e.target.value }))}
                required
              />
            </label>

            <label className="label">
              Exercises Done
              <input
                className="input"
                type="number"
                min="0"
                value={form.exercisesDone}
                onChange={(e) => setForm((f) => ({ ...f, exercisesDone: e.target.value }))}
                required
              />
            </label>

            <label className="label">
              Weight
              <input
                className="input"
                type="number"
                step="0.01"
                value={form.weight}
                onChange={(e) => setForm((f) => ({ ...f, weight: e.target.value }))}
                required
              />
            </label>

            <label className="label">
              BMI
              <input
                className="input"
                type="number"
                step="0.01"
                value={form.bmi}
                onChange={(e) => setForm((f) => ({ ...f, bmi: e.target.value }))}
                required
              />
            </label>

            <label className="label">
              Notes (optional)
              <textarea
                className="input"
                value={form.progressNotes}
                onChange={(e) => setForm((f) => ({ ...f, progressNotes: e.target.value }))}
                rows="3"
              />
            </label>

            <button className="btn btn-primary" type="submit">
              Save Progress
            </button>
          </form>
        </div>

        <div className="card">
          <div className="card-title">My Records</div>
          <div className="mt-8">
            {records.length === 0 ? (
              <div className="muted">No progress records yet.</div>
            ) : (
              records.map((r) => (
                <div key={r.progressId} className="record-row">
                  <div className="record-title">
                    Week {r.weekNumber} | {r.planName}
                  </div>
                  <div className="muted">
                    {r.exercisesDone} exercises | Weight: {r.weight} | BMI: {r.bmi}
                  </div>
                  {r.progressNotes ? <div className="record-notes">{r.progressNotes}</div> : null}
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

