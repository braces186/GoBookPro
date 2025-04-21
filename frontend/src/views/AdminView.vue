<template>
  <div class="admin-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h1>管理員控制台</h1>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>用戶總數</span>
            </div>
          </template>
          <div class="stat-content">
            <el-icon class="stat-icon"><User /></el-icon>
            <span class="stat-number">{{ dashboard?.totalUsers || 0 }}</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>場地總數</span>
            </div>
          </template>
          <div class="stat-content">
            <el-icon class="stat-icon"><Location /></el-icon>
            <span class="stat-number">{{ dashboard?.totalVenues || 0 }}</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>預訂總數</span>
            </div>
          </template>
          <div class="stat-content">
            <el-icon class="stat-icon"><Calendar /></el-icon>
            <span class="stat-number">{{ dashboard?.totalBookings || 0 }}</span>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>總收入</span>
            </div>
          </template>
          <div class="stat-content">
            <el-icon class="stat-icon"><Money /></el-icon>
            <span class="stat-number">{{ dashboard?.totalRevenue || 0 }} 元</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="data-section">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近註冊用戶</span>
              <el-button type="primary" link @click="goToUsers">
                查看全部
              </el-button>
            </div>
          </template>
          
          <el-table
            v-loading="loading"
            :data="dashboard?.recentUsers"
            style="width: 100%"
          >
            <el-table-column prop="username" label="用戶名" />
            <el-table-column prop="email" label="電子郵件" />
            <el-table-column prop="role" label="角色">
              <template #default="{ row }">
                <el-tag :type="getRoleType(row.role)">
                  {{ getRoleText(row.role) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="註冊時間" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
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
            :data="dashboard?.recentBookings"
            style="width: 100%"
          >
            <el-table-column prop="venueName" label="場地" />
            <el-table-column prop="username" label="用戶" />
            <el-table-column prop="date" label="預訂日期" />
            <el-table-column prop="totalPrice" label="金額">
              <template #default="{ row }">
                {{ row.totalPrice }} 元
              </template>
            </el-table-column>
            <el-table-column prop="status" label="狀態">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="data-section">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>用戶管理</span>
              <el-button type="primary" @click="handleAddUser">
                新增用戶
              </el-button>
            </div>
          </template>
          
          <el-form :model="filterForm" inline>
            <el-form-item label="用戶名">
              <el-input v-model="filterForm.username" placeholder="搜尋用戶名" />
            </el-form-item>
            <el-form-item label="電子郵件">
              <el-input v-model="filterForm.email" placeholder="搜尋電子郵件" />
            </el-form-item>
            <el-form-item label="角色">
              <el-select v-model="filterForm.role" placeholder="選擇角色" clearable>
                <el-option label="普通用戶" value="USER" />
                <el-option label="商家" value="MERCHANT" />
                <el-option label="管理員" value="ADMIN" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜尋</el-button>
              <el-button @click="resetFilter">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table
            v-loading="loading"
            :data="users"
            style="width: 100%"
          >
            <el-table-column prop="username" label="用戶名" />
            <el-table-column prop="email" label="電子郵件" />
            <el-table-column prop="phone" label="手機號碼" />
            <el-table-column prop="role" label="角色">
              <template #default="{ row }">
                <el-tag :type="getRoleType(row.role)">
                  {{ getRoleText(row.role) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="註冊時間" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button
                  type="primary"
                  link
                  @click="handleEditUser(row)"
                >
                  編輯
                </el-button>
                <el-button
                  type="danger"
                  link
                  @click="handleDeleteUser(row.id)"
                >
                  刪除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-container">
            <el-pagination
              :current-page="currentPage"
              :page-size="pageSize"
              :total="total"
              :page-sizes="[10, 20, 30, 50]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      v-model="userDialogVisible"
      :title="isEdit ? '編輯用戶' : '新增用戶'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="userForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="用戶名" prop="username">
          <el-input v-model="userForm.username" />
        </el-form-item>
        
        <el-form-item label="電子郵件" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        
        <el-form-item label="手機號碼" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="選擇角色">
            <el-option label="普通用戶" value="USER" />
            <el-option label="商家" value="MERCHANT" />
            <el-option label="管理員" value="ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userDialogVisible = false">取消</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Location, Calendar, Money } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { AdminDashboard, UserListResponse, UserFilter, UpdateUserRequest } from '@/types/admin'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const userDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const dashboard = ref<AdminDashboard | null>(null)
const users = ref<UserListResponse['users']>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filterForm = reactive<UserFilter>({
  username: '',
  email: '',
  role: undefined
})

const userForm = reactive<UpdateUserRequest>({
  username: '',
  email: '',
  phone: '',
  role: 'USER'
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
  role: [
    { required: true, message: '請選擇角色', trigger: 'change' }
  ]
})

const getRoleType = (role: string) => {
  const types = {
    USER: '',
    MERCHANT: 'success',
    ADMIN: 'danger'
  }
  return types[role as keyof typeof types]
}

const getRoleText = (role: string) => {
  const texts = {
    USER: '普通用戶',
    MERCHANT: '商家',
    ADMIN: '管理員'
  }
  return texts[role as keyof typeof texts]
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

const handleSearch = () => {
  currentPage.value = 1
  fetchUsers()
}

const resetFilter = () => {
  filterForm.username = ''
  filterForm.email = ''
  filterForm.role = undefined
  handleSearch()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchUsers()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchUsers()
}

const goToUsers = () => {
  router.push('/admin/users')
}

const goToBookings = () => {
  router.push('/admin/bookings')
}

const handleAddUser = () => {
  isEdit.value = false
  userForm.username = ''
  userForm.email = ''
  userForm.phone = ''
  userForm.role = 'USER'
  userDialogVisible.value = true
}

const handleEditUser = (user: UserListResponse['users'][0]) => {
  isEdit.value = true
  userForm.username = user.username
  userForm.email = user.email
  userForm.phone = user.phone
  userForm.role = user.role
  userDialogVisible.value = true
}

const handleDeleteUser = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '確定要刪除此用戶嗎？',
      '刪除用戶',
      {
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 實現刪除用戶的 API 調用
    ElMessage.success('用戶已刪除')
    fetchUsers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete user:', error)
      ElMessage.error('刪除用戶失敗')
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    // TODO: 實現新增/更新用戶的 API 調用
    ElMessage.success(isEdit.value ? '用戶更新成功' : '用戶新增成功')
    userDialogVisible.value = false
    fetchUsers()
  } catch (error) {
    console.error('Failed to submit user:', error)
    ElMessage.error(isEdit.value ? '更新用戶失敗' : '新增用戶失敗')
  } finally {
    submitting.value = false
  }
}

const fetchDashboard = async () => {
  try {
    loading.value = true
    // TODO: 實現獲取管理員儀表板的 API 調用
    // 這裡先使用模擬數據
    dashboard.value = {
      totalUsers: 100,
      totalVenues: 20,
      totalBookings: 500,
      totalRevenue: 1000000,
      recentUsers: [
        {
          id: 1,
          username: 'user1',
          email: 'user1@example.com',
          role: 'USER',
          createdAt: '2024-03-25 10:00:00'
        },
        {
          id: 2,
          username: 'merchant1',
          email: 'merchant1@example.com',
          role: 'MERCHANT',
          createdAt: '2024-03-26 15:30:00'
        }
      ],
      recentBookings: [
        {
          id: 1,
          venueName: '會議室 A',
          username: 'user1',
          date: '2024-04-01',
          totalPrice: 6000,
          status: 'CONFIRMED'
        },
        {
          id: 2,
          venueName: '展演廳 B',
          username: 'user2',
          date: '2024-04-15',
          totalPrice: 15000,
          status: 'PENDING'
        }
      ]
    }
  } catch (error) {
    console.error('Failed to fetch dashboard:', error)
    ElMessage.error('獲取儀表板數據失敗')
  } finally {
    loading.value = false
  }
}

const fetchUsers = async () => {
  try {
    loading.value = true
    // TODO: 實現獲取用戶列表的 API 調用
    // 這裡先使用模擬數據
    users.value = [
      {
        id: 1,
        username: 'user1',
        email: 'user1@example.com',
        phone: '0912345678',
        role: 'USER',
        createdAt: '2024-01-01 00:00:00',
        updatedAt: '2024-01-01 00:00:00'
      },
      {
        id: 2,
        username: 'merchant1',
        email: 'merchant1@example.com',
        phone: '0923456789',
        role: 'MERCHANT',
        createdAt: '2024-01-02 00:00:00',
        updatedAt: '2024-01-02 00:00:00'
      }
    ]
    total.value = users.value.length
  } catch (error) {
    console.error('Failed to fetch users:', error)
    ElMessage.error('獲取用戶列表失敗')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchDashboard()
  fetchUsers()
})
</script>

<style scoped>
.admin-container {
  padding: 20px;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.stat-icon {
  font-size: 24px;
  margin-right: 10px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
}

.data-section {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 