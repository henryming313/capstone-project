/**
 * Trip booking page logic
 * Handles full trip lifecycle: create booking → assign driver → start trip → complete trip → pay → review
 * All API calls use axios wrapper from api.js
 */

/**
 * Create new trip booking
 * Collects form data and calls createBooking API
 * Displays response (success/error) on page
 */
async function createBooking() {
    // Get form values
    const customerId = document.getElementById('bookingCustomerId').value;
    const pickupLocation = document.getElementById('pickupLocation').value;
    const dropoffLocation = document.getElementById('dropoffLocation').value;
    const baseFare = parseFloat(document.getElementById('baseFare').value);

    // Prepare booking data
    const bookingData = {
        customer: { customerId: customerId },
        pickupLocation: pickupLocation,
        dropoffLocation: dropoffLocation,
        baseFare: baseFare,
        surgeMultiplier: 1.0, // Default surge multiplier
        status: "PENDING" // Default status
    };

    // Call API
    const response = await createBooking(bookingData);

    // Display response
    const responseDiv = document.getElementById('bookingResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Booking created: ${JSON.stringify(response.data, null, 2)}`;
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Assign driver and cab to booking
 * Collects form data and calls assignDriverToBooking API
 * Displays response (success/error) on page
 */
async function assignDriverToBooking() {
    // Get form values
    const bookingId = document.getElementById('assignBookingId').value;
    const driverId = document.getElementById('assignDriverId').value;
    const cabId = document.getElementById('assignCabId').value;

    // Call API
    const response = await window.assignDriverToBooking(bookingId, driverId, cabId);

    // Display response
    const responseDiv = document.getElementById('assignResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Driver assigned: ${JSON.stringify(response.data, null, 2)}`;
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Start trip
 * Collects form data and calls startTrip API
 * Displays response (success/error) on page
 */
async function startTrip() {
    // Get form values
    const bookingId = document.getElementById('startBookingId').value;

    // Call API
    const response = await window.startTrip(bookingId);

    // Display response
    const responseDiv = document.getElementById('startResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Trip started: ${JSON.stringify(response.data, null, 2)}`;
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Complete trip
 * Collects form data and calls completeTrip API
 * Displays response (success/error) on page
 */
async function completeTrip() {
    // Get form values
    const bookingId = document.getElementById('completeBookingId').value;
    const distanceKm = parseFloat(document.getElementById('distanceKm').value);

    // Call API
    const response = await window.completeTrip(bookingId, distanceKm);

    // Display response
    const responseDiv = document.getElementById('completeResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Trip completed: ${JSON.stringify(response.data, null, 2)}`;
        // Auto-fill payment amount with total fare
        const totalFare = response.data.totalFare;
        document.getElementById('paymentAmount').value = totalFare;
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Create trip payment
 * Collects form data and calls createTripPayment API
 * Displays response (success/error) on page
 */
async function createTripPayment() {
    // Get form values
    const bookingId = document.getElementById('paymentBookingId').value;
    const amount = parseFloat(document.getElementById('paymentAmount').value);
    const paymentMethod = document.getElementById('paymentMethod').value;

    // Prepare payment data
    const paymentData = {
        amount: amount,
        paymentMethod: paymentMethod,
        status: "COMPLETED" // Default status
    };

    // Call API
    const response = await window.createTripPayment(bookingId, paymentData);

    // Display response
    const responseDiv = document.getElementById('paymentResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Payment completed: ${JSON.stringify(response.data, null, 2)}`;
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Create trip review
 * Collects form data and calls createTripReview API
 * Displays response (success/error) on page
 */
async function createTripReview() {
    // Get form values
    const bookingId = document.getElementById('reviewBookingId').value;
    const rating = parseInt(document.getElementById('reviewRating').value);
    const comments = document.getElementById('reviewComments').value;

    // Get booking details to get driver/customer info
    const bookingResponse = await getBookingById(bookingId);
    if (!bookingResponse.success) {
        const responseDiv = document.getElementById('reviewResponse');
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error getting booking: ${JSON.stringify(bookingResponse.error, null, 2)}`;
        return;
    }

    // Prepare review data
    const reviewData = {
        rating: rating,
        comments: comments,
        driver: bookingResponse.data.driver,
        customer: bookingResponse.data.customer
    };

    // Call API
    const response = await window.createTripReview(bookingId, reviewData);

    // Display response
    const responseDiv = document.getElementById('reviewResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Review submitted: ${JSON.stringify(response.data, null, 2)}`;
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}