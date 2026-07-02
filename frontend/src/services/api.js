import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  if (import.meta.env.DEV) {
    console.debug('Request:', config.method?.toUpperCase(), config.url);
  }
  const token = localStorage.getItem('token');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => {
    if (import.meta.env.DEV) {
      console.debug('Response:', response.status, response.config.url);
    }
    return response;
  },
  (error) => {
    if (import.meta.env.DEV) {
      console.error('Response error:', error.response?.status, error.response?.data, error.config?.url);
    }
    return Promise.reject(error);
  }
);

export default api;
