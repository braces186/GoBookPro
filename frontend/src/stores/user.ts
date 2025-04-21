import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '@/types/user'
import { login as loginApi, register as registerApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const currentUser = ref<User | null>(null)
  const token = ref<string | null>(null)

  async function login(username: string, password: string) {
    try {
      const response = await loginApi(username, password)
      token.value = response.token
      currentUser.value = response.user
      localStorage.setItem('token', response.token)
      return true
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  async function register(userData: { username: string; password: string; email: string }) {
    try {
      const response = await registerApi(userData)
      token.value = response.token
      currentUser.value = response.user
      localStorage.setItem('token', response.token)
      return true
    } catch (error) {
      console.error('Registration failed:', error)
      return false
    }
  }

  function logout() {
    currentUser.value = null
    token.value = null
    localStorage.removeItem('token')
  }

  // 初始化時檢查本地存儲的 token
  const initializeFromStorage = () => {
    const storedToken = localStorage.getItem('token')
    if (storedToken) {
      token.value = storedToken
      // TODO: 使用 token 獲取用戶信息
    }
  }

  return {
    currentUser,
    token,
    login,
    register,
    logout,
    initializeFromStorage
  }
}) 