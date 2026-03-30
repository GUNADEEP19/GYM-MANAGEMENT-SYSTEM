import { Navigate } from 'react-router-dom';
import { useAuth } from '../app/AuthContext';

export default function ProtectedRoute({ children, requiredRoles }) {
  const { isAuthenticated, role } = useAuth();

  if (!isAuthenticated) return <Navigate to="/login" replace />;

  if (requiredRoles && Array.isArray(requiredRoles) && requiredRoles.length > 0) {
    const allowed = requiredRoles.includes(role);
    if (!allowed) {
      // Redirect to role-specific landing page
      if (role === 'ADMIN') return <Navigate to="/admin/create-user" replace />;
      if (role === 'TRAINER') return <Navigate to="/trainer/members" replace />;
      return <Navigate to="/packages" replace />;
    }
  }

  return children;
}

