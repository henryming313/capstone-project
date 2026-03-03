// Axios API wrapper for cab booking system
// All API endpoints are mapped to backend Spring Boot REST APIs
// Base URL: Backend server address (adjust according to actual deployment)
const API_BASE_URL = 'http://localhost:8080/api/v1';

/**
 * Create a new user (customer/driver)
 * @param {Object} userData - User creation data (email, password, role, etc.)
 * @returns {Promise} Axios promise with response data
 */
async function createUser(userData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/users`, userData);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Create customer profile
 * @param {Object} profileData - Customer profile data (userId, address, etc.)
 * @returns {Promise} Axios promise with response data
 */
async function createCustomerProfile(profileData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/customers`, profileData);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Create driver profile
 * @param {Object} profileData - Driver profile data (userId, licenseNumber, etc.)
 * @returns {Promise} Axios promise with response data
 */
async function createDriverProfile(profileData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/drivers`, profileData);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Update driver status (online/offline)
 * @param {String} driverId - Driver profile ID
 * @param {String} status - New status (ONLINE/OFFLINE/ON_TRIP)
 * @param {String} location - Optional current location
 * @returns {Promise} Axios promise with response data
 */
async function updateDriverStatus(driverId, status, location = '') {
    try {
        const params = { status };
        if (location) params.currentLocation = location;

        const response = await axios.patch(`${API_BASE_URL}/drivers/${driverId}/status`, null, { params });
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Create new cab
 * @param {Object} cabData - Cab data (carNumber, carModel, perKmRate, etc.)
 * @returns {Promise} Axios promise with response data
 */
async function createCab(cabData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/cabs`, cabData);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Create new trip booking
 * @param {Object} bookingData - Booking data (customerId, pickupLocation, baseFare, etc.)
 * @returns {Promise} Axios promise with response data
 */
async function createBooking(bookingData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/bookings`, bookingData);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Assign driver and cab to booking
 * @param {String} bookingId - Booking ID
 * @param {String} driverId - Driver ID
 * @param {String} cabId - Cab ID
 * @returns {Promise} Axios promise with response data
 */
async function assignDriverToBooking(bookingId, driverId, cabId) {
    try {
        const params = { driverId, cabId };
        const response = await axios.patch(`${API_BASE_URL}/bookings/${bookingId}/assign`, null, { params });
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Start trip
 * @param {String} bookingId - Booking ID
 * @returns {Promise} Axios promise with response data
 */
async function startTrip(bookingId) {
    try {
        const response = await axios.patch(`${API_BASE_URL}/bookings/${bookingId}/start`);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Complete trip
 * @param {String} bookingId - Booking ID
 * @param {Number} distanceKm - Actual travel distance in kilometers
 * @returns {Promise} Axios promise with response data
 */
async function completeTrip(bookingId, distanceKm) {
    try {
        const params = { distanceKm };
        const response = await axios.patch(`${API_BASE_URL}/bookings/${bookingId}/complete`, null, { params });
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Create trip payment
 * @param {String} bookingId - Booking ID
 * @param {Object} paymentData - Payment data (amount, paymentMethod, etc.)
 * @returns {Promise} Axios promise with response data
 */
async function createTripPayment(bookingId, paymentData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/bookings/${bookingId}/pay`, paymentData);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Create trip review
 * @param {String} bookingId - Booking ID
 * @param {Object} reviewData - Review data (rating, comments, etc.)
 * @returns {Promise} Axios promise with response data
 */
async function createTripReview(bookingId, reviewData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/bookings/${bookingId}/review`, reviewData);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}

/**
 * Get booking by ID
 * @param {String} bookingId - Booking ID
 * @returns {Promise} Axios promise with response data
 */
async function getBookingById(bookingId) {
    try {
        const response = await axios.get(`${API_BASE_URL}/bookings/${bookingId}`);
        return { success: true, data: response.data };
    } catch (error) {
        return { success: false, error: error.response?.data || error.message };
    }
}