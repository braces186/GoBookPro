<template>
  <main>
    <el-row class="mb-4">
      <el-col :span="24">
        <h1>歡迎來到票務預訂平台</h1>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>瀏覽場地</span>
            </div>
          </template>
          <div class="card-content">
            <p>查看所有可用的場地和活動</p>
            <el-button type="primary" @click="$router.push('/venues')">立即瀏覽</el-button>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>我的預訂</span>
            </div>
          </template>
          <div class="card-content">
            <p>管理您的預訂記錄</p>
            <el-button type="primary" @click="$router.push('/bookings')">查看預訂</el-button>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>個人中心</span>
            </div>
          </template>
          <div class="card-content">
            <p>管理您的個人資料</p>
            <el-button type="primary" @click="$router.push('/profile')">前往設置</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </main>
</template>

<style scoped>
.mb-4 {
  margin-bottom: 1.5rem;
}

h1 {
  text-align: center;
  margin: 2rem 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
}

.card-content p {
  margin-bottom: 1rem;
}
</style> 