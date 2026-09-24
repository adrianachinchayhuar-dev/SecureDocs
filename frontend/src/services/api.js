import axios from 'axios'

const api = axios.create({ baseURL: 'http://localhost:8081', headers: { 'Content-Type': 'application/json' } })
api.interceptors.request.use((config) => { const token = localStorage.getItem('token'); if (token) config.headers.Authorization = `Bearer ${token}`; return config })

export function getErrorMessage(error) {
  if (!error.response) return 'No se pudo conectar con el servidor. Verifica que el backend esté ejecutándose.'
  const { status, data } = error.response
  if (status === 401) return 'Tu sesión expiró o el token no es válido. Inicia sesión nuevamente.'
  if (status === 403) return data?.mensaje?.startsWith('ABAC:') ? `Operación rechazada por una política de acceso: ${data.mensaje.slice(6)}` : 'No tienes permisos suficientes para realizar esta acción.'
  if (status === 404) return 'El recurso solicitado no existe o fue eliminado.'
  if (status === 400) return data?.detalles?.length ? data.detalles.join('. ') : (data?.mensaje || 'Revisa los datos ingresados.')
  if (status >= 500) return 'El servidor tuvo un problema. Intenta nuevamente en unos instantes.'
  return data?.mensaje || 'La operación no pudo completarse.'
}
export default api
