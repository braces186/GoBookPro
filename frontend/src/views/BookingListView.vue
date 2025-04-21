<template>
  <div class="booking-list-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h1>我的預訂</h1>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>篩選條件</span>
            </div>
          </template>
          
          <el-form :model="filterForm" label-position="top">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="預訂狀態">
                  <el-select v-model="filterForm.status" placeholder="選擇狀態" clearable>
                    <el-option label="待確認" value="PENDING" />
                    <el-option label="已確認" value="CONFIRMED" />
                    <el-option label="已取消" value="CANCELLED" />
                    <el-option label="已完成" value="COMPLETED" />
                  </el-select>
                </el-form-item>
              </el-col>
              
              <el-col :span="8">
                <el-form-item label="預訂日期">
                  <el-date-picker
                    v-model="filterForm.date"
                    type="date"
                    placeholder="選擇日期"
                    clearable
                  />
                </el-form-item>
              </el-col>
              
              <el-col :span="8">
                <el-form-item>
                  <el-button type="primary" @click="handleSearch">搜尋</el-button>
                  <el-button @click="resetFilter">重置</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="booking-list">
      <el-col :span="24">
        <el-table
          v-loading="loading"
          :data="bookings"
          style="width: 100%"
        >
          <el-table-column prop="venueName" label="場地" />
          <el-table-column label="預訂時間">
            <template #default="{ row }">
              {{ row.date }} {{ row.startTime }} - {{ row.endTime }}
            </template>
          </el-table-column>
          <el-table-column prop="attendees" label="預訂人數" />
          <el-table-column prop="totalPrice" label="總金額">
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
          <el-table-column prop="createdAt" label="預訂時間" />
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button
                type="primary"
                link
                @click="goToDetail(row.id)"
              >
                查看詳情
              </el-button>
              <el-button
                v-if="row.status === 'PENDING' || row.status === 'CONFIRMED'"
                type="danger"
                link
                @click="handleCancel(row.id)"
              >
                取消預訂
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 20, 30, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Booking, BookingFilter } from '@/types/booking'

const router = useRouter()
const loading = ref(false)
const bookings = ref<Booking[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filterForm = reactive<BookingFilter>({
  status: undefined,
  date: undefined
})

const getStatusType = (status: Booking['status']) => {
  const types = {
    PENDING: 'warning',
    CONFIRMED: 'success',
    CANCELLED: 'info',
    COMPLETED: ''
  }
  return types[status]
}

const getStatusText = (status: Booking['status']) => {
  const texts = {
    PENDING: '待確認',
    CONFIRMED: '已確認',
    CANCELLED: '已取消',
    COMPLETED: '已完成'
  }
  return texts[status]
}

const handleSearch = () => {
  currentPage.value = 1
  fetchBookings()
}

const resetFilter = () => {
  filterForm.status = undefined
  filterForm.date = undefined
  handleSearch()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchBookings()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchBookings()
}

const goToDetail = (id: number) => {
  router.push(`/bookings/${id}`)
}

const handleCancel = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '確定要取消此預訂嗎？',
      '取消預訂',
      {
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 實現取消預訂的 API 調用
    ElMessage.success('預訂已取消')
    fetchBookings()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to cancel booking:', error)
      ElMessage.error('取消預訂失敗')
    }
  }
}

const fetchBookings = async () => {
  try {
    loading.value = true
    // TODO: 實現獲取預訂列表的 API 調用
    // 這裡先使用模擬數據
    bookings.value = [
      {
        id: 1,
        userId: 1,
        venueId: 1,
        venueName: '會議室 A',
        date: '2024-04-01',
        startTime: '09:00',
        endTime: '12:00',
        attendees: 20,
        totalPrice: 6000,
        status: 'CONFIRMED',
        createdAt: '2024-03-25 10:00:00',
        updatedAt: '2024-03-25 10:00:00'
      },
      {
        id: 2,
        userId: 1,
        venueId: 2,
        venueName: '展演廳 B',
        date: '2024-04-15',
        startTime: '14:00',
        endTime: '17:00',
        attendees: 100,
        totalPrice: 15000,
        status: 'PENDING',
        createdAt: '2024-03-26 15:30:00',
        updatedAt: '2024-03-26 15:30:00'
      }
    ]
    total.value = bookings.value.length
  } catch (error) {
    console.error('Failed to fetch bookings:', error)
    ElMessage.error('獲取預訂列表失敗')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchBookings()
})
</script>

<style scoped>
.booking-list-container {
  padding: 20px;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

.booking-list {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 