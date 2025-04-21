import axios from 'axios'
import { ElMessage } from 'element-plus'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  withCredentials: true,
})

service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    if (data.code === 0) {
      return data.data
    }
    ElMessage.error(data.message || '請求失敗')
    return Promise.reject(new Error(data.message || '請求失敗'))
  },
  (error) => {
    console.error('Response error:', error)
    ElMessage.error(error.message || '請求失敗')
    return Promise.reject(error)
  }
)

export default service 