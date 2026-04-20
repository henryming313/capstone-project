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
          } else if (trip.status === "COMPLETED") {
            action = `<button class="warning" onclick="openRatingForm(${trip.id})">Rate</button>`;
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

function openRatingForm(tripId) {
  const existing = document.getElementById("rating-overlay");
  if (existing) existing.remove();

  const overlay = document.createElement("div");
  overlay.id = "rating-overlay";
  overlay.style.cssText = "position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.4);display:flex;align-items:center;justify-content:center;z-index:999;";
  overlay.innerHTML =
    '<div style="background:#fff;border-radius:14px;padding:24px;width:340px;max-width:90%;">' +
      '<h3 style="margin-top:0;">Rate Trip #' + tripId + '</h3>' +
      '<label for="rating-score">Score (1-5)</label>' +
      '<input id="rating-score" type="number" min="1" max="5" value="5" />' +
      '<label for="rating-comment">Comment (optional)</label>' +
      '<textarea id="rating-comment" rows="3" placeholder="Great ride!"></textarea>' +
      '<div style="display:flex;gap:8px;margin-top:8px;">' +
        '<button onclick="submitRating(' + tripId + ')">Submit</button>' +
        '<button class="secondary" onclick="closeRatingForm()">Cancel</button>' +
      '</div>' +
      '<p id="rating-msg" class="message"></p>' +
    '</div>';
  document.body.appendChild(overlay);
}

function closeRatingForm() {
  const overlay = document.getElementById("rating-overlay");
  if (overlay) overlay.remove();
}

async function submitRating(tripId) {
  if (!passenger) return;
  const score = Number(document.getElementById("rating-score").value);
  const comment = (document.getElementById("rating-comment").value || "").trim();

  if (!score || score < 1 || score > 5) {
    setMessage("rating-msg", "Score must be between 1 and 5.", "error");
    return;
  }

  try {
    const response = await apiRequest("post", "/ratings", {
      tripId: tripId,
      riderId: passenger.id,
      score: score,
      comment: comment
    });
    unwrapData(response);
    closeRatingForm();
    setMessage("passenger-msg", "Trip " + tripId + " rated successfully.", "success");
    loadPassengerTrips();
  } catch (error) {
    setMessage("rating-msg", error.message || "Rating failed", "error");
  }
}

document.getElementById("refresh-trips-btn").addEventListener("click", loadPassengerTrips);

renderPassengerInfo();
loadPassengerTrips();
