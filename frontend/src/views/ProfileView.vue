<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h1>個人中心</h1>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="profile-card">
          <template #header>
            <div class="card-header">
              <span>個人資料</span>
              <el-button type="primary" link @click="handleEditProfile">
                編輯資料
              </el-button>
            </div>
          </template>
          
          <div class="profile-info">
            <el-avatar :size="100" :src="userStore.user?.avatar" />
            <h2>{{ userStore.user?.username }}</h2>
            <p class="email">{{ userStore.user?.email }}</p>
            <p class="phone" v-if="userStore.user?.phone">
              <el-icon><Phone /></el-icon>
              {{ userStore.user.phone }}
            </p>
            <p class="role">
              <el-tag :type="getRoleType(userStore.user?.role)">
                {{ getRoleText(userStore.user?.role) }}
              </el-tag>
            </p>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card class="stats-card">
          <template #header>
            <div class="card-header">
              <span>預訂統計</span>
            </div>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="stat-item">
                <h3>總預訂次數</h3>
                <p class="number">{{ profile?.totalBookings || 0 }}</p>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-item">
                <h3>總消費金額</h3>
                <p class="number">{{ profile?.totalSpent || 0 }} 元</p>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="recent-bookings-card">
          <template #header>
            <div class="card-header">
              <span>最近預訂</span>
              <el-button type="primary" link @click="goToBookings">
                查看全部
              </el-button>
            </div>
          </template>
          
          <el-table
            v-loading="loading"
            :data="profile?.recentBookings"
            style="width: 100%"
          >
            <el-table-column prop="venueName" label="場地" />
            <el-table-column prop="date" label="預訂日期" />
            <el-table-column prop="status" label="狀態">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button
                  type="primary"
                  link
                  @click="goToBookingDetail(row.id)"
                >
                  查看詳情
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      v-model="editDialogVisible"
      title="編輯個人資料"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="editForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="用戶名" prop="username">
          <el-input v-model="editForm.username" />
        </el-form-item>
        
        <el-form-item label="電子郵件" prop="email">
          <el-input v-model="editForm.email" />
        </el-form-item>
        
        <el-form-item label="手機號碼" prop="phone">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        
        <el-form-item label="當前密碼" prop="currentPassword">
          <el-input
            v-model="editForm.currentPassword"
            type="password"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="新密碼" prop="newPassword">
          <el-input
            v-model="editForm.newPassword"
            type="password"
            show-password
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            確定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Phone } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import type { UserProfile, UpdateProfileRequest } from '@/types/user'
import type { FormInstance, FormRules } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const submitting = ref(false)
const editDialogVisible = ref(false)
const formRef = ref<FormInstance>()
const profile = ref<UserProfile | null>(null)

const editForm = reactive<UpdateProfileRequest>({
  username: userStore.user?.username,
  email: userStore.user?.email,
  phone: userStore.user?.phone,
  currentPassword: '',
  newPassword: ''
})

const rules = reactive<FormRules>({
  username: [
    { required: true, message: '請輸入用戶名', trigger: 'blur' },
    { min: 3, max: 20, message: '長度在 3 到 20 個字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '請輸入電子郵件', trigger: 'blur' },
    { type: 'email', message: '請輸入正確的電子郵件格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '請輸入正確的手機號碼格式', trigger: 'blur' }
  ],
  currentPassword: [
    { min: 6, max: 20, message: '長度在 6 到 20 個字符', trigger: 'blur' }
  ],
  newPassword: [
    { min: 6, max: 20, message: '長度在 6 到 20 個字符', trigger: 'blur' }
  ]
})

const getRoleType = (role?: string) => {
  const types = {
    USER: '',
    MERCHANT: 'success',
    ADMIN: 'danger'
  }
  return role ? types[role as keyof typeof types] : ''
}

const getRoleText = (role?: string) => {
  const texts = {
    USER: '普通用戶',
    MERCHANT: '商家',
    ADMIN: '管理員'
  }
  return role ? texts[role as keyof typeof texts] : ''
}

const getStatusType = (status: string) => {
  const types = {
    PENDING: 'warning',
    CONFIRMED: 'success',
    CANCELLED: 'info',
    COMPLETED: ''
  }
  return types[status as keyof typeof types]
}

const getStatusText = (status: string) => {
  const texts = {
    PENDING: '待確認',
    CONFIRMED: '已確認',
    CANCELLED: '已取消',
    COMPLETED: '已完成'
  }
  return texts[status as keyof typeof texts]
}

const handleEditProfile = () => {
  editForm.username = userStore.user?.username
  editForm.email = userStore.user?.email
  editForm.phone = userStore.user?.phone
  editForm.currentPassword = ''
  editForm.newPassword = ''
  editDialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    // TODO: 實現更新個人資料的 API 調用
    ElMessage.success('個人資料更新成功')
    editDialogVisible.value = false
    fetchProfile()
  } catch (error) {
    console.error('Failed to update profile:', error)
    ElMessage.error('更新個人資料失敗')
  } finally {
    submitting.value = false
  }
}

const goToBookings = () => {
  router.push('/bookings')
}

const goToBookingDetail = (id: number) => {
  router.push(`/bookings/${id}`)
}

const fetchProfile = async () => {
  try {
    loading.value = true
    // TODO: 實現獲取個人資料的 API 調用
    // 這裡先使用模擬數據
    profile.value = {
      user: {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        phone: '0912345678',
        role: 'USER',
        createdAt: '2024-01-01 00:00:00',
        updatedAt: '2024-01-01 00:00:00'
      },
      totalBookings: 10,
      totalSpent: 50000,
      recentBookings: [
        {
          id: 1,
          venueName: '會議室 A',
          date: '2024-04-01',
          status: 'CONFIRMED'
        },
        {
          id: 2,
          venueName: '展演廳 B',
          date: '2024-04-15',
          status: 'PENDING'
        }
      ]
    }
  } catch (error) {
    console.error('Failed to fetch profile:', error)
    ElMessage.error('獲取個人資料失敗')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

.profile-card {
  margin-bottom: 20px;
}

.profile-info {
  text-align: center;
}

.profile-info h2 {
  margin: 20px 0 10px;
}

.profile-info .email,
.profile-info .phone {
  color: #666;
  margin: 5px 0;
}

.profile-info .role {
  margin-top: 10px;
}

.stats-card {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-item h3 {
  margin: 0 0 10px;
  color: #666;
}

.stat-item .number {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.recent-bookings-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 