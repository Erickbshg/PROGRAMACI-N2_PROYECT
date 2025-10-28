const API_BASE_URL = "http://localhost:8080/data/Operacion"

async function calcular(operacion) {
  const numA = document.getElementById("numA").value
  const numB = document.getElementById("numB").value
  const resultDiv = document.getElementById("result")

  // Validar inputs
  if (numA === "" || numB === "") {
    mostrarResultado("Por favor ingresa ambos números", true)
    return
  }

  // Mostrar loading
  resultDiv.innerHTML = `
        <div class="result-content">  
            <div class="result-label">Calculando...</div>
            <div class="result-value loading"></div>
        </div>
    `

  try {
    const url = `${API_BASE_URL}?a=${numA}&b=${numB}&op=${operacion}`
    const response = await fetch(url)

    if (!response.ok) {
      throw new Error(`Error HTTP: ${response.status}`)
    }

    const data = await response.json()

    // Verificar si hay un error en la respuesta
    if (data.error) {
      mostrarResultado(data.error, true)
    } else if (data.resultado !== undefined) {
      mostrarResultado(data.resultado, false)
    } else {
      mostrarResultado("Respuesta inesperada del servidor", true)
    }
  } catch (error) {
    mostrarResultado(`Error: ${error.message}`, true)
  }
}

function mostrarResultado(valor, esError) {
  const resultDiv = document.getElementById("result")
  const clase = esError ? "error" : ""

  resultDiv.innerHTML = `
        <div class="result-content">
            <div class="result-label">Resultado</div>
            <div class="result-value ${clase}">${valor}</div>
        </div>
    `
}

// Permitir calcular con Enter
document.getElementById("numA").addEventListener("keypress", (e) => {
  if (e.key === "Enter") calcular("suma")
})

document.getElementById("numB").addEventListener("keypress", (e) => {
  if (e.key === "Enter") calcular("suma")
})
