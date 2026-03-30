const STORAGE_KEY = 'gymAuth';
const AUTH_EVENT = 'gymAuthUpdated';

function notifyAuthChanged() {
  window.dispatchEvent(new Event(AUTH_EVENT));
}

export function loadAuth() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

export function saveAuth(auth) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(auth));
  notifyAuthChanged();
}

export function clearAuth() {
  localStorage.removeItem(STORAGE_KEY);
  notifyAuthChanged();
}

export function getToken() {
  return loadAuth()?.token ?? null;
}

export function getUser() {
  return loadAuth();
}

