/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import api from '../services/api'
import Title from '../components/Title'
import Loading from '../components/Loading'
import Empty from '../components/Empty'
import Tag from '../components/Tag'

export default function Audit({ onError }) { const [rows, setRows] = useState([]); const [loading, setLoading] = useState(true); useEffect(() => { api.get('/api/auditorias').then(({ data }) => setRows(data)).catch(onError).finally(() => setLoading(false)) }, [])
  return <div><Title eyebrow="TRAZABILIDAD" title="Auditoría" text="Registro de operaciones y decisiones de autorización." /><div className="card table-card">{loading ? <Loading /> : rows.length === 0 ? <Empty text="Aún no hay registros de auditoría." /> : <div className="table-responsive"><table className="table align-middle mb-0"><thead><tr><th>FECHA</th><th>USUARIO</th><th>RECURSO</th><th>ACCIÓN</th><th>RESULTADO</th><th>MOTIVO</th></tr></thead><tbody>{rows.map((row) => <tr key={row.id}><td>{row.fecha ? new Date(row.fecha).toLocaleString() : '—'}</td><td>{row.usuario?.nombre || 'Sistema'}</td><td>{row.recurso}</td><td>{row.accion}</td><td><Tag text={row.resultado} tone={row.resultado === 'PERMITIDO' ? 'green' : 'red'} /></td><td>{row.motivo || '—'}</td></tr>)}</tbody></table></div>}</div></div>
}
