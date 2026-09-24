export default function Tag({ text, tone = 'blue' }) { return <span className={`badge rounded-pill tag tag-${tone}`}><span className="tag-dot" />{text || '—'}</span> }
