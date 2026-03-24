import { useState } from 'react';

export default function RouteDisplay({ nodes, route, onFindRoute, onClear, loading }) {
  const [from, setFrom] = useState('');
  const [to, setTo] = useState('');

  const handleFind = async (e) => {
    e.preventDefault();
    await onFindRoute(parseInt(from), parseInt(to));
  };

  return (
    <div className="card">
      <div className="card-header">
        <span className="card-icon">🛤️</span>
        <h3>Route Finder</h3>
        <span className="algo-badge">Dijkstra's Algorithm</span>
      </div>

      <form onSubmit={handleFind} className="form-grid">
        <div className="form-group">
          <label htmlFor="route-from">From</label>
          <select id="route-from" value={from} onChange={e => setFrom(e.target.value)} required>
            <option value="">Select origin…</option>
            {nodes.map(n => (
              <option key={n.id} value={n.id}>{n.id}. {n.name}</option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="route-to">To</label>
          <select id="route-to" value={to} onChange={e => setTo(e.target.value)} required>
            <option value="">Select destination…</option>
            {nodes.map(n => (
              <option key={n.id} value={n.id}>{n.id}. {n.name}</option>
            ))}
          </select>
        </div>
        <div className="btn-row">
          <button type="submit" className="btn btn-primary" disabled={!from || !to || from === to || loading}>
            {loading ? 'Computing…' : '🔍 Find Route'}
          </button>
          {route && (
            <button type="button" className="btn btn-ghost" onClick={() => { onClear(); setFrom(''); setTo(''); }}>
              ✖ Clear
            </button>
          )}
        </div>
      </form>

      {route && (
        <div className="route-result">
          <div className="route-stats">
            <div className="stat">
              <span className="stat-value">{route.totalDistance}</span>
              <span className="stat-label">min total</span>
            </div>
            <div className="stat">
              <span className="stat-value">{route.path?.length || 0}</span>
              <span className="stat-label">hops</span>
            </div>
          </div>
          <div className="route-path">
            {route.pathNames?.map((name, i) => (
              <span key={i} className="path-node">
                {i > 0 && <span className="path-arrow">→</span>}
                <span className="path-label">{name}</span>
              </span>
            ))}
          </div>
          <div className="complexity-note">
            <code>O((V + E) log V)</code> · Priority Queue (Min-Heap)
          </div>
        </div>
      )}
    </div>
  );
}
