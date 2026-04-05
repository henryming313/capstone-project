const vehicleUser = ensureRole("DRIVER");

const driverIdInput = document.getElementById("driver-id");

function prefillDriverId() {
  if (vehicleUser && vehicleUser.id) {
    driverIdInput.value = String(vehicleUser.id);
  }
}

async function addCab() {
  const driverId = Number(driverIdInput.value);
  const brand = document.getElementById("brand").value.trim();
  const model = document.getElementById("model").value.trim();
  const color = document.getElementById("color").value.trim();
  const plateNumber = document.getElementById("plate-number").value.trim();
  const cabType = document.getElementById("cab-type").value;

  if (!driverId || !brand || !model || !color || !plateNumber || !cabType) {
    setMessage("cab-msg", "Please complete all fields.", "error");
    return;
  }

  try {
    setMessage("cab-msg", "Adding cab...", "");
    const response = await apiRequest("post", "/cabs", {
      driverId,
      brand,
      model,
      color,
      plateNumber,
      cabType
    });

    const cab = unwrapData(response);
    setMessage("cab-msg", `Cab ${cab.id} created successfully.`, "success");
    await loadCabs();
  } catch (error) {
    setMessage("cab-msg", error.message || "Add cab failed", "error");
  }
}

async function loadCabs() {
  const driverId = Number(driverIdInput.value);
  if (!driverId) {
    setMessage("cab-msg", "Please input driver ID to load cabs.", "error");
    return;
  }

  try {
    const response = await apiRequest("get", `/cabs/driver/${driverId}`);
    const cabs = unwrapData(response) || [];

    const body = document.getElementById("cabs-body");
    body.innerHTML = cabs.length
      ? cabs.map((cab) => `
      <tr>
        <td>${cab.id}</td>
        <td>${cab.plateNumber || "-"}</td>
        <td>${cab.brand || "-"}</td>
        <td>${cab.model || "-"}</td>
        <td>${cab.status || "-"}</td>
      </tr>
      `).join("")
      : `<tr><td colspan="5">No vehicles found for this driver.</td></tr>`;

    setMessage("cab-msg", `Loaded ${cabs.length} vehicles.`, "success");
  } catch (error) {
    setMessage("cab-msg", error.message || "Load cabs failed", "error");
  }
}

document.getElementById("add-cab-btn").addEventListener("click", addCab);
document.getElementById("load-cabs-btn").addEventListener("click", loadCabs);

prefillDriverId();
loadCabs();
