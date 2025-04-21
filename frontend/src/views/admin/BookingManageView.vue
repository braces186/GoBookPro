<template>
  <div class="booking-manage-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h1>預訂管理</h1>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>預訂列表</span>
            </div>
          </template>
          
          <el-form :model="filterForm" inline>
            <el-form-item label="場地">
              <el-select
                v-model="filterForm.venueId"
                placeholder="選擇場地"
                clearable
                filterable
              >
                <el-option
                  v-for="venue in venues"
                  :key="venue.id"
                  :label="venue.name"
                  :value="venue.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="狀態">
              <el-select v-model="filterForm.status" placeholder="選擇狀態" clearable>
                <el-option label="待確認" value="PENDING" />
                <el-option label="已確認" value="CONFIRMED" />
                <el-option label="已取消" value="CANCELLED" />
                <el-option label="已完成" value="COMPLETED" />
              </el-select>
            </el-form-item>
            <el-form-item label="預訂日期">
              <el-date-picker
                v-model="filterForm.date"
                type="date"
                placeholder="選擇日期"
                clearable
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜尋</el-button>
              <el-button @click="resetFilter">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table
            v-loading="loading"
            :data="bookings"
            style="width: 100%"
          >
            <el-table-column prop="venueName" label="場地" />
            <el-table-column prop="username" label="用戶" />
            <el-table-column prop="date" label="預訂日期" />
            <el-table-column prop="startTime" label="開始時間" />
            <el-table-column prop="endTime" label="結束時間" />
            <el-table-column prop="attendees" label="人數" />
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
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button
                  v-if="row.status === 'PENDING'"
                  type="success"
                  link
                  @click="handleConfirmBooking(row.id)"
                >
                  確認
                </el-button>
                <el-button
                  v-if="row.status === 'PENDING'"
                  type="danger"
                  link
                  @click="handleCancelBooking(row.id)"
                >
                  取消
                </el-button>
                <el-button
                  v-if="row.status === 'CONFIRMED'"
                  type="success"
                  link
                  @click="handleCompleteBooking(row.id)"
                >
                  完成
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { Booking, BookingListResponse, BookingFilter } from '@/types/booking'
import type { Venue } from '@/types/venue'
import { getBookingList, confirmBooking, cancelBooking, completeBooking } from '@/api/booking'
import { getVenueList } from '@/api/venue'

const route = useRoute()
const loading = ref(false)
const venues = ref<Venue[]>([])
const bookings = ref<BookingListResponse['bookings']>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filterForm = reactive<BookingFilter>({
  venueId: route.query.venueId ? Number(route.query.venueId) : undefined,
  status: undefined,
  date: undefined,
  page: 1,
  pageSize: 10
})

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
  fetchBookings()
}

const resetFilter = () => {
  filterForm.venueId = undefined
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

const handleConfirmBooking = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '確定要確認此預訂嗎？',
      '確認預訂',
      {
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await confirmBooking(id)
    ElMessage.success('預訂已確認')
    fetchBookings()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to confirm booking:', error)
      ElMessage.error('確認預訂失敗')
    }
  }
}

const handleCancelBooking = async (id: number) => {
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
    
    await cancelBooking(id)
    ElMessage.success('預訂已取消')
    fetchBookings()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to cancel booking:', error)
      ElMessage.error('取消預訂失敗')
    }
  }
}

const handleCompleteBooking = async (id: number) => {
  try {
    await ElMessageBox.confirm(
      '確定要將此預訂標記為已完成嗎？',
      '完成預訂',
      {
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await completeBooking(id)
    ElMessage.success('預訂已完成')
    fetchBookings()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Failed to complete booking:', error)
      ElMessage.error('完成預訂失敗')
    }
  }
}

const fetchVenues = async () => {
  try {
    const response = await getVenueList({})
    venues.value = response.venues
  } catch (error) {
    console.error('Failed to fetch venues:', error)
    ElMessage.error('獲取場地列表失敗')
  }
}

const fetchBookings = async () => {
  try {
    loading.value = true
    const params = {
      ...filterForm,
      page: currentPage.value,
      pageSize: pageSize.value
    }
    const response = await getBookingList(params)
    bookings.value = response.bookings
    total.value = response.total
  } catch (error) {
    console.error('Failed to fetch bookings:', error)
    ElMessage.error('獲取預訂列表失敗')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchVenues()
  fetchBookings()
})
</script>

<style scoped>
.booking-manage-container {
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
</style> 