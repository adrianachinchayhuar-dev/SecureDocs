import { ClipboardCheck, FileText, LayoutDashboard, ShieldCheck, Users } from 'lucide-react'

const ITEMS = [['inicio', LayoutDashboard, 'Inicio'], ['documentos', FileText, 'Documentos', 'CONSULTAR_DOCUMENTO'], ['usuarios', Users, 'Usuarios', 'GESTIONAR_USUARIOS'], ['auditoria', ClipboardCheck, 'Auditoría', 'VER_AUDITORIA'], ['rbac', ShieldCheck, 'Roles y permisos', 'ASIGNAR_ROLES']]

export default function Sidebar({ vista, setVista, puede }) {
  return <aside className="sidebar"><div className="sidebar-brand"><div className="sidebar-shield"><ShieldCheck size={19} /></div><div><strong>SecureDocs</strong><small>Gestión documental segura</small></div></div><div className="sidebar-label">NAVEGACIÓN</div>{ITEMS.filter((item) => !item[3] || puede(item[3])).map(([id, Icon, label]) => <button key={id} className={`nav-item ${vista === id ? 'active' : ''}`} onClick={() => setVista(id)}><Icon size={18} strokeWidth={1.9} />{label}</button>)}<div className="sidebar-footer"><ShieldCheck size={17} /><div><strong>Acceso protegido</strong><small>RBAC + ABAC activo</small></div></div></aside>
}
