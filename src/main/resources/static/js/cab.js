/**
 * Cab management page logic
 * Handles new cab creation
 * All API calls use axios wrapper from api.js
 */

/**
 * Create new cab
 * Collects form data and calls createCab API
 * Displays response (success/error) on page
 */
async function createCab() {
    // Get form values
    const carNumber = document.getElementById('carNumber').value;
    const carModel = document.getElementById('carModel').value;
    const carType = document.getElementById('carType').value;
    const perKmRate = parseFloat(document.getElementById('perKmRate').value);
    const status = document.getElementById('cabStatus').value;

    // Prepare cab data
    const cabData = {
        carNumber: carNumber,
        carModel: carModel,
        carType: carType,
        perKmRate: perKmRate,
        status: status
    };

    // Call API
    const response = await createCab(cabData);

    // Display response
    const responseDiv = document.getElementById('cabResponse');
    if (response.success) {
        responseDiv.className = 'response success';
        responseDiv.textContent = `Success! Cab created: ${JSON.stringify(response.data, null, 2)}`;
        // Clear form
        document.getElementById('carNumber').value = '';
        document.getElementById('carModel').value = '';
    } else {
        responseDiv.className = 'response error';
        responseDiv.textContent = `Error: ${JSON.stringify(response.error, null, 2)}`;
    }
}