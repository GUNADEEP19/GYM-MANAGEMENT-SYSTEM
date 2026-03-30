import AppLayout from './AppLayout';
import ProtectedRoute from '../components/ProtectedRoute';

export default function RequireAuthLayout() {
  return (
    <ProtectedRoute>
      <AppLayout />
    </ProtectedRoute>
  );
}

