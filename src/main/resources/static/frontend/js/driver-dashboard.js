const driverUser = ensureRole("DRIVER");

const driverIdInput = document.getElementById("driver-id");
const cabIdInput = document.getElementById("cab-id");

function getDriverContext() {
  return {
    driverId: Number(driverIdInput.value),
    cabId: Number(cabIdInput.value)
  };
}

function prefillDriverContext() {
  if (driverUser && driverUser.id) {
    driverIdInput.value = String(driverUser.id);
  }
}

function renderPendingTrips(trips) {
  const body = document.getElementById("pending-trips-body");
  body.innerHTML = trips.length
    ? trips.map((trip) => `
      <tr>
        <td>${trip.id}</td>
        <td>${trip.pickupLocation || "-"}</td>
        <td>${trip.dropoffLocation || "-"}</td>
        <td>${statusBadge(trip.status)}</td>
        <td>
          <button onclick="acceptTrip(${trip.id})">Accept</button>
          <button class="danger" onclick="rejectTrip(${trip.id})">Reject</button>
        </td>
      </tr>
    `).join("")
    : `<tr><td colspan="5">No pending trips.</td></tr>`;
}

function renderDriverTrips(trips) {
  const body = document.getElementById("driver-trips-body");
  body.innerHTML = trips.length
    ? trips.map((trip) => {
      let action = "";
      if (trip.status === "ACCEPTED") {
        action = `<button class=\"warning\" onclick=\"startTrip(${trip.id})\">Start</button>`;
      } else if (trip.status === "IN_PROGRESS") {
        action = `<button class=\"success\" onclick=\"completeTrip(${trip.id})\">Complete</button>`;
      } else {
        action = "-";
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
    : `<tr><td colspan="5">No driver trips.</td></tr>`;
}

async function loadPendingTrips() {
  const { driverId } = getDriverContext();
  if (!driverId) {
    setMessage("driver-msg", "Please set Driver ID first.", "error");
    return;
  }
  try {
    setMessage("driver-msg", "Loading pending trips...", "");
    const response = await apiRequest("get", `/trips/pending?driverId=${driverId}`);
    const trips = unwrapData(response) || [];
    renderPendingTrips(trips);
    setMessage("driver-msg", `Loaded ${trips.length} pending trips.`, "success");
  } catch (error) {
    setMessage("driver-msg", error.message || "Failed loading pending trips", "error");
  }
}

async function rejectTrip(tripId) {
  const { driverId } = getDriverContext();
  if (!driverId) {
    setMessage("driver-msg", "Please set Driver ID first.", "error");
    return;
  }
  try {
    const response = await apiRequest("put", `/trips/${tripId}/reject?driverId=${driverId}`);
    unwrapData(response);
    setMessage("driver-msg", `Trip ${tripId} rejected.`, "success");
    loadPendingTrips();
  } catch (error) {
    setMessage("driver-msg", error.message || "Reject failed", "error");
  }
}

async function loadMyTrips() {
  const { driverId } = getDriverContext();
  if (!driverId) {
    setMessage("driver-msg", "Please set Driver ID first.", "error");
    return;
  }

  try {
    setMessage("driver-msg", "Loading driver trips...", "");
    const response = await apiRequest("get", `/trips/driver/${driverId}`);
    const trips = unwrapData(response) || [];
    renderDriverTrips(trips);
    setMessage("driver-msg", `Loaded ${trips.length} driver trips.`, "success");
  } catch (error) {
    setMessage("driver-msg", error.message || "Failed loading driver trips", "error");
  }
}

async function acceptTrip(tripId) {
  const { driverId, cabId } = getDriverContext();
  if (!driverId || !cabId) {
    setMessage("driver-msg", "Driver ID and Cab ID are required.", "error");
    return;
  }

  try {
    const response = await apiRequest("put", `/trips/${tripId}/accept`, { driverId, cabId });
    unwrapData(response);
    setMessage("driver-msg", `Trip ${tripId} accepted.`, "success");
    loadPendingTrips();
    loadMyTrips();
  } catch (error) {
    setMessage("driver-msg", error.message || "Accept failed", "error");
  }
}

async function startTrip(tripId) {
  try {
    const response = await apiRequest("put", `/trips/${tripId}/start`);
    unwrapData(response);
    setMessage("driver-msg", `Trip ${tripId} started.`, "success");
    loadMyTrips();
  } catch (error) {
    setMessage("driver-msg", error.message || "Start failed", "error");
  }
}

async function completeTrip(tripId) {
  try {
    const response = await apiRequest("put", `/trips/${tripId}/complete`);
    unwrapData(response);
    setMessage("driver-msg", `Trip ${tripId} completed.`, "success");
    loadMyTrips();
  } catch (error) {
    setMessage("driver-msg", error.message || "Complete failed", "error");
  }
}

document.getElementById("save-driver-context").addEventListener("click", () => {
  const { driverId, cabId } = getDriverContext();
  if (!driverId || !cabId) {
    setMessage("driver-msg", "Please fill both Driver ID and Cab ID.", "error");
    return;
  }
  setMessage("driver-msg", "Driver context saved for this session.", "success");
});

document.getElementById("refresh-pending").addEventListener("click", loadPendingTrips);
document.getElementById("refresh-my-trips").addEventListener("click", loadMyTrips);

prefillDriverContext();
loadPendingTrips();
loadMyTrips();
