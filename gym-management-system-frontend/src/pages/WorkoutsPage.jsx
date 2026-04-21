import { useCallback, useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import { api, unwrapApi } from '../app/apiClient';
import { useAuth } from '../app/AuthContext';

export default function WorkoutsPage() {
  const { userId, role } = useAuth();

  const [assignedTrainer, setAssignedTrainer] = useState(null);
  const [loadingTrainer, setLoadingTrainer] = useState(true);
  const [trainerMessage, setTrainerMessage] = useState(null);

  const [plans, setPlans] = useState([]);
  const [exercises, setExercises] = useState([]);
  const [loadingPlans, setLoadingPlans] = useState(true);
  const [loadingExercises, setLoadingExercises] = useState(false);
  const [error, setError] = useState(null);

  const [myMembers, setMyMembers] = useState([]);
  const [loadingMembers, setLoadingMembers] = useState(false);

  const [selectedPlanId, setSelectedPlanId] = useState(null);

  const [showCreate, setShowCreate] = useState(false);
  const [creating, setCreating] = useState(false);
  const [createForm, setCreateForm] = useState({
    memberId: '',
    planName: '',
    description: '',
    durationWeeks: 4,
    difficultyLevel: 'BEGINNER',
    exercises: []
  });

  const handleAddExercise = () => {
    setCreateForm({
      ...createForm,
      exercises: [
        ...createForm.exercises,
        { exerciseName: '', sets: 3, reps: 10, bodyPart: '', instructions: '' }
      ]
    });
  };

  const handleUpdateExercise = (index, field, value) => {
    const updated = [...createForm.exercises];
    updated[index][field] = value;
    setCreateForm({ ...createForm, exercises: updated });
  };

  const handleRemoveExercise = (index) => {
    const updated = [...createForm.exercises];
    updated.splice(index, 1);
    setCreateForm({ ...createForm, exercises: updated });
  };

  const handleCreatePlan = async (e) => {
    e.preventDefault();
    if (!createForm.memberId || !createForm.planName) {
      return toast.error('Member ID and Plan Name are required.');
    }
    setCreating(true);
    try {
      await api.post('/api/workouts/create', {
        ...createForm,
        memberId: parseInt(createForm.memberId, 10),
        durationWeeks: parseInt(createForm.durationWeeks, 10),
        exercises: createForm.exercises.map(ex => ({
          ...ex,
          sets: parseInt(ex.sets, 10),
          reps: parseInt(ex.reps, 10)
        }))
      });
      toast.success('Workout plan assigned successfully!');
      setShowCreate(false);
      setCreateForm({ memberId: '', planName: '', description: '', durationWeeks: 4, difficultyLevel: 'BEGINNER', exercises: [] });
      fetchPlans();
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || 'Failed to create plan');
    } finally {
      setCreating(false);
    }
  };

  const fetchPlans = useCallback(async () => {
    setLoadingPlans(true);
    setError(null);
    try {
      const endpoint = role === 'TRAINER' ? '/api/workouts/trainer/me' : '/api/workouts/me';
      const res = await api.get(endpoint);
      const list = unwrapApi(res.data) || [];
      setPlans(list);
      if (!selectedPlanId && list.length > 0) setSelectedPlanId(list[0].planId);
    } catch (err) {
      setError(err?.response?.data?.message || err?.message || 'Failed to load workout plans');
    } finally {
      setLoadingPlans(false);
    }
  }, [selectedPlanId, userId, role]);

  const fetchExercises = useCallback(async (planId) => {
    if (!planId) return;
    if (role === 'TRAINER') {
      setExercises([]);
      return;
    }
    setLoadingExercises(true);
    try {
      const res = await api.get(`/api/workouts/${planId}/exercises`);
      const list = unwrapApi(res.data) || [];
      setExercises(list);
    } catch (err) {
      toast.error(err?.response?.data?.message || err?.message || 'Failed to load exercises');
    } finally {
      setLoadingExercises(false);
    }
  }, [role]);

  const fetchAssignedTrainer = useCallback(async () => {
    if (role === 'TRAINER') {
      setLoadingTrainer(false);
      setAssignedTrainer(null);
      setTrainerMessage('You are viewing the workouts you authored.');
      return;
    }
    setLoadingTrainer(true);
    setTrainerMessage(null);
    try {
      const res = await api.get('/api/members/me/trainer');
      const trainer = unwrapApi(res.data);
      setAssignedTrainer(trainer);
    } catch (err) {
      const status = err?.response?.status;
      const msg = err?.response?.data?.message || err?.message;

      setAssignedTrainer(null);
      if (status === 404) {
        setTrainerMessage(msg || 'Trainer not assigned yet.');
      } else {
        setTrainerMessage(msg || 'Failed to load assigned trainer');
      }
    } finally {
      setLoadingTrainer(false);
    }
  }, [role]);

  const fetchMyMembers = useCallback(async () => {
    if (role !== 'TRAINER') return;
    setLoadingMembers(true);
    try {
      const res = await api.get('/api/trainers/me/members');
      setMyMembers(unwrapApi(res.data) || []);
    } catch (err) {
      toast.error('Failed to load assigned members');
    } finally {
      setLoadingMembers(false);
    }
  }, [role]);

  useEffect(() => {
    if (!userId) return;
    fetchPlans();
  }, [fetchPlans, userId]);

  useEffect(() => {
    if (!userId) return;
    fetchAssignedTrainer();
    fetchMyMembers();
  }, [fetchAssignedTrainer, fetchMyMembers, userId]);

  useEffect(() => {
    fetchExercises(selectedPlanId);
  }, [fetchExercises, selectedPlanId]);

  const selectedPlan = useMemo(
    () => plans.find((p) => p.planId === selectedPlanId) || null,
    [plans, selectedPlanId]
  );

  return (
    <div>
      <div className="page-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <h1 className="page-title">My Workout Plans</h1>
          <div className="muted">Select a plan to view exercises</div>
        </div>
        {role === 'TRAINER' && (
          <button className="btn btn-primary" onClick={() => setShowCreate(!showCreate)}>
            {showCreate ? 'Cancel' : '+ Create Plan'}
          </button>
        )}
      </div>

      {showCreate && role === 'TRAINER' ? (
        <form onSubmit={handleCreatePlan} className="card mt-16" style={{ marginBottom: '24px' }}>
          <div className="card-title mb-16">Assign New Workout Plan</div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
            <label className="label">
              Assigned Member
              <select className="input" value={createForm.memberId} onChange={(e) => setCreateForm({ ...createForm, memberId: e.target.value })} required disabled={loadingMembers}>
                <option value="" disabled>-- Select a Member --</option>
                {myMembers.map(m => (
                  <option key={m.id} value={m.id}>{m.name}</option>
                ))}
              </select>
            </label>
            <label className="label">
              Plan Name
              <input type="text" className="input" placeholder="E.g. 30-Day Shred" value={createForm.planName} onChange={(e) => setCreateForm({ ...createForm, planName: e.target.value })} required />
            </label>
          </div>
          <label className="label mb-16">
            Description (Optional)
            <input type="text" className="input" placeholder="Goals and focus areas" value={createForm.description} onChange={(e) => setCreateForm({ ...createForm, description: e.target.value })} />
          </label>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '24px' }}>
            <label className="label">
              Duration (Weeks)
              <input type="number" className="input" min="1" value={createForm.durationWeeks} onChange={(e) => setCreateForm({ ...createForm, durationWeeks: e.target.value })} required />
            </label>
            <label className="label">
              Difficulty
              <select className="input" value={createForm.difficultyLevel} onChange={(e) => setCreateForm({ ...createForm, difficultyLevel: e.target.value })}>
                <option value="BEGINNER">Beginner</option>
                <option value="INTERMEDIATE">Intermediate</option>
                <option value="ADVANCED">Advanced</option>
              </select>
            </label>
          </div>

          <div className="card-title" style={{ fontSize: '1.2rem', marginBottom: '16px' }}>Exercises</div>
          {createForm.exercises.map((ex, idx) => (
            <div key={idx} style={{ background: 'var(--slate-50)', padding: '16px', borderRadius: '8px', marginBottom: '16px', border: '1px solid var(--slate-200)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                <div style={{ fontWeight: '500' }}>Exercise {idx + 1}</div>
                <button type="button" className="btn btn-secondary" onClick={() => handleRemoveExercise(idx)} style={{ padding: '4px 8px', fontSize: '0.8rem' }}>Remove</button>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '12px' }}>
                <label className="label">
                  Exercise Name
                  <input type="text" className="input" placeholder="E.g. Bench Press" value={ex.exerciseName} onChange={(e) => handleUpdateExercise(idx, 'exerciseName', e.target.value)} required />
                </label>
                <label className="label">
                  Body Part
                  <input type="text" className="input" placeholder="E.g. Chest" value={ex.bodyPart} onChange={(e) => handleUpdateExercise(idx, 'bodyPart', e.target.value)} required />
                </label>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '12px' }}>
                <label className="label">
                  Sets
                  <input type="number" className="input" min="1" value={ex.sets} onChange={(e) => handleUpdateExercise(idx, 'sets', e.target.value)} required />
                </label>
                <label className="label">
                  Reps
                  <input type="number" className="input" min="1" value={ex.reps} onChange={(e) => handleUpdateExercise(idx, 'reps', e.target.value)} required />
                </label>
              </div>
              <label className="label">
                Instructions (Optional)
                <input type="text" className="input" placeholder="E.g. Keep back straight" value={ex.instructions} onChange={(e) => handleUpdateExercise(idx, 'instructions', e.target.value)} />
              </label>
            </div>
          ))}
          
          <button type="button" className="btn btn-secondary" onClick={handleAddExercise} style={{ width: '100%', marginBottom: '24px' }}>
            + Add Exercise
          </button>

          <button type="submit" className="btn btn-primary" disabled={creating} style={{ width: '100%' }}>
            {creating ? 'Assigning...' : 'Assign Full Plan to Member'}
          </button>
        </form>
      ) : null}

      <div className="card">
        <div className="card-title">Assigned Trainer</div>
        {loadingTrainer ? <div className="muted mt-8">Loading trainer...</div> : null}

        {!loadingTrainer && assignedTrainer ? (
          <div className="mt-8">
            <div>{assignedTrainer.name}</div>
            <div className="muted mt-4">Email: {assignedTrainer.email}</div>
            <div className="muted">Phone: {assignedTrainer.phone}</div>
          </div>
        ) : null}

        {!loadingTrainer && !assignedTrainer ? (
          <div className="muted mt-8">{trainerMessage || 'Trainer not assigned yet.'}</div>
        ) : null}
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
                    <div className="muted">
                      {role === 'TRAINER' ? 'Individual exercise details are viewed by the assigned member.' : 'No exercises assigned for this plan.'}
                    </div>
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

