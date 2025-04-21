<template>
  <div class="venue-manage-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h1>場地管理</h1>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>場地列表</span>
              <el-button type="primary" @click="handleAddVenue">
                新增場地
              </el-button>
            </div>
          </template>
          
          <el-form :model="filterForm" inline>
            <el-form-item label="場地名稱">
              <el-input v-model="filterForm.name" placeholder="搜尋場地名稱" />
            </el-form-item>
            <el-form-item label="狀態">
              <el-select v-model="filterForm.status" placeholder="選擇狀態" clearable>
                <el-option label="可用" value="AVAILABLE" />
                <el-option label="維護中" value="MAINTENANCE" />
                <el-option label="已停用" value="DISABLED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜尋</el-button>
              <el-button @click="resetFilter">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table
            v-loading="loading"
            :data="venues"
            style="width: 100%"
          >
            <el-table-column prop="name" label="場地名稱" />
            <el-table-column prop="location" label="位置" />
            <el-table-column prop="capacity" label="容納人數" />
            <el-table-column prop="price" label="價格">
              <template #default="{ row }">
                {{ row.price }} 元/小時
              </template>
            </el-table-column>
            <el-table-column prop="status" label="狀態">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="250">
              <template #default="{ row }">
                <el-button
                  type="primary"
                  link
                  @click="handleEditVenue(row)"
                >
                  編輯
                </el-button>
                <el-button
                  type="success"
                  link
                  @click="handleViewBookings(row.id)"
                >
                  預訂記錄
                </el-button>
                <el-button
                  type="danger"
                  link
                  @click="handleDeleteVenue(row.id)"
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
      v-model="venueDialogVisible"
      :title="isEdit ? '編輯場地' : '新增場地'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="venueForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="場地名稱" prop="name">
          <el-input v-model="venueForm.name" />
        </el-form-item>
        
        <el-form-item label="位置" prop="location">
          <el-input v-model="venueForm.location" />
        </el-form-item>
        
        <el-form-item label="容納人數" prop="capacity">
          <el-input-number v-model="venueForm.capacity" :min="1" />
        </el-form-item>
        
        <el-form-item label="價格" prop="price">
          <el-input-number v-model="venueForm.price" :min="0" :precision="2" />
          <span class="unit">元/小時</span>
        </el-form-item>
        
        <el-form-item label="狀態" prop="status">
          <el-select v-model="venueForm.status" placeholder="選擇狀態">
            <el-option label="可用" value="AVAILABLE" />
            <el-option label="維護中" value="MAINTENANCE" />
            <el-option label="已停用" value="DISABLED" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="venueForm.description"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
        
        <el-form-item label="設施" prop="facilities">
          <el-input
            v-model="venueForm.facilities"
            type="textarea"
            :rows="2"
            placeholder="請用逗號分隔多個設施"
          />
        </el-form-item>
        
        <el-form-item label="規則" prop="rules">
          <el-input
            v-model="venueForm.rules"
            type="textarea"
            :rows="2"
            placeholder="請用逗號分隔多個規則"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="venueDialogVisible = false">取消</el-button>
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
import type { FormInstance, FormRules } from 'element-plus'
import type { Venue, VenueListResponse, VenueFilter } from '@/types/venue'
import { getVenueList, createVenue, updateVenue, deleteVenue, getVenueBookings } from '@/api/venue'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const venueDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const venues = ref<VenueListResponse['venues']>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filterForm = reactive<VenueFilter>({
  name: '',
  status: undefined,
  page: 1,
  pageSize: 10
})

const venueForm = reactive<Partial<Venue>>({
  name: '',
  location: '',
  capacity: 0,
  price: 0,
  status: 'AVAILABLE',
  description: '',
  facilities: '',
  rules: ''
})

const rules = reactive<FormRules>({
  name: [
    { required: true, message: '請輸入場地名稱', trigger: 'blur' },
    { min: 2, max: 50, message: '長度在 2 到 50 個字符', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '請輸入位置', trigger: 'blur' }
  ],
  capacity: [
    { required: true, message: '請輸入容納人數', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '請輸入價格', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '請選擇狀態', trigger: 'change' }
  ]
})

const getStatusType = (status: string) => {
  const types = {
    AVAILABLE: 'success',
    MAINTENANCE: 'warning',
    DISABLED: 'danger'
  }
  return types[status as keyof typeof types]
}

const getStatusText = (status: string) => {
  const texts = {
    AVAILABLE: '可用',
    MAINTENANCE: '維護中',
    DISABLED: '已停用'
  }
  return texts[status as keyof typeof texts]
}

const handleSearch = () => {
  currentPage.value = 1
  fetchVenues()
}

const resetFilter = () => {
  filterForm.name = ''
  filterForm.status = undefined
  handleSearch()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchVenues()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchVenues()
}

const handleAddVenue = () => {
  isEdit.value = false
  venueForm.name = ''
  venueForm.location = ''
  venueForm.capacity = 0
  venueForm.price = 0
  venueForm.status = 'AVAILABLE'
  venueForm.description = ''
  venueForm.facilities = ''
  venueForm.rules = ''
  venueDialogVisible.value = true
}

const handleEditVenue = (venue: Venue) => {
  isEdit.value = true
  Object.assign(venueForm, venue)
  venueDialogVisible.value = true
}

const handleViewBookings = async (id: number) => {
  try {
    loading.value = true
    const response = await getVenueBookings(id)
    // TODO: 顯示預訂列表
    console.log('Venue bookings:', response)
  } catch (error) {
    console.error('Failed to fetch venue bookings:', error)
    ElMessage.error('獲取場地預訂記錄失敗')
  } finally {
    loading.value = false
  }
}

const handleDeleteVenue = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '確定要刪除此場地嗎？',
      '刪除場地',
      {
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteVenue(id)
    ElMessage.success('場地已刪除')
    fetchVenues()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to delete venue:', error)
      ElMessage.error('刪除場地失敗')
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    if (isEdit.value) {
      await updateVenue(venueForm.id!, venueForm)
      ElMessage.success('場地更新成功')
    } else {
      // TODO: 從用戶信息中獲取 merchantId
      const merchantId = 1 // 臨時使用固定值
      await createVenue(merchantId, venueForm)
      ElMessage.success('場地新增成功')
    }
    
    venueDialogVisible.value = false
    fetchVenues()
  } catch (error) {
    console.error('Failed to submit venue:', error)
    ElMessage.error(isEdit.value ? '更新場地失敗' : '新增場地失敗')
  } finally {
    submitting.value = false
  }
}

const fetchVenues = async () => {
  try {
    loading.value = true
    const params = {
      ...filterForm,
      page: currentPage.value,
      pageSize: pageSize.value
    }
    const response = await getVenueList(params)
    venues.value = response.venues
    total.value = response.total
  } catch (error) {
    console.error('Failed to fetch venues:', error)
    ElMessage.error('獲取場地列表失敗')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchVenues()
})
</script>

<style scoped>
.venue-manage-container {
  padding: 20px;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
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

.unit {
  margin-left: 10px;
  color: #666;
}
</style> 