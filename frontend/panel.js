const API_URL = "http://localhost:8080";

const token = localStorage.getItem("jwt");
if (!token) {
    window.location.href = "index.html";
}

// Parsear token
function parseJwt(token) {
    const base64Url = token.split(".")[1];
    const jsonPayload = atob(base64Url);
    return JSON.parse(jsonPayload);
}

const userData = parseJwt(token);
const userPermissions = userData.authorities || [];

function hasPermission(p) {
    return userPermissions.includes(p);
}

async function secureFetch(url, method = "GET", options = {}) {
    return fetch(url, {
        method,
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        },
        body: options.body || null
    });
}

/* ---------------- PANEL SERVER INFO ---------------- */

async function loadPanel() {
    const res = await secureFetch(`${API_URL}/admin/panel`);

    const form = document.getElementById("jsonForm");

    if (!res.ok) {
        form.innerHTML = "<p>Acceso denegado</p>";
        return;
    }

    const data = await res.json();
    form.innerHTML = "";

    for (const key in data) {
        form.innerHTML += `
            <label>${key}</label>
            <input value="${data[key]}" readonly>
        `;
    }
}

/* ---------------- CRUD PRODUCTOS ---------------- */

async function loadProducts() {
    if (!hasPermission("READ_PRODUCTS")) {
        document.getElementById("products-list").innerHTML =
            "<p>No tienes permiso para ver productos</p>";
        return;
    }

    const res = await secureFetch(`${API_URL}/products`);
    const products = await res.json();

    const container = document.getElementById("products-list");
    container.innerHTML = "";

    products.forEach(p => {
        container.innerHTML += `
            <div class="product-item">
                <strong>${p.name}</strong> - $${p.price} - Stock: ${p.stock}
                ${hasPermission("UPDATE_PRODUCTS") ? `<button onclick="openEditForm(${p.id}, '${p.name}', ${p.price}, ${p.stock})">Editar</button>` : ""}
                ${hasPermission("DELETE_PRODUCTS") ? `<button onclick="deleteProduct(${p.id})">Eliminar</button>` : ""}
            </div>
        `;
    });

    if (hasPermission("CREATE_PRODUCTS")) {
        document.getElementById("createProductSection").classList.remove("hidden");
    }
}

async function createProduct() {
    const name = document.getElementById("newProductName").value;
    const price = parseFloat(document.getElementById("newProductPrice").value);
    const stock = parseInt(document.getElementById("newProductStock").value);

    await secureFetch(`${API_URL}/products`, "POST", {
        body: JSON.stringify({ name, price, stock })
    });

    loadProducts();
}

function openEditForm(id, name, price, stock) {
    document.getElementById("editProductId").value = id;
    document.getElementById("editProductName").value = name;
    document.getElementById("editProductPrice").value = price;
    document.getElementById("editProductStock").value = stock;

    document.getElementById("editProductContainer").classList.remove("hidden");
}

async function updateProduct() {
    const id = document.getElementById("editProductId").value;
    const name = document.getElementById("editProductName").value;
    const price = parseFloat(document.getElementById("editProductPrice").value);
    const stock = parseInt(document.getElementById("editProductStock").value);

    await secureFetch(`${API_URL}/products/${id}`, "PUT", {
        body: JSON.stringify({ name, price, stock })
    });

    loadProducts();
}

async function deleteProduct(id) {
    await secureFetch(`${API_URL}/products/${id}`, "DELETE");
    loadProducts();
}

/* ---------------- LOGOUT ---------------- */

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

/* ---------------- INIT ---------------- */
loadPanel();
loadProducts();
