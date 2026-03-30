import { useCallback, useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';
import { useAuth } from '../app/AuthContext';

export default function WorkoutsPage() {
  const { userId } = useAuth();

  const [plans, setPlans] = useState([]);
  const [exercises, setExercises] = useState([]);
  const [loadingPlans, setLoadingPlans] = useState(true);
  const [loadingExercises, setLoadingExercises] = useState(false);
  const [error, setError] = useState(null);

  const [selectedPlanId, setSelectedPlanId] = useState(null);

  const fetchPlans = useCallback(async () => {
    setLoadingPlans(true);
    setError(null);
    try {
      const res = await api.get(`/workout/member/${userId}`);
      const list = unwrapApi(res.data) || [];
      setPlans(list);
      if (!selectedPlanId && list.length > 0) setSelectedPlanId(list[0].planId);
    } catch (err) {
      setError(err?.response?.data?.message || err?.message || 'Failed to load workout plans');
    } finally {
      setLoadingPlans(false);
    }
  }, [selectedPlanId, userId]);

  const fetchExercises = useCallback(async (planId) => {
    if (!planId) return;
    setLoadingExercises(true);
    try {
      const res = await api.get(`/workout/${planId}/exercises`);
      const list = unwrapApi(res.data) || [];
      setExercises(list);
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || 'Failed to load exercises');
    } finally {
      setLoadingExercises(false);
    }
  }, []);

  useEffect(() => {
    if (!userId) return;
    fetchPlans();
  }, [fetchPlans, userId]);

  useEffect(() => {
    fetchExercises(selectedPlanId);
  }, [fetchExercises, selectedPlanId]);

  const selectedPlan = useMemo(
    () => plans.find((p) => p.planId === selectedPlanId) || null,
    [plans, selectedPlanId]
  );

  return (
    <div>
      <div className="page-header">
        <h1 className="page-title">My Workout Plans</h1>
        <div className="muted">Select a plan to view exercises</div>
      </div>

      {loadingPlans ? <div className="muted">Loading plans...</div> : null}
      {error ? <div className="alert alert-error">{error}</div> : null}

      <div className="workouts-layout">
        <div className="workouts-left">
          {plans.map((p) => (
            <button
              key={p.planId}
              className={`workout-item ${p.planId === selectedPlanId ? 'workout-item-active' : ''}`}
              onClick={() => setSelectedPlanId(p.planId)}
              type="button"
            >
              <div className="workout-item-title">{p.planName}</div>
              <div className="muted">
                {p.durationWeeks} weeks | Difficulty: {p.difficultyLevel}
              </div>
            </button>
          ))}

          {plans.length === 0 && !loadingPlans ? <div className="muted">No workout plans found.</div> : null}
        </div>

        <div className="workouts-right">
          {selectedPlan ? (
            <>
              <div className="card">
                <div className="card-title">{selectedPlan.planName}</div>
                <div className="muted mt-4">{selectedPlan.description}</div>
                <div className="mt-12">
                  <div className="muted">Exercise count: {selectedPlan.exerciseCount}</div>
                </div>
              </div>

              <div className="mt-16 card">
                <div className="card-title">Exercises</div>
                {loadingExercises ? <div className="muted mt-8">Loading exercises...</div> : null}
                <div className="mt-8">
                  {exercises.length === 0 && !loadingExercises ? (
                    <div className="muted">No exercises assigned for this plan.</div>
                  ) : null}
                  {exercises.map((ex) => (
                    <div key={ex.exerciseId} className="exercise-row">
                      <div className="exercise-name">{ex.exerciseName}</div>
                      <div className="muted">
                        {ex.sets} sets x {ex.reps} reps | {ex.bodyPart}
                      </div>
                      {ex.instructions ? <div className="exercise-instructions">{ex.instructions}</div> : null}
                    </div>
                  ))}
                </div>
              </div>
            </>
          ) : (
            <div className="muted">Select a plan to see details.</div>
          )}
        </div>
      </div>
    </div>
  );
}

