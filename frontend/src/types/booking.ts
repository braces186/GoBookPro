export interface Booking {
  id: number
  userId: number
  venueId: number
  venueName: string
  username: string
  date: string
  startTime: string
  endTime: string
  attendees: number
  totalPrice: number
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED'
  createdAt: string
  updatedAt: string
}

export interface BookingListResponse {
  bookings: Booking[]
  total: number
  page: number
  pageSize: number
}

export interface BookingFilter {
  venueId?: number
  status?: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED'
  date?: string
  page?: number
  pageSize?: number
} 