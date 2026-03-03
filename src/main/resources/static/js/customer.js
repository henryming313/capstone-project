/**
 * Customer operations page logic
 * Handles user creation and customer profile creation
 * All API calls use axios wrapper from api.js
 */

/**
 * Create customer user (role = CUSTOMER)
 * Collects form data and calls createUser API
 * Displays response (success/error) on page
 */
async function createCustomerUser() {
    // Get form values
    const email = document.getElementById('userEmail').value;
    const password = document.getElementById('userPassword').value;
    const mobile = document.getElementById('userMobile').value;

    // Prepare user data (role fixed as CUSTOMER)
    const userData = {
        email: email,
        password: password,
        mobileNumber: mobile,
        role: "CUSTOMER" // Fixed role for customer users
    };

    // Call API
    const response = await createUser(userData);

    // Display response
    const responseDiv = document.getElementById('userResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! User created: ${JSON.stringify(response.data, null, 2)}`;
        // Clear form
        document.getElementById('userPassword').value = '';
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}

/**
 * Create customer profile
 * Collects form data and calls createCustomerProfile API
 * Displays response (success/error) on page
 */
async function createCustomerProfile() {
    // Get form values
    const userId = document.getElementById('profileUserId').value;
    const address = document.getElementById('profileAddress').value;
    const rating = parseFloat(document.getElementById('profileRating').value);

    // Prepare profile data
    const profileData = {
        user: { userId: userId },
        address: address,
        rating: rating
    };

    // Call API
    const response = await createCustomerProfile(profileData);

    // Display response
    const responseDiv = document.getElementById('profileResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Profile created: ${JSON.stringify(response.data, null, 2)}`;
        // Clear form
        document.getElementById('profileUserId').value = '';
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}