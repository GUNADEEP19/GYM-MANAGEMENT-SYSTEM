import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';
import { useAuth } from '../app/AuthContext';

export default function RecommendationsPage() {
  const { userId } = useAuth();

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [rec, setRec] = useState(null);

  useEffect(() => {
    const run = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await api.get('/api/recommendation/me');
        setRec(unwrapApi(res.data));
      } catch (err) {
        const msg = err?.response?.data?.message || err?.message || 'Failed to load recommendation';
        setError(msg);
        toast.error(msg);
      } finally {
        setLoading(false);
      }
    };
    if (!userId) return;
    run();
  }, [userId]);

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">Recommendation</h1>
        <div className="muted">Personalized strategy based on your BMI</div>
      </div>

      {loading ? <div className="muted">Loading...</div> : null}
      {error ? <div className="alert alert-error">{error}</div> : null}

      {rec ? (
        <div className="grid-2">
          <div className="card">
            <div className="card-title">Your Plan</div>
            <div className="mt-8 muted">Member: {rec.memberName}</div>
            <div className="mt-8 muted">Current BMI: {rec.currentBmi}</div>

            <div className="mt-16">
              <div className="label">Selected Strategy</div>
              <div className="value">{rec.selectedStrategy}</div>
            </div>

            <div className="mt-16">
              <div className="label">Advised Goal</div>
              <div className="value">{rec.advisedGoal}</div>
            </div>

            <div className="mt-16">
              <div className="label">Recommended Duration</div>
              <div className="value">{rec.recommendedDurationWeeks} weeks</div>
            </div>
          </div>

          <div className="card">
            <div className="card-title">Recommended Exercises</div>
            <div className="mt-8">
              {rec.recommendedExercises?.length ? (
                <ul className="list">
                  {rec.recommendedExercises.map((ex, idx) => (
                    <li key={`${ex}-${idx}`}>{ex}</li>
                  ))}
                </ul>
              ) : (
                <div className="muted">No exercises recommended yet.</div>
              )}
            </div>
          </div>
        </div>
      ) : null}
    </div>
  );
}

