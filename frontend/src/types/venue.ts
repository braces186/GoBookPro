export interface Venue {
  id: number
  name: string
  location: string
  description: string
  capacity: number
  pricePerHour: number
  merchantId: number
  createdAt: string
  updatedAt: string
}

export interface CreateVenueRequest {
  name: string
  location: string
  description: string
  capacity: number
  pricePerHour: number
}

export interface VenueListResponse {
  success: boolean
  data: {
    content: Venue[]
    totalElements: number
    totalPages: number
    size: number
    number: number
  }
}

export interface VenueFilter {
  name?: string
  minPrice?: number
  maxPrice?: number
  capacity?: number
  page?: number
  size?: number
} 