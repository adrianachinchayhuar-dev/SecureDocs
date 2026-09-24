import { X } from 'lucide-react'

export default function Modal({ children, onClose }) { return <div className="modal-backdrop-custom" onClick={onClose}><div className="modal-card" onClick={(event) => event.stopPropagation()}><button className="modal-close" onClick={onClose} aria-label="Cerrar"><X size={19} /></button>{children}</div></div> }
