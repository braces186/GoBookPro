<template>
  <el-container>
    <el-header>
      <nav>
        <el-menu
          :router="true"
          mode="horizontal"
          :ellipsis="false"
        >
          <el-menu-item index="/">首頁</el-menu-item>
          <el-menu-item index="/venues">場地</el-menu-item>
          <div class="flex-grow" />
          <template v-if="userStore.currentUser">
            <el-menu-item index="/bookings">我的預訂</el-menu-item>
            <el-menu-item index="/profile">個人中心</el-menu-item>
            <el-menu-item @click="handleLogout">登出</el-menu-item>
          </template>
          <template v-else>
            <el-menu-item index="/login">登入</el-menu-item>
            <el-menu-item index="/register">註冊</el-menu-item>
          </template>
        </el-menu>
      </nav>
    </el-header>

    <el-main>
      <router-view />
    </el-main>

    <el-footer>
      <p>&copy; 2024 票務預訂平台. All rights reserved.</p>
    </el-footer>
  </el-container>
</template>

<script setup lang="ts">
import { useUserStore } from './stores/user'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

onMounted(() => {
  userStore.initializeFromStorage()
})

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.el-container {
  min-height: 100vh;
}

.el-header {
  padding: 0;
  border-bottom: 1px solid #eee;
}

.flex-grow {
  flex-grow: 1;
}

.el-footer {
  text-align: center;
  padding: 20px;
  border-top: 1px solid #eee;
}
</style>
