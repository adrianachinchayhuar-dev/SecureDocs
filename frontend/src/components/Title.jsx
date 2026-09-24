export default function Title({ eyebrow, title, text, action }) {
  return <div className="page-title"><div><span className="eyebrow">{eyebrow}</span><h1>{title}</h1><p className="text-muted">{text}</p></div>{action}</div>
}
