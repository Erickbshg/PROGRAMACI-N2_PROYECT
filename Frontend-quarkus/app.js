// API Base URL
const API_URL = "http://localhost:8080"

// Estado de la aplicación
let editMode = false
let currentEditId = null

// Inicialización
document.addEventListener("DOMContentLoaded", () => {
  setupTabs()
  loadClientes()
  loadProductos()
})

// Configuración de tabs
function setupTabs() {
  const tabButtons = document.querySelectorAll(".tab-btn")

  tabButtons.forEach((button) => {
    button.addEventListener("click", () => {
      const tabName = button.getAttribute("data-tab")

      // Remover clase active de todos los botones y contenidos
      tabButtons.forEach((btn) => btn.classList.remove("active"))
      document.querySelectorAll(".tab-content").forEach((content) => {
        content.classList.remove("active")
      })

      // Agregar clase active al botón y contenido seleccionado
      button.classList.add("active")
      document.getElementById(`${tabName}-section`).classList.add("active")
    })
  })
}

// ==================== CLIENTES ====================

// Cargar clientes
async function loadClientes() {
  try {
    const response = await fetch(`${API_URL}/clientes`)

    if (!response.ok) {
      throw new Error("Error al cargar clientes")
    }

    const clientes = await response.json()
    displayClientes(clientes)
  } catch (error) {
    console.error("Error:", error)
    document.getElementById("clientes-table").innerHTML = `
            <tr>
                <td colspan="6" class="loading">Error al cargar clientes. Verifica que el servidor esté corriendo.</td>
            </tr>
        `
  }
}

// Mostrar clientes en la tabla
function displayClientes(clientes) {
  const tbody = document.getElementById("clientes-table")

  if (clientes.length === 0) {
    tbody.innerHTML = `
            <tr>
                <td colspan="6" class="loading">No hay clientes registrados</td>
            </tr>
        `
    return
  }

  tbody.innerHTML = clientes
    .map(
      (cliente) => `
        <tr>
            <td>${cliente.idCliente}</td>
            <td>${cliente.nombre}</td>
            <td>${cliente.direccion}</td>
            <td>${cliente.telefono}</td>
            <td>${cliente.correo}</td>
            <td>
                <button class="btn btn-edit" onclick="editCliente(${cliente.idCliente})">Editar</button>
                <button class="btn btn-delete" onclick="deleteCliente(${cliente.idCliente})">Eliminar</button>
            </td>
        </tr>
    `,
    )
    .join("")
}

// Abrir modal de cliente
function openClientModal() {
  editMode = false
  currentEditId = null
  document.getElementById("client-modal-title").textContent = "Agregar Cliente"
  document.getElementById("client-form").reset()
  document.getElementById("client-id").value = ""
  document.getElementById("client-modal").classList.add("active")
}

// Cerrar modal de cliente
function closeClientModal() {
  document.getElementById("client-modal").classList.remove("active")
  document.getElementById("client-form").reset()
}

// Editar cliente
async function editCliente(id) {
  try {
    const response = await fetch(`${API_URL}/clientes`)
    const clientes = await response.json()
    const cliente = clientes.find((c) => c.idCliente === id)

    if (cliente) {
      editMode = true
      currentEditId = id
      document.getElementById("client-modal-title").textContent = "Editar Cliente"
      document.getElementById("client-id").value = cliente.idCliente
      document.getElementById("client-nombre").value = cliente.nombre
      document.getElementById("client-direccion").value = cliente.direccion
      document.getElementById("client-telefono").value = cliente.telefono
      document.getElementById("client-correo").value = cliente.correo
      document.getElementById("client-modal").classList.add("active")
    }
  } catch (error) {
    console.error("Error:", error)
    alert("Error al cargar los datos del cliente")
  }
}

// Guardar cliente
async function saveClient(event) {
  event.preventDefault()

  const cliente = {
    nombre: document.getElementById("client-nombre").value,
    direccion: document.getElementById("client-direccion").value,
    telefono: document.getElementById("client-telefono").value,
    correo: document.getElementById("client-correo").value,
  }

  try {
    let response

    if (editMode && currentEditId) {
      // Actualizar cliente existente
      response = await fetch(`${API_URL}/clientes/${currentEditId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(cliente),
      })
    } else {
      // Crear nuevo cliente
      response = await fetch(`${API_URL}/clientes`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(cliente),
      })
    }

    if (!response.ok) {
      throw new Error("Error al guardar el cliente")
    }

    closeClientModal()
    loadClientes()
    alert(editMode ? "Cliente actualizado exitosamente" : "Cliente creado exitosamente")
  } catch (error) {
    console.error("Error:", error)
    alert("Error al guardar el cliente")
  }
}

// Eliminar cliente
async function deleteCliente(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar este cliente?")) {
    return
  }

  try {
    const response = await fetch(`${API_URL}/clientes/${id}`, {
      method: "DELETE",
    })

    if (!response.ok) {
      throw new Error("Error al eliminar el cliente")
    }

    loadClientes()
    alert("Cliente eliminado exitosamente")
  } catch (error) {
    console.error("Error:", error)
    alert("Error al eliminar el cliente")
  }
}

// ==================== PRODUCTOS ====================

// Cargar productos
async function loadProductos() {
  try {
    const response = await fetch(`${API_URL}/productos`)

    if (!response.ok) {
      throw new Error("Error al cargar productos")
    }

    const productos = await response.json()
    displayProductos(productos)
  } catch (error) {
    console.error("Error:", error)
    document.getElementById("productos-table").innerHTML = `
            <tr>
                <td colspan="5" class="loading">Error al cargar productos. Verifica que el servidor esté corriendo.</td>
            </tr>
        `
  }
}

// Mostrar productos en la tabla
function displayProductos(productos) {
  const tbody = document.getElementById("productos-table")

  if (productos.length === 0) {
    tbody.innerHTML = `
            <tr>
                <td colspan="5" class="loading">No hay productos registrados</td>
            </tr>
        `
    return
  }

  tbody.innerHTML = productos
    .map(
      (producto) => `
        <tr>
            <td>${producto.id}</td>
            <td>${producto.nombre}</td>
            <td>${producto.descripcion}</td>
            <td>$${Number.parseFloat(producto.precio).toFixed(2)}</td>
            <td>
                <button class="btn btn-edit" onclick="editProducto(${producto.id})">Editar</button>
                <button class="btn btn-delete" onclick="deleteProducto(${producto.id})">Eliminar</button>
            </td>
        </tr>
    `,
    )
    .join("")
}

// Abrir modal de producto
function openProductModal() {
  editMode = false
  currentEditId = null
  document.getElementById("product-modal-title").textContent = "Agregar Producto"
  document.getElementById("product-form").reset()
  document.getElementById("product-id").value = ""
  document.getElementById("product-modal").classList.add("active")
}

// Cerrar modal de producto
function closeProductModal() {
  document.getElementById("product-modal").classList.remove("active")
  document.getElementById("product-form").reset()
}

// Editar producto
async function editProducto(id) {
  try {
    const response = await fetch(`${API_URL}/productos`)
    const productos = await response.json()
    const producto = productos.find((p) => p.id === id)

    if (producto) {
      editMode = true
      currentEditId = id
      document.getElementById("product-modal-title").textContent = "Editar Producto"
      document.getElementById("product-id").value = producto.id
      document.getElementById("product-nombre").value = producto.nombre
      document.getElementById("product-descripcion").value = producto.descripcion
      document.getElementById("product-precio").value = producto.precio
      document.getElementById("product-modal").classList.add("active")
    }
  } catch (error) {
    console.error("Error:", error)
    alert("Error al cargar los datos del producto")
  }
}

// Guardar producto
async function saveProduct(event) {
  event.preventDefault()

  const producto = {
    nombre: document.getElementById("product-nombre").value,
    descripcion: document.getElementById("product-descripcion").value,
    precio: Number.parseFloat(document.getElementById("product-precio").value),
  }

  try {
    let response

    if (editMode && currentEditId) {
      // Actualizar producto existente
      response = await fetch(`${API_URL}/productos/${currentEditId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(producto),
      })
    } else {
      // Crear nuevo producto
      response = await fetch(`${API_URL}/productos`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(producto),
      })
    }

    if (!response.ok) {
      throw new Error("Error al guardar el producto")
    }

    closeProductModal()
    loadProductos()
    alert(editMode ? "Producto actualizado exitosamente" : "Producto creado exitosamente")
  } catch (error) {
    console.error("Error:", error)
    alert("Error al guardar el producto")
  }
}

// Eliminar producto
async function deleteProducto(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar este producto?")) {
    return
  }

  try {
    const response = await fetch(`${API_URL}/productos/${id}`, {
      method: "DELETE",
    })

    if (!response.ok) {
      throw new Error("Error al eliminar el producto")
    }

    loadProductos()
    alert("Producto eliminado exitosamente")
  } catch (error) {
    console.error("Error:", error)
    alert("Error al eliminar el producto")
  }
}
