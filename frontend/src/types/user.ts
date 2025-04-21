export interface User {
  id: number
  username: string
  email: string
  phone?: string
  role: 'USER' | 'MERCHANT' | 'ADMIN'
  createdAt: string
  updatedAt: string
}

export interface UserProfile {
  user: User
  totalBookings: number
  totalSpent: number
  recentBookings: {
    id: number
    venueName: string
    date: string
    status: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED'
  }[]
}

export interface UpdateProfileRequest {
  username?: string
  email?: string
  phone?: string
  currentPassword?: string
  newPassword?: string
}

export interface LoginResponse {
  token: string
  user: User
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
}

export interface RegisterResponse {
  token: string
  user: User
} 