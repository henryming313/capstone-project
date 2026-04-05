const API_BASE_URL = "http://localhost:8081/api";

const FIXED_LOCATIONS = {
  "Centria University": { lat: 63.838, lng: 23.13 },
  "Kokkola Railway Station": { lat: 63.841, lng: 23.123 },
  "Kokkola Bus Station": { lat: 63.839, lng: 23.131 },
  "City Center": { lat: 63.838, lng: 23.141 },
  "Chydenia Shopping Center": { lat: 63.84, lng: 23.145 }
};

async function apiRequest(method, path, data) {
  try {
    const response = await axios({
      method,
      url: `${API_BASE_URL}${path}`,
      data
    });
    return response.data;
  } catch (error) {
    const serverMsg = error.response && error.response.data && error.response.data.msg;
    const validationMsg = error.response && error.response.data && error.response.data.message;
    throw new Error(serverMsg || validationMsg || error.message || "Request failed");
  }
}

function unwrapData(response) {
  if (response && typeof response.code !== "undefined") {
    if (response.code === 0) {
      return response.data;
    }
    throw new Error(response.msg || "Operation failed");
  }
  return response;
}
