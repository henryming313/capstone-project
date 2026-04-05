const passenger = ensureRole("RIDER");

function renderPassengerInfo() {
  if (!passenger) {
    return;
  }

  document.getElementById("user-info").innerHTML = `
    <div><strong>ID:</strong> ${passenger.id}</div>
    <div><strong>Name:</strong> ${passenger.name || "-"}</div>
    <div><strong>Email:</strong> ${passenger.email || "-"}</div>
    <div><strong>Role:</strong> Passenger</div>
  `;
}

async function loadPassengerTrips() {
  if (!passenger) {
    return;
  }

  setMessage("passenger-msg", "Loading trips...", "");
  try {
    const response = await apiRequest("get", `/trips/passenger/${passenger.id}`);
    const trips = unwrapData(response) || [];

    const body = document.getElementById("passenger-trips-body");
    body.innerHTML = trips.length
      ? trips.map((trip) => {
          let action = "-";
          if (trip.status === "PENDING" || trip.status === "ACCEPTED") {
            action = `<button class="danger" onclick="cancelTrip(${trip.id})">Cancel</button>`;
          }
          return `
          <tr>
            <td>${trip.id}</td>
            <td>${trip.pickupLocation || "-"}</td>
            <td>${trip.dropoffLocation || "-"}</td>
            <td>${statusBadge(trip.status)}</td>
            <td>${trip.driver ? trip.driver.name || trip.driver.id : "-"}</td>
            <td>${trip.cab ? trip.cab.plateNumber || trip.cab.id : "-"}</td>
            <td>${action}</td>
          </tr>
          `;
        }).join("")
      : `<tr><td colspan="7">No trips found.</td></tr>`;

    setMessage("passenger-msg", `Loaded ${trips.length} trips.`, "success");
  } catch (error) {
    setMessage("passenger-msg", error.message || "Failed to load trips", "error");
  }
}

async function cancelTrip(tripId) {
  if (!passenger) return;
  try {
    const response = await apiRequest("put", `/trips/${tripId}/cancel?userId=${passenger.id}`);
    unwrapData(response);
    setMessage("passenger-msg", `Trip ${tripId} cancelled.`, "success");
    loadPassengerTrips();
  } catch (error) {
    setMessage("passenger-msg", error.message || "Cancel failed", "error");
  }
}

document.getElementById("refresh-trips-btn").addEventListener("click", loadPassengerTrips);

renderPassengerInfo();
loadPassengerTrips();
