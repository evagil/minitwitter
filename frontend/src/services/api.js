import axios from 'axios';
import { CONFIG } from '@/constants/config';

const API_BASE_URL = import.meta.env.PROD 
    ? CONFIG.API.BASE_URL_PROD
    : CONFIG.API.BASE_URL_DEV;

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: CONFIG.API.TIMEOUT,
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.code === 'ECONNABORTED') {
            console.error('Timeout:', error.message);
        } else if (error.response) {
            console.error('Error del servidor:', error.response.status, error.response.data);
        } else if (error.request) {
            console.error('Error de conexión:', error.message);
        } else {
            console.error('Error:', error.message);
        }
        return Promise.reject(error);
    }
);

export default api;

