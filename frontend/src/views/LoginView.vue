<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>登入</h2>
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
        
        <el-form-item label="密碼" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="請輸入密碼"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading">
            登入
          </el-button>
          <el-button type="text" @click="$router.push('/register')">
            還沒有帳號？立即註冊
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
  password: ''
})

const rules = reactive<FormRules>({
  username: [
    { required: true, message: '請輸入用戶名', trigger: 'blur' },
    { min: 3, max: 20, message: '用戶名長度應為 3 到 20 個字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '請輸入密碼', trigger: 'blur' },
    { min: 6, max: 20, message: '密碼長度應為 6 到 20 個字符', trigger: 'blur' }
  ]
})

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    const success = await userStore.login(form.username, form.password)
    if (success) {
      router.push('/')
    }
  } catch (error) {
    console.error('Login failed:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
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