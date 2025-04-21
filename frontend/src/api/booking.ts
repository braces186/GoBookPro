import request from '@/utils/request'
import type { Booking, BookingListResponse, BookingFilter } from '@/types/booking'

export const getBookingList = (params: BookingFilter) => {
  return request.get<BookingListResponse>('/bookings', { params })
}

export const getBookingById = (id: number) => {
  return request.get<Booking>(`/bookings/${id}`)
}

export const getUserBookings = () => {
  return request.get<BookingListResponse>('/bookings/user')
}

export const createBooking = (data: Partial<Booking>) => {
  return request.post<Booking>('/bookings', data)
}

export const confirmBooking = (id: number) => {
  return request.put<Booking>(`/bookings/${id}/confirm`)
}

export const cancelBooking = (id: number) => {
  return request.put<Booking>(`/bookings/${id}/cancel`)
}

export const completeBooking = (id: number) => {
  return request.put<Booking>(`/bookings/${id}/complete`)
} 