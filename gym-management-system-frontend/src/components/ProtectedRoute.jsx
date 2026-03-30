import { Navigate } from 'react-router-dom';
import { useAuth } from '../app/AuthContext';

export default function ProtectedRoute({ children, requiredRoles }) {
  const { isAuthenticated, role } = useAuth();

  if (!isAuthenticated) return <Navigate to="/login" replace />;

  if (requiredRoles && Array.isArray(requiredRoles) && requiredRoles.length > 0) {
    const allowed = requiredRoles.includes(role);
    if (!allowed) return <Navigate to="/" replace />;
  }

  return children;
}

