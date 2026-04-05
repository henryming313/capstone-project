const USER_STORAGE_KEY = "cabbooking_current_user";
const MODE_STORAGE_KEY = "cabbooking_mode";

function saveCurrentUser(user) {
  localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
}

function getCurrentUser() {
  const raw = localStorage.getItem(USER_STORAGE_KEY);
  if (!raw) {
    return null;
  }
  try {
    return JSON.parse(raw);
  } catch (e) {
    return null;
  }
}

function setCurrentMode(mode) {
  localStorage.setItem(MODE_STORAGE_KEY, mode);
}

function getCurrentMode() {
  return localStorage.getItem(MODE_STORAGE_KEY) || "RIDER";
}

function logout() {
  localStorage.removeItem(USER_STORAGE_KEY);
  localStorage.removeItem(MODE_STORAGE_KEY);
  window.location.href = "login.html";
}

function ensureLoggedIn() {
  const user = getCurrentUser();
  if (!user) {
    window.location.href = "login.html";
    return null;
  }
  return user;
}

function ensureRole(requiredRole) {
  const user = ensureLoggedIn();
  if (!user) return null;
  const role = getCurrentMode();
  if (role !== requiredRole) {
    window.location.href = "login.html";
    return null;
  }
  return user;
}

function setMessage(elementId, text, type) {
  const el = document.getElementById(elementId);
  if (!el) {
    return;
  }
  el.textContent = text || "";
  el.className = "message" + (type ? ` ${type}` : "");
}

function statusBadge(status) {
  if (!status) {
    return "-";
  }
  return `<span class=\"badge ${status}\">${status}</span>`;
}
