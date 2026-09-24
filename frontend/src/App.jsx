import { useEffect, useMemo, useState } from 'react'
import { LogOut, UserRound } from 'lucide-react'
import 'bootstrap/dist/css/bootstrap.min.css'
import './App.css'
import api, { getErrorMessage } from './services/api'
import Brand from './components/Brand'
import Sidebar from './components/Sidebar'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import Documents from './pages/Documents'
import Users from './pages/Users'
import Audit from './pages/Audit'
import Rbac from './pages/Rbac'

const PERMISSIONS = {
  ADMINISTRADOR: ['CREAR_DOCUMENTO', 'CONSULTAR_DOCUMENTO', 'MODIFICAR_DOCUMENTO', 'ELIMINAR_DOCUMENTO', 'APROBAR_DOCUMENTO', 'VER_AUDITORIA', 'GESTIONAR_USUARIOS', 'ASIGNAR_ROLES'],
  GERENTE: ['CONSULTAR_DOCUMENTO', 'MODIFICAR_DOCUMENTO', 'APROBAR_DOCUMENTO'],
  SUPERVISOR: ['CREAR_DOCUMENTO', 'CONSULTAR_DOCUMENTO', 'MODIFICAR_DOCUMENTO'],
  EMPLEADO: ['CREAR_DOCUMENTO', 'CONSULTAR_DOCUMENTO'],
  AUDITOR: ['CONSULTAR_DOCUMENTO', 'VER_AUDITORIA'],
  INVITADO: ['CONSULTAR_DOCUMENTO'],
}

function App() {
  const [usuario, setUsuario] = useState(() => JSON.parse(localStorage.getItem('usuario') || 'null'))
  const [vista, setVista] = useState('inicio')
  const [toast, setToast] = useState(null)
  const permisos = useMemo(() => PERMISSIONS[usuario?.rol] || [], [usuario])
  const puede = (permiso) => permisos.includes(permiso)

  useEffect(() => {
    if (!toast) return undefined
    const timer = setTimeout(() => setToast(null), 4500)
    return () => clearTimeout(timer)
  }, [toast])

  const mostrarError = (error) => {
    if (error.response?.status === 401) {
      localStorage.clear()
      setUsuario(null)
      setVista('inicio')
    }
    setToast({ type: 'error', text: getErrorMessage(error) })
  }

  const iniciarSesion = async (credenciales) => {
    const { data } = await api.post('/api/auth/login', credenciales)
    const sesion = { usuarioId: data.usuarioId, nombre: data.nombre, rol: data.rol }
    localStorage.setItem('token', data.token)
    localStorage.setItem('usuario', JSON.stringify(sesion))
    setUsuario(sesion)
    setToast({ type: 'success', text: `Bienvenido, ${data.nombre}` })
  }

  const cerrarSesion = () => { localStorage.clear(); setUsuario(null); setVista('inicio') }

  if (!usuario) return <Login onLogin={iniciarSesion} onError={mostrarError} toast={toast} />

  return <div className="app-shell">
    <header className="topbar"><Brand compact /><div className="session-info"><div className="avatar"><UserRound size={17} /></div><div className="session-name"><strong>{usuario.nombre}</strong><span>{usuario.rol}</span></div><button className="btn btn-outline-secondary btn-sm logout-button" onClick={cerrarSesion}><LogOut size={15} /> <span>Cerrar sesión</span></button></div></header>
    <div className="workspace"><Sidebar vista={vista} setVista={setVista} puede={puede} /><main className="main-content">
      {toast && <div className={`alert toast shadow-sm alert-${toast.type === 'success' ? 'success' : 'danger'}`}>{toast.text}</div>}
      {vista === 'inicio' && <Dashboard usuario={usuario} setVista={setVista} puede={puede} />}
      {vista === 'documentos' && <Documents puede={puede} onError={mostrarError} onSuccess={(text) => setToast({ type: 'success', text })} />}
      {vista === 'usuarios' && <Users onError={mostrarError} onSuccess={(text) => setToast({ type: 'success', text })} />}
      {vista === 'auditoria' && <Audit onError={mostrarError} />}
      {vista === 'rbac' && <Rbac onError={mostrarError} onSuccess={(text) => setToast({ type: 'success', text })} />}
    </main></div>
  </div>
}

export default App
