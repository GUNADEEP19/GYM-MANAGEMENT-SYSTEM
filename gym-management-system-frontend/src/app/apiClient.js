import axios from 'axios';
import { clearAuth, getToken } from './tokenStorage';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error?.response?.status === 401) {
      clearAuth();
    }
    return Promise.reject(error);
  }
);

export function unwrapApi(body) {
  // Backend returns: { success: boolean, message: string, data: T, timestamp: ... }
  if (body && typeof body.success === 'boolean') {
    if (!body.success) throw new Error(body.message || 'Request failed');
    return body.data;
  }
  // Fallback (in case backend changes shape)
  return body;
}

