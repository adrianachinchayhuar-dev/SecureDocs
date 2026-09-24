/* eslint-disable react-hooks/set-state-in-effect, react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import api from '../services/api'
import Title from '../components/Title'
import Loading from '../components/Loading'

export default function Rbac({ onError, onSuccess }) { const [roles, setRoles] = useState([]); const [permisos, setPermisos] = useState([]); const [selected, setSelected] = useState(''); const [loading, setLoading] = useState(true)
  const load = async () => { try { const [rolesResponse, permissionsResponse] = await Promise.all([api.get('/api/rbac/roles'), api.get('/api/rbac/permisos')]); setRoles(rolesResponse.data); setPermisos(permissionsResponse.data); if (!selected && rolesResponse.data[0]) setSelected(rolesResponse.data[0].id) } catch (error) { onError(error) } finally { setLoading(false) } }
  useEffect(() => { load() }, [])
  const role = roles.find((item) => String(item.id) === String(selected)); const assigned = role?.permisos?.map((item) => item.permiso?.id) || []
  const toggle = async (permission) => { try { if (assigned.includes(permission.id)) await api.delete(`/api/rbac/roles/${selected}/permisos/${permission.id}`); else await api.post(`/api/rbac/roles/${selected}/permisos/${permission.id}`); await load(); onSuccess('Permisos actualizados.') } catch (error) { onError(error) } }
  if (loading) return <Loading />
  return <div><Title eyebrow="CONTROL DE ACCESO" title="Roles y permisos" text="Consulta y ajusta la matriz RBAC. El backend mantiene la autorización real." /><div className="card rbac-card"><div className="role-list"><span className="eyebrow">ROLES</span>{roles.map((item) => <button className={`role-item ${String(selected) === String(item.id) ? 'active' : ''}`} key={item.id} onClick={() => setSelected(item.id)}>{item.nombre}<small>{item.permisos?.length || 0} permisos</small></button>)}</div><div className="permission-list"><span className="eyebrow">PERMISOS DE {role?.nombre || 'ROL'}</span>{permisos.map((permission) => <button className={`permission-item ${assigned.includes(permission.id) ? 'checked' : ''}`} key={permission.id} onClick={() => toggle(permission)}><b>{assigned.includes(permission.id) ? '✓' : '○'}</b><span><strong>{permission.nombre}</strong><small>{permission.descripcion}</small></span></button>)}</div></div></div>
}
