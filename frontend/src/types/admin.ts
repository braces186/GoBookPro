export interface AdminDashboard {
  totalUsers: number
  totalVenues: number
  totalBookings: number
  totalRevenue: number
  recentUsers: {
    id: number
    username: string
    email: string
    role: 'USER' | 'MERCHANT' | 'ADMIN'
    createdAt: string
  }[]
  recentBookings: {
    id: number
    venueName: string
    username: string
    date: string
    totalPrice: number
    status: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED'
  }[]
}

export interface UserListResponse {
  users: {
    id: number
    username: string
    email: string
    phone?: string
    role: 'USER' | 'MERCHANT' | 'ADMIN'
    createdAt: string
    updatedAt: string
  }[]
  total: number
  page: number
  pageSize: number
}

export interface UserFilter {
  username?: string
  email?: string
  role?: 'USER' | 'MERCHANT' | 'ADMIN'
  page?: number
  pageSize?: number
}

export interface UpdateUserRequest {
  username?: string
  email?: string
  phone?: string
  role?: 'USER' | 'MERCHANT' | 'ADMIN'
} 