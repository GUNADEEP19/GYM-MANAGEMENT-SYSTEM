import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../app/AuthContext';

const linkClass = ({ isActive }) =>
  `nav-link ${isActive ? 'nav-link-active' : ''}`;

export default function AppLayout() {
  const { logout, auth } = useAuth();
  const navigate = useNavigate();
  const role = auth?.role;
  const roleLabel = role ? `${role.slice(0, 1)}${role.slice(1).toLowerCase()}` : null;

  const onLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <div className="app-shell">
      <aside className="app-sidebar">
        <div className="app-brand">
          <div className="app-brand-title">Gym Management</div>
          {roleLabel ? <div className="app-brand-sub">Role: {roleLabel}</div> : null}
        </div>

        <nav className="app-nav">
          {role === 'ADMIN' ? (
            // Admin menu
            <>
              <NavLink to="/admin/create-user" className={linkClass}>
                Create User
              </NavLink>
              <NavLink to="/admin/report" className={linkClass}>
                Admin Report
              </NavLink>
            </>
          ) : role === 'TRAINER' ? (
            // Trainer menu
            <>
              <NavLink to="/trainer/members" className={linkClass}>
                My Members
              </NavLink>
              <NavLink to="/trainer/workouts" className={linkClass}>
                My Workouts
              </NavLink>
            </>
          ) : (
            // Member menu
            <>
              <NavLink to="/packages" className={linkClass}>
                Packages
              </NavLink>
              <NavLink to="/workouts" className={linkClass}>
                My Workouts
              </NavLink>
              <NavLink to="/progress" className={linkClass}>
                Progress
              </NavLink>
              <NavLink to="/attendance" className={linkClass}>
                Attendance
              </NavLink>
              <NavLink to="/recommendation" className={linkClass}>
                Recommendation
              </NavLink>
            </>
          )}
        </nav>

        <div className="app-sidebar-footer">
          <button className="btn btn-secondary" onClick={onLogout}>
            Logout
          </button>
        </div>
      </aside>

      <main className="app-content">
        <Outlet />
      </main>
    </div>
  );
}

