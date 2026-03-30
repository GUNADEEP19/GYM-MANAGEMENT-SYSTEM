import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import './App.css';

import { AuthProvider, useAuth } from './app/AuthContext';
import AppLayout from './layout/AppLayout';
import ProtectedRoute from './components/ProtectedRoute';

import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import PackagesPage from './pages/PackagesPage';
import WorkoutsPage from './pages/WorkoutsPage';
import ProgressPage from './pages/ProgressPage';
import AttendancePage from './pages/AttendancePage';
import RecommendationsPage from './pages/RecommendationsPage';
import AdminReportPage from './pages/AdminReportPage';

function PublicOnly({ children }) {
  const { isAuthenticated } = useAuth();
  if (isAuthenticated) return <Navigate to="/packages" replace />;
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
            path="/register"
            element={
              <PublicOnly>
                <RegisterPage />
              </PublicOnly>
            }
          />

          <Route element={<ProtectedRoute><AppLayout /></ProtectedRoute>}>
            <Route index element={<Navigate to="/packages" replace />} />
            <Route path="packages" element={<PackagesPage />} />
            <Route path="workouts" element={<WorkoutsPage />} />
            <Route path="progress" element={<ProgressPage />} />
            <Route path="attendance" element={<AttendancePage />} />
            <Route path="recommendation" element={<RecommendationsPage />} />

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
                    Go back to <a className="link" href="/packages">Packages</a>
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
