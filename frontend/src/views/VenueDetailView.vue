<template>
  <div class="venue-detail-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="venue-header">
              <h2>{{ venue?.name }}</h2>
              <el-tag :type="venue?.status === 'AVAILABLE' ? 'success' : 'danger'">
                {{ venue?.status === 'AVAILABLE' ? '可預訂' : '已滿' }}
              </el-tag>
            </div>
          </template>
          
          <el-image
            :src="venue?.imageUrl"
            fit="cover"
            class="venue-image"
          />
          
          <div class="venue-info">
            <h3>場地資訊</h3>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="位置">
                <el-icon><Location /></el-icon>
                {{ venue?.location }}
              </el-descriptions-item>
              <el-descriptions-item label="容納人數">
                <el-icon><User /></el-icon>
                {{ venue?.capacity }} 人
              </el-descriptions-item>
              <el-descriptions-item label="價格">
                <el-icon><Money /></el-icon>
                {{ venue?.price }} 元/小時
              </el-descriptions-item>
              <el-descriptions-item label="狀態">
                <el-tag :type="venue?.status === 'AVAILABLE' ? 'success' : 'danger'">
                  {{ venue?.status === 'AVAILABLE' ? '可預訂' : '已滿' }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>
            
            <h3>場地描述</h3>
            <p>{{ venue?.description }}</p>
            
            <h3>設施</h3>
            <el-tag
              v-for="facility in venue?.facilities"
              :key="facility"
              class="facility-tag"
            >
              {{ facility }}
            </el-tag>
            
            <h3>使用規則</h3>
            <ul>
              <li v-for="rule in venue?.rules" :key="rule">
                {{ rule }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="booking-card">
          <template #header>
            <div class="card-header">
              <h3>預訂場地</h3>
            </div>
          </template>
          
          <el-form
            ref="formRef"
            :model="bookingForm"
            :rules="rules"
            label-position="top"
          >
            <el-form-item label="預訂日期" prop="date">
              <el-date-picker
                v-model="bookingForm.date"
                type="date"
                placeholder="選擇日期"
                :disabled-date="disabledDate"
              />
            </el-form-item>
            
            <el-form-item label="開始時間" prop="startTime">
              <el-time-picker
                v-model="bookingForm.startTime"
                format="HH:mm"
                placeholder="選擇開始時間"
              />
            </el-form-item>
            
            <el-form-item label="結束時間" prop="endTime">
              <el-time-picker
                v-model="bookingForm.endTime"
                format="HH:mm"
                placeholder="選擇結束時間"
              />
            </el-form-item>
            
            <el-form-item label="預訂人數" prop="attendees">
              <el-input-number
                v-model="bookingForm.attendees"
                :min="1"
                :max="venue?.capacity"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                @click="handleSubmit"
                :disabled="!venue || venue.status !== 'AVAILABLE'"
              >
                立即預訂
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Location, User, Money } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import type { Venue } from '@/types/venue'

const route = useRoute()
const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)
const venue = ref<Venue | null>(null)

const bookingForm = reactive({
  date: '',
  startTime: '',
  endTime: '',
  attendees: 1
})

const rules = reactive<FormRules>({
  date: [
    { required: true, message: '請選擇預訂日期', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '請選擇開始時間', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '請選擇結束時間', trigger: 'change' }
  ],
  attendees: [
    { required: true, message: '請輸入預訂人數', trigger: 'change' }
  ]
})

const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7
}

const fetchVenue = async () => {
  try {
    const id = Number(route.params.id)
    // TODO: 實現獲取場地詳情的 API 調用
    // 這裡先使用模擬數據
    venue.value = {
      id,
      name: '會議室 A',
      location: '台北市信義區',
      capacity: 50,
      price: 2000,
      status: 'AVAILABLE',
      imageUrl: 'https://example.com/image1.jpg',
      description: '這是一個設備齊全的會議室，適合舉辦各種會議和活動。',
      facilities: ['投影機', '白板', 'WiFi', '空調'],
      rules: [
        '請提前 15 分鐘到達',
        '禁止攜帶食物和飲料',
        '使用完畢後請清理場地'
      ]
    }
  } catch (error) {
    console.error('Failed to fetch venue:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    // TODO: 實現預訂邏輯
    console.log('Booking data:', bookingForm)
    
    // 預訂成功後跳轉到預訂列表頁面
    router.push('/bookings')
  } catch (error) {
    console.error('Booking failed:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchVenue()
})
</script>

<style scoped>
.venue-detail-container {
  padding: 20px;
}

.venue-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.venue-header h2 {
  margin: 0;
}

.venue-image {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 20px;
}

.venue-info {
  margin-top: 20px;
}

.venue-info h3 {
  margin: 20px 0 10px;
}

.facility-tag {
  margin-right: 10px;
  margin-bottom: 10px;
}

.booking-card {
  position: sticky;
  top: 20px;
}

.card-header h3 {
  margin: 0;
}
</style> 