const adminUser = ensureRole("ADMIN");

const adminIdInput = document.getElementById("admin-id");

function getAdminId() {
  return Number(adminIdInput.value);
}

function prefillAdminId() {
  if (adminUser && adminUser.id) {
    adminIdInput.value = String(adminUser.id);
  }
}

// ==================== Users ====================

async function loadUsers() {
  const adminId = getAdminId();
  if (!adminId) {
    setMessage("users-msg", "Please set Admin ID first.", "error");
    return;
  }
  try {
    setMessage("users-msg", "Loading users...", "");
    const response = await apiRequest("get", `/admin/users?adminId=${adminId}`);
    const users = unwrapData(response) || [];
    renderUsers(users);
    setMessage("users-msg", `Loaded ${users.length} users.`, "success");
  } catch (error) {
    setMessage("users-msg", error.message || "Failed to load users", "error");
  }
}

function renderUsers(users) {
  const body = document.getElementById("users-body");
  body.innerHTML = users.length
    ? users.map(function (u) {
        const isBanned = u.status === "BANNED";
        const btnClass = isBanned ? "success" : "danger";
        const btnText = isBanned ? "Unban" : "Ban";
        const newStatus = isBanned ? "ACTIVE" : "BANNED";
        return '<tr>' +
          '<td>' + u.id + '</td>' +
          '<td>' + (u.name || "-") + '</td>' +
          '<td>' + (u.email || "-") + '</td>' +
          '<td>' + (u.role || "-") + '</td>' +
          '<td>' + statusBadgeUser(u.status) + '</td>' +
          '<td><button class="' + btnClass + '" onclick="toggleUserStatus(' + u.id + ',\'' + newStatus + '\')">' + btnText + '</button></td>' +
          '</tr>';
      }).join("")
    : '<tr><td colspan="6">No users found.</td></tr>';
}

function statusBadgeUser(status) {
  if (!status) return "-";
  return '<span class="badge ' + status + '">' + status + '</span>';
}

async function toggleUserStatus(userId, newStatus) {
  const adminId = getAdminId();
  if (!adminId) {
    setMessage("users-msg", "Please set Admin ID first.", "error");
    return;
  }
  try {
    const response = await apiRequest("put", "/admin/users/" + userId + "/status?adminId=" + adminId + "&status=" + newStatus);
    unwrapData(response);
    setMessage("users-msg", "User " + userId + " status changed to " + newStatus + ".", "success");
    loadUsers();
  } catch (error) {
    setMessage("users-msg", error.message || "Failed to update user status", "error");
  }
}

// ==================== Trips ====================

async function loadTrips() {
  const adminId = getAdminId();
  if (!adminId) {
    setMessage("trips-msg", "Please set Admin ID first.", "error");
    return;
  }
  const statusFilter = document.getElementById("trip-status-filter").value;
  let url = "/admin/trips?adminId=" + adminId;
  if (statusFilter) {
    url += "&status=" + statusFilter;
  }
  try {
    setMessage("trips-msg", "Loading trips...", "");
    const response = await apiRequest("get", url);
    const trips = unwrapData(response) || [];
    renderTrips(trips);
    setMessage("trips-msg", "Loaded " + trips.length + " trips.", "success");
  } catch (error) {
    setMessage("trips-msg", error.message || "Failed to load trips", "error");
  }
}

function renderTrips(trips) {
  const body = document.getElementById("trips-body");
  body.innerHTML = trips.length
    ? trips.map(function (t) {
        return '<tr>' +
          '<td>' + t.id + '</td>' +
          '<td>' + (t.rider ? (t.rider.name || t.rider.id) : (t.riderId || "-")) + '</td>' +
          '<td>' + (t.driver ? (t.driver.name || t.driver.id) : "-") + '</td>' +
          '<td>' + (t.cab ? (t.cab.plateNumber || t.cab.id) : "-") + '</td>' +
          '<td>' + (t.pickupLocation || "-") + '</td>' +
          '<td>' + (t.dropoffLocation || "-") + '</td>' +
          '<td>' + statusBadge(t.status) + '</td>' +
          '<td>' + (t.totalFare != null ? t.totalFare : "-") + '</td>' +
          '</tr>';
      }).join("")
    : '<tr><td colspan="8">No trips found.</td></tr>';
}

// ==================== Assign ====================

async function assignTrip() {
  const adminId = getAdminId();
  const tripId = Number(document.getElementById("assign-trip-id").value);
  const driverId = Number(document.getElementById("assign-driver-id").value);
  const cabId = Number(document.getElementById("assign-cab-id").value);

  if (!adminId) {
    setMessage("assign-msg", "Please set Admin ID first.", "error");
    return;
  }
  if (!tripId || !driverId || !cabId) {
    setMessage("assign-msg", "Trip ID, Driver ID, and Cab ID are all required.", "error");
    return;
  }

  try {
    const url = "/admin/trips/" + tripId + "/assign?adminId=" + adminId + "&driverId=" + driverId + "&cabId=" + cabId;
    const response = await apiRequest("put", url);
    unwrapData(response);
    setMessage("assign-msg", "Trip " + tripId + " assigned to driver " + driverId + " with cab " + cabId + ".", "success");
    loadTrips();
  } catch (error) {
    setMessage("assign-msg", error.message || "Assign failed", "error");
  }
}

// ==================== Event Listeners ====================

document.getElementById("refresh-users-btn").addEventListener("click", loadUsers);
document.getElementById("refresh-trips-btn").addEventListener("click", loadTrips);
document.getElementById("trip-status-filter").addEventListener("change", loadTrips);
document.getElementById("assign-trip-btn").addEventListener("click", assignTrip);

// ==================== Init ====================

prefillAdminId();
loadUsers();
loadTrips();
