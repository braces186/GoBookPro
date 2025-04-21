import axios from 'axios';
import { ElMessage } from 'element-plus';

// 創建 axios 實例
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL, 
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true,
});

// 請求攔截器
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 響應攔截器
api.interceptors.response.use(
    (response) => {
        return response.data;
    },
    (error) => {
        if (error.response) {
            switch (error.response.status) {
                case 401:
                    // 未授權，清除 token 並跳轉到登錄頁
                    localStorage.removeItem('token');
                    window.location.href = '/login';
                    break;
                case 403:
                    // 權限不足
                    ElMessage.error('權限不足');
                    break;
                case 404:
                    // 資源不存在
                    ElMessage.error('資源不存在');
                    break;
                case 500:
                    // 服務器錯誤
                    ElMessage.error('服務器錯誤');
                    break;
                default:
                    ElMessage.error(error.response.data.message || '請求失敗');
            }
        } else {
            ElMessage.error('網絡錯誤，請檢查網絡連接');
        }
        return Promise.reject(error);
    }
);

export default api; 