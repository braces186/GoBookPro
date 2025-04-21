<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <h2>註冊</h2>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        @submit.prevent="handleSubmit"
      >
        <el-form-item label="用戶名" prop="username">
          <el-input v-model="form.username" placeholder="請輸入用戶名" />
        </el-form-item>
        
        <el-form-item label="電子郵件" prop="email">
          <el-input v-model="form.email" placeholder="請輸入電子郵件" />
        </el-form-item>
        
        <el-form-item label="密碼" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="請輸入密碼"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="確認密碼" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="請再次輸入密碼"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading">
            註冊
          </el-button>
          <el-button type="text" @click="$router.push('/login')">
            已有帳號？立即登入
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validatePass = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('請再次輸入密碼'))
  } else if (value !== form.password) {
    callback(new Error('兩次輸入的密碼不一致'))
  } else {
    callback()
  }
}

const rules = reactive<FormRules>({
  username: [
    { required: true, message: '請輸入用戶名', trigger: 'blur' },
    { min: 3, max: 20, message: '用戶名長度應為 3 到 20 個字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '請輸入電子郵件', trigger: 'blur' },
    { type: 'email', message: '請輸入正確的電子郵件格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '請輸入密碼', trigger: 'blur' },
    { min: 6, max: 20, message: '密碼長度應為 6 到 20 個字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '請再次輸入密碼', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ]
})

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const { confirmPassword, ...registerData } = form
    const success = await userStore.register(registerData)
    
    if (success) {
      router.push('/')
    }
  } catch (error) {
    console.error('Registration failed:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.register-card {
  width: 100%;
  max-width: 400px;
  margin: 20px;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
}

.el-form {
  margin-top: 20px;
}

.el-button {
  width: 100%;
  margin-bottom: 10px;
}
</style> 