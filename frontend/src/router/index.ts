import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/venues',
      name: 'venues',
      component: () => import('../views/VenueListView.vue')
    },
    {
      path: '/venues/:id',
      name: 'venue-detail',
      component: () => import('../views/VenueDetailView.vue')
    },
    {
      path: '/bookings',
      name: 'bookings',
      component: () => import('../views/BookingListView.vue')
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/ProfileView.vue')
    },
    {
      path: '/admin/venues',
      name: 'admin-venues',
      component: () => import('../views/admin/VenueManageView.vue')
    },
    {
      path: '/admin/bookings',
      name: 'admin-bookings',
      component: () => import('../views/admin/BookingManageView.vue')
    }
  ]
})

export default router 