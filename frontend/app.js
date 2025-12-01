// --------------------------------------------
// CONFIG
// --------------------------------------------
const API_URL = "http://localhost:8080";

// --------------------------------------------
// MANEJO DE TABS (LOGIN / REGISTER)
// --------------------------------------------
document.getElementById("btnShowLogin").onclick = () => switchTab("login");
document.getElementById("btnShowRegister").onclick = () => switchTab("register");

function switchTab(tab){
    const loginCard = document.getElementById("loginCard");
    const registerCard = document.getElementById("registerCard");

    const btnLogin = document.getElementById("btnShowLogin");
    const btnRegister = document.getElementById("btnShowRegister");

    if(tab === "login"){
        loginCard.classList.remove("hidden");
        registerCard.classList.add("hidden");
        btnLogin.classList.add("active");
        btnRegister.classList.remove("active");
    } else {
        loginCard.classList.add("hidden");
        registerCard.classList.remove("hidden");
        btnLogin.classList.remove("active");
        btnRegister.classList.add("active");
    }
}

// --------------------------------------------
// LOGIN
// --------------------------------------------
document.getElementById("loginBtn").onclick = login;

async function login() {
    const username = document.getElementById("logUser").value;
    const password = document.getElementById("logPass").value;

    const res = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    if (!res.ok) {
        document.getElementById("loginMsg").innerText = "Credenciales incorrectas";
        return;
    }

    const token = await res.text();

    // Guardamos con la clave CORRECTA
    localStorage.setItem("jwt", token);

    window.location.href = "panel.html";
}

// --------------------------------------------
// REGISTRO
// --------------------------------------------
document.getElementById("registerBtn").onclick = register;

async function register() {
    const username = document.getElementById("regUser").value;
    const password = document.getElementById("regPass").value;
    const role = document.getElementById("regRole").value;

    const res = await fetch(`${API_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password, role })
    });

    document.getElementById("regMsg").innerText = 
        res.ok ? "Usuario registrado" : "Error al registrar";
}
