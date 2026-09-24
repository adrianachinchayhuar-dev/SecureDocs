import { FileLock2 } from 'lucide-react'

export default function Brand({ compact = false }) {
  return <div className={`brand ${compact ? 'brand-compact' : ''}`}><span className="brand-mark"><FileLock2 size={20} /></span><div><strong>SecureDocs</strong><small>Gestión documental segura</small></div></div>
}
