document.getElementById("register-btn").addEventListener("click", async () => {
  setMessage("register-msg", "", "");

  const name = document.getElementById("name").value.trim();
  const email = document.getElementById("email").value.trim();
  const phone = document.getElementById("phone").value.trim();
  const password = document.getElementById("password").value.trim();
  const role = document.getElementById("role").value;

  if (!name || !email || !phone || !password) {
    setMessage("register-msg", "Please complete all required fields.", "error");
    return;
  }

  try {
    const response = await apiRequest("post", "/register", {
      name,
      email,
      phone,
      password,
      role
    });

    const user = unwrapData(response);
    const savedRole = user.role || role;
    saveCurrentUser({ id: user.id, name: user.name, email: user.email, phone: user.phone, role: savedRole });
    setCurrentMode(savedRole);
    setMessage("register-msg", "Register success. Redirecting to dashboard...", "success");

    window.location.href = savedRole === "DRIVER" ? "driver-dashboard.html" : "passenger-dashboard.html";
  } catch (error) {
    setMessage("register-msg", error.message || "Register failed", "error");
  }
});
