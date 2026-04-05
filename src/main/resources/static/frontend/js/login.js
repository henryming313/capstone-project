document.getElementById("login-btn").addEventListener("click", async () => {
  setMessage("login-msg", "", "");

  const identifier = document.getElementById("identifier").value.trim();
  const password = document.getElementById("password").value.trim();

  if (!identifier || !password) {
    setMessage("login-msg", "Please fill identifier and password.", "error");
    return;
  }

  try {
    const response = await apiRequest("post", "/login", { identifier, password });
    if (!response || !response.id) {
      throw new Error("Unexpected login response");
    }

    const role = response.role || "RIDER";

    saveCurrentUser({
      id: response.id,
      name: response.name,
      email: response.email,
      phone: response.phone,
      role: role
    });
    setCurrentMode(role);

    setMessage("login-msg", "Login success. Redirecting...", "success");
    window.location.href = role === "DRIVER" ? "driver-dashboard.html" : "passenger-dashboard.html";
  } catch (error) {
    setMessage("login-msg", error.message || "Login failed", "error");
  }
});
