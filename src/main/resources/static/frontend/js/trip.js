const tripUser = ensureRole("RIDER");

const riderIdInput = document.getElementById("rider-id");
const pickupSelect = document.getElementById("pickup-location");
const dropoffSelect = document.getElementById("dropoff-location");

let map;
let pickupMarker;
let dropoffMarker;
let activeTileLayer;
let activeTileProviderIndex = 0;

const OSM_TILE_PROVIDERS = [
  {
    url: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    options: { attribution: "&copy; OpenStreetMap contributors" }
  },
  {
    url: "https://{s}.tile.openstreetmap.de/{z}/{x}/{y}.png",
    options: { attribution: "&copy; OpenStreetMap contributors" }
  },
  {
    url: "https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png",
    options: {
      attribution: "&copy; OpenStreetMap contributors, HOT"
    }
  }
];

function initTripPage() {
  if (tripUser && tripUser.id) {
    riderIdInput.value = String(tripUser.id);
  }

  const locationNames = Object.keys(FIXED_LOCATIONS);
  pickupSelect.innerHTML = locationNames.map((name) => `<option value=\"${name}\">${name}</option>`).join("");
  dropoffSelect.innerHTML = locationNames.map((name) => `<option value=\"${name}\">${name}</option>`).join("");

  dropoffSelect.selectedIndex = 1;

  initMap();
  updateMarkers();
  loadPassengerTrips();
}

function initMap() {
  map = L.map("trip-map").setView([63.839, 23.135], 13);
  loadTileProvider(0);
}

function loadTileProvider(providerIndex) {
  activeTileProviderIndex = providerIndex;
  const provider = OSM_TILE_PROVIDERS[providerIndex];

  if (!provider) {
    setMessage("trip-msg", "Map tiles blocked by network. Try VPN/proxy or different network.", "error");
    return;
  }

  if (activeTileLayer) {
    map.removeLayer(activeTileLayer);
  }

  activeTileLayer = L.tileLayer(provider.url, provider.options).addTo(map);

  let tileLoaded = false;
  const onTileLoad = () => {
    tileLoaded = true;
  };

  const onTileError = () => {
    if (tileLoaded) {
      return;
    }

    activeTileLayer.off("tileload", onTileLoad);
    activeTileLayer.off("tileerror", onTileError);
    loadTileProvider(providerIndex + 1);
  };

  activeTileLayer.on("tileload", onTileLoad);
  activeTileLayer.on("tileerror", onTileError);
}

function createOrMoveMarker(existingMarker, locationName, markerType) {
  const coord = FIXED_LOCATIONS[locationName];
  if (!coord) {
    return existingMarker;
  }

  if (existingMarker) {
    existingMarker.setLatLng([coord.lat, coord.lng]).bindPopup(`${markerType}: ${locationName}`);
    return existingMarker;
  }

  return L.marker([coord.lat, coord.lng]).addTo(map).bindPopup(`${markerType}: ${locationName}`);
}

function updateMarkers() {
  const pickup = pickupSelect.value;
  const dropoff = dropoffSelect.value;

  pickupMarker = createOrMoveMarker(pickupMarker, pickup, "Pickup");
  dropoffMarker = createOrMoveMarker(dropoffMarker, dropoff, "Dropoff");

  const bounds = [];
  if (pickupMarker) {
    bounds.push(pickupMarker.getLatLng());
  }
  if (dropoffMarker) {
    bounds.push(dropoffMarker.getLatLng());
  }
  if (bounds.length > 0) {
    map.fitBounds(bounds, { padding: [40, 40], maxZoom: 14 });
  }
}

async function estimateFare() {
  const pickupLocation = pickupSelect.value;
  const dropoffLocation = dropoffSelect.value;
  const resultDiv = document.getElementById("fare-estimate-result");

  if (!pickupLocation || !dropoffLocation) {
    setMessage("trip-msg", "Please choose both pickup and dropoff locations.", "error");
    return;
  }
  if (pickupLocation === dropoffLocation) {
    setMessage("trip-msg", "Pickup and dropoff should be different.", "error");
    return;
  }

  try {
    setMessage("trip-msg", "Estimating fare...", "");
    const response = await apiRequest("post", "/fare/estimate", {
      pickupLocation,
      dropoffLocation
    });
    const fare = unwrapData(response);
    resultDiv.style.display = "block";
    resultDiv.innerHTML =
      "<strong>Fare Estimate:</strong> Base " + fare.baseFare + " + Route " + fare.routeFare +
      " = <strong>" + fare.estimatedTotal + " " + fare.currency + "</strong>";
    setMessage("trip-msg", "Fare estimated successfully.", "success");
  } catch (error) {
    resultDiv.style.display = "none";
    setMessage("trip-msg", error.message || "Fare estimate failed", "error");
  }
}

async function createTrip() {
  const riderId = Number(riderIdInput.value);
  const pickupLocation = pickupSelect.value;
  const dropoffLocation = dropoffSelect.value;

  if (!riderId) {
    setMessage("trip-msg", "Please provide rider ID.", "error");
    return;
  }

  if (!pickupLocation || !dropoffLocation) {
    setMessage("trip-msg", "Please choose both pickup and dropoff locations.", "error");
    return;
  }

  if (pickupLocation === dropoffLocation) {
    setMessage("trip-msg", "Pickup and dropoff should be different.", "error");
    return;
  }

  try {
    setMessage("trip-msg", "Submitting trip...", "");
    const response = await apiRequest("post", "/trips", {
      riderId,
      pickupLocation,
      dropoffLocation
    });
    const trip = unwrapData(response);

    setMessage("trip-msg", `Trip ${trip.id} created successfully.`, "success");
    await loadPassengerTrips();
  } catch (error) {
    setMessage("trip-msg", error.message || "Trip creation failed", "error");
  }
}

async function loadPassengerTrips() {
  const riderId = Number(riderIdInput.value);
  if (!riderId) {
    return;
  }

  try {
    const response = await apiRequest("get", `/trips/passenger/${riderId}`);
    const trips = unwrapData(response) || [];

    const body = document.getElementById("trip-list-body");
    body.innerHTML = trips.length
      ? trips.map((trip) => {
          let action = "-";
          if (trip.status === "PENDING" || trip.status === "ACCEPTED") {
            action = `<button class="danger" onclick="cancelTripFromList(${trip.id})">Cancel</button>`;
          }
          return `
      <tr>
        <td>${trip.id}</td>
        <td>${trip.pickupLocation || "-"}</td>
        <td>${trip.dropoffLocation || "-"}</td>
        <td>${statusBadge(trip.status)}</td>
        <td>${action}</td>
      </tr>
      `;
        }).join("")
      : `<tr><td colspan="5">No trips found.</td></tr>`;
  } catch (error) {
    setMessage("trip-msg", error.message || "Failed to load trips", "error");
  }
}

async function cancelTripFromList(tripId) {
  if (!tripUser) return;
  try {
    const response = await apiRequest("put", `/trips/${tripId}/cancel?userId=${tripUser.id}`);
    unwrapData(response);
    setMessage("trip-msg", `Trip ${tripId} cancelled.`, "success");
    loadPassengerTrips();
  } catch (error) {
    setMessage("trip-msg", error.message || "Cancel failed", "error");
  }
}

pickupSelect.addEventListener("change", updateMarkers);
dropoffSelect.addEventListener("change", updateMarkers);
document.getElementById("estimate-fare-btn").addEventListener("click", estimateFare);
document.getElementById("create-trip-btn").addEventListener("click", createTrip);
document.getElementById("refresh-trip-list").addEventListener("click", loadPassengerTrips);

initTripPage();
