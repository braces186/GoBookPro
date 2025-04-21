<template>
  <div class="venue-list-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <h1>場地列表</h1>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="filter-card">
          <template #header>
            <div class="card-header">
              <span>篩選條件</span>
            </div>
          </template>
          
          <el-form :model="filterForm" label-position="top">
            <el-form-item label="場地名稱">
              <el-input v-model="filterForm.name" placeholder="輸入場地名稱" />
            </el-form-item>
            
            <el-form-item label="價格範圍">
              <el-slider
                v-model="filterForm.priceRange"
                range
                :min="0"
                :max="10000"
                :step="100"
              />
            </el-form-item>
            
            <el-form-item label="容納人數">
              <el-input-number
                v-model="filterForm.capacity"
                :min="1"
                :max="1000"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="handleSearch">搜尋</el-button>
              <el-button @click="resetFilter">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-row :gutter="20">
          <el-col
            v-for="venue in venues"
            :key="venue.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="8"
            :xl="6"
          >
            <el-card class="venue-card" @click="goToDetail(venue.id)">
              <template #header>
                <div class="venue-header">
                  <h3>{{ venue.name }}</h3>
                  <el-tag type="success">可預訂</el-tag>
                </div>
              </template>
              
              <div class="venue-content">
                <el-image
                  :src="venue.imageUrl"
                  fit="cover"
                  class="venue-image"
                />
                <div class="venue-info">
                  <p><el-icon><Location /></el-icon> {{ venue.location }}</p>
                  <p><el-icon><User /></el-icon> 容納人數: {{ venue.capacity }}</p>
                  <p><el-icon><Money /></el-icon> 價格: {{ venue.pricePerHour }} 元/小時</p>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <div class="pagination-container">
          <el-pagination
            :current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            :page-sizes="[12, 24, 36, 48]"
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
import { Location, User, Money } from '@element-plus/icons-vue'
import type { Venue } from '@/types/venue'
import { getVenues } from '@/api/venue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const venues = ref<Venue[]>([])
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const loading = ref(false)

const filterForm = reactive({
  name: '',
  priceRange: [0, 10000],
  capacity: 1
})

const handleSearch = () => {
  currentPage.value = 1
  fetchVenues()
}

const resetFilter = () => {
  filterForm.name = ''
  filterForm.priceRange = [0, 10000]
  filterForm.capacity = 1
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

const goToDetail = (id: number) => {
  router.push(`/venues/${id}`)
}

const fetchVenues = async () => {
  loading.value = true
  try {
    const response = await getVenues({
      name: filterForm.name,
      minPrice: filterForm.priceRange[0],
      maxPrice: filterForm.priceRange[1],
      capacity: filterForm.capacity,
      page: currentPage.value - 1,
      size: pageSize.value
    })
    console.log('API Response:', response)
    venues.value = response.content
    total.value = response.totalElements
    currentPage.value = response.number + 1
    pageSize.value = response.size
  } catch (error) {
    console.error('獲取場地列表失敗:', error)
    ElMessage.error(`獲取場地列表失敗: ${error instanceof Error ? error.message : '未知錯誤'}`)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchVenues()
})
</script>

<style scoped>
.venue-list-container {
  padding: 20px;
}

h1 {
  margin-bottom: 20px;
  text-align: center;
}

.filter-card {
  margin-bottom: 20px;
}

.venue-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.venue-card:hover {
  transform: translateY(-5px);
}

.venue-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.venue-header h3 {
  margin: 0;
}

.venue-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.venue-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
}

.venue-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.venue-info p {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style> 