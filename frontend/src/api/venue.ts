import api from './index';
import type { Venue, CreateVenueRequest, VenueFilter } from '@/types/venue';

export const getVenues = async (params?: VenueFilter) => {
    const response = await api.get('/venues', { params });
    return response.data;
};

export const getVenueById = async (id: number) => {
    const response = await api.get(`/venues/${id}`);
    return response.data;
};

export const getVenuesByMerchant = async (merchantId: number) => {
    const response = await api.get(`/venues/merchant/${merchantId}`);
    return response.data;
};

export const createVenue = async (merchantId: number, data: CreateVenueRequest) => {
    const response = await api.post('/venues', data, {
        params: { merchantId }
    });
    return response.data;
};

export const updateVenue = async (id: number, data: Venue) => {
    const response = await api.put(`/venues/${id}`, data);
    return response.data;
};

export const deleteVenue = async (id: number) => {
    const response = await api.delete(`/venues/${id}`);
    return response.data;
};

export const getVenueBookings = (venueId: number) => {
  return api.get(`/bookings/venue/${venueId}`)
} 