import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../app/AuthContext';

const linkClass = ({ isActive }) =>
  `nav-link ${isActive ? 'nav-link-active' : ''}`;

export default function AppLayout() {
  const { logout, auth } = useAuth();
  const navigate = useNavigate();
  const role = auth?.role;

  const onLogout = () => {
    logout();
    navigate('/login', { replace: true });
  };

  return (
    <div className="app-shell">
      <aside className="app-sidebar">
        <div className="app-brand">
          <div className="app-brand-title">Gym Management</div>
          {role ? <div className="app-brand-sub">Role: {role}</div> : null}
        </div>

        <nav className="app-nav">
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

          {role === 'ADMIN' ? (
            <>
              <NavLink to="/admin/create-user" className={linkClass}>
                Create User
              </NavLink>
              <NavLink to="/admin/report" className={linkClass}>
                Admin Report
              </NavLink>
            </>
          ) : null}
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

