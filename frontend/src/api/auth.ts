import axios from 'axios'
import type { LoginResponse, RegisterRequest, RegisterResponse } from '@/types/user'

const api = axios.create({
  baseURL: '/api'
})

export async function login(username: string, password: string): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>('/auth/login', {
    username,
    password
  })
  return response.data
}

export async function register(data: RegisterRequest): Promise<RegisterResponse> {
  const response = await api.post<RegisterResponse>('/auth/register', data)
  return response.data
}

// 添加請求攔截器設置 token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
) 