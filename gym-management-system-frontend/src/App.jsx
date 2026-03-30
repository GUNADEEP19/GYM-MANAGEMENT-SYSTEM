import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import './App.css';

import { AuthProvider, useAuth } from './app/AuthContext';
import AppLayout from './layout/AppLayout';
import ProtectedRoute from './components/ProtectedRoute';

import LoginPage from './pages/LoginPage';
import ForgotPasswordPage from './pages/ForgotPasswordPage';
import PackagesPage from './pages/PackagesPage';
import WorkoutsPage from './pages/WorkoutsPage';
import ProgressPage from './pages/ProgressPage';
import AttendancePage from './pages/AttendancePage';
import RecommendationsPage from './pages/RecommendationsPage';
import AdminReportPage from './pages/AdminReportPage';
import AdminCreateUserPage from './pages/AdminCreateUserPage';
import TrainerMembersPage from './pages/TrainerMembersPage';

function PublicOnly({ children }) {
  const { isAuthenticated, role } = useAuth();
  if (isAuthenticated) {
    // Redirect to role-specific landing page
    if (role === 'ADMIN') return <Navigate to="/admin/create-user" replace />;
    if (role === 'TRAINER') return <Navigate to="/trainer/members" replace />;
    return <Navigate to="/packages" replace />;
  }
  return children;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: { fontSize: '14px' },
          }}
        />

        <Routes>
          <Route
            path="/login"
            element={
              <PublicOnly>
                <LoginPage />
              </PublicOnly>
            }
          />
          <Route
            path="/forgot-password"
            element={
              <PublicOnly>
                <ForgotPasswordPage />
              </PublicOnly>
            }
          />

          <Route element={<ProtectedRoute><AppLayout /></ProtectedRoute>}>
            <Route index element={<Navigate to="/packages" replace />} />
            
            {/* Member-only routes */}
            <Route
              path="packages"
              element={
                <ProtectedRoute requiredRoles={['MEMBER']}>
                  <PackagesPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="workouts"
              element={
                <ProtectedRoute requiredRoles={['MEMBER']}>
                  <WorkoutsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="progress"
              element={
                <ProtectedRoute requiredRoles={['MEMBER']}>
                  <ProgressPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="attendance"
              element={
                <ProtectedRoute requiredRoles={['MEMBER']}>
                  <AttendancePage />
                </ProtectedRoute>
              }
            />
            <Route
              path="recommendation"
              element={
                <ProtectedRoute requiredRoles={['MEMBER']}>
                  <RecommendationsPage />
                </ProtectedRoute>
              }
            />

            {/* Trainer-only routes */}
            <Route
              path="trainer/members"
              element={
                <ProtectedRoute requiredRoles={['TRAINER']}>
                  <TrainerMembersPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="trainer/workouts"
              element={
                <ProtectedRoute requiredRoles={['TRAINER']}>
                  <WorkoutsPage />
                </ProtectedRoute>
              }
            />

            {/* Admin-only routes */}
            <Route
              path="admin/create-user"
              element={
                <ProtectedRoute requiredRoles={['ADMIN']}>
                  <AdminCreateUserPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="admin/report"
              element={
                <ProtectedRoute requiredRoles={['ADMIN']}>
                  <AdminReportPage />
                </ProtectedRoute>
              }
            />
          </Route>

          <Route
            path="*"
            element={
              <div className="page">
                <div className="card page-card">
                  <h1 className="page-title">Page not found</h1>
                  <div className="muted">
                    Go back to <a className="link" href="/packages">home</a>
                  </div>
                </div>
              </div>
            }
          />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
