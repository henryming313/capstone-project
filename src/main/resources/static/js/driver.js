/**
 * Driver operations page logic
 * Handles driver user creation, profile creation and status update
 * All API calls use axios wrapper from api.js
 */

/**
 * Create driver user (role = DRIVER)
 * Collects form data and calls createUser API
 * Displays response (success/error) on page
 */
async function createDriverUser() {
    // Get form values
    const email = document.getElementById('driverEmail').value;
    const password = document.getElementById('driverPassword').value;
    const mobile = document.getElementById('driverMobile').value;

    // Prepare user data (role fixed as DRIVER)
    const userData = {
        email: email,
        password: password,
        mobileNumber: mobile,
        role: "DRIVER" // Fixed role for driver users
    };

    // Call API
    const response = await createUser(userData);

    // Display response
    const responseDiv = document.getElementById('driverUserResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Driver user created: ${JSON.stringify(response.data, null, 2)}`;
        // Clear form
        document.getElementById('driverPassword').value = '';
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Create driver profile
 * Collects form data and calls createDriverProfile API
 * Displays response (success/error) on page
 */
async function createDriverProfile() {
    // Get form values
    const userId = document.getElementById('driverProfileUserId').value;
    const licenseNumber = document.getElementById('licenseNumber').value;
    const rating = parseFloat(document.getElementById('driverRating').value);
    const currentLocation = document.getElementById('currentLocation').value;

    // Prepare profile data
    const profileData = {
        user: { userId: userId },
        licenseNumber: licenseNumber,
        rating: rating,
        currentLocation: currentLocation,
        currentStatus: "OFFLINE" // Default status
    };

    // Call API
    const response = await createDriverProfile(profileData);

    // Display response
    const responseDiv = document.getElementById('driverProfileResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Driver profile created: ${JSON.stringify(response.data, null, 2)}`;
        // Clear form
        document.getElementById('driverProfileUserId').value = '';
        document.getElementById('licenseNumber').value = '';
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Update driver status (online/offline/on trip)
 * Collects form data and calls updateDriverStatus API
 * Displays response (success/error) on page
 */
async function updateDriverStatus() {
    // Get form values
    const driverId = document.getElementById('driverId').value;
    const status = document.getElementById('driverStatus').value;
    const location = document.getElementById('statusLocation').value;

    // Call API
    const response = await window.updateDriverStatus(driverId, status, location);

    // Display response
    const responseDiv = document.getElementById('statusResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Driver status updated: ${JSON.stringify(response.data, null, 2)}`;
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}