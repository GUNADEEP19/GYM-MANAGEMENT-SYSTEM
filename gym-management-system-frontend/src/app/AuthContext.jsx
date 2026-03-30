/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { clearAuth, getUser, saveAuth } from './tokenStorage';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => getUser());

  useEffect(() => {
    const AUTH_EVENT = 'gymAuthUpdated';
    // Keep state in sync if another tab changes localStorage.
    const onStorage = () => setAuth(getUser());
    window.addEventListener('storage', onStorage);
    window.addEventListener(AUTH_EVENT, onStorage);
    return () => {
      window.removeEventListener('storage', onStorage);
      window.removeEventListener(AUTH_EVENT, onStorage);
    };
  }, []);

  const value = useMemo(() => {
    return {
      auth,
      isAuthenticated: !!auth?.token,
      role: auth?.role ?? null,
      userId: auth?.userId ?? null,
      token: auth?.token ?? null,
      login: (userResponse) => {
        // UserResponse: { userId, name, email, phone, role, token }
        saveAuth(userResponse);
        setAuth(userResponse);
      },
      logout: () => {
        clearAuth();
        setAuth(null);
      },
    };
  }, [auth]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}

