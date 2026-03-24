import { useState } from 'react';

export default function AgentList({ agents, nodes, onRegister, loading }) {
  const [form, setForm] = useState({ name: '', currentNode: '' });
  const [showForm, setShowForm] = useState(false);
  const [success, setSuccess] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const result = await onRegister({
        name: form.name,
        currentNode: parseInt(form.currentNode),
      });
      setSuccess(`Agent ${result.name} registered!`);
      setForm({ name: '', currentNode: '' });
      setShowForm(false);
    } catch { /* handled in hook */ }
  };

  const nodeMap = {};
  nodes.forEach(n => { nodeMap[n.id] = n.name; });

  return (
    <div className="card">
      <div className="card-header">
        <span className="card-icon">🚴</span>
        <h3>Delivery Agents</h3>
        <span className="badge">{agents.length}</span>
      </div>

      <div className="agent-grid">
        {agents.length === 0 && <p className="empty-state">No agents registered yet.</p>}
        {agents.map(agent => (
          <div key={agent.id} className={`agent-card ${agent.status === 'AVAILABLE' ? 'available' : 'busy'}`}>
            <div className="agent-avatar">
              {agent.name?.charAt(0).toUpperCase()}
            </div>
            <div className="agent-info">
              <span className="agent-name">{agent.name}</span>
              <span className="agent-location">📍 {nodeMap[agent.currentNode] || `Node ${agent.currentNode}`}</span>
            </div>
            <span className={`status-badge ${agent.status === 'AVAILABLE' ? 'status-available' : 'status-busy'}`}>
              {agent.status}
            </span>
          </div>
        ))}
      </div>

      <button className="btn btn-secondary" onClick={() => setShowForm(!showForm)}>
        {showForm ? '✖ Cancel' : '➕ Add Agent'}
      </button>

      {showForm && (
        <form onSubmit={handleSubmit} className="form-grid" style={{ marginTop: '1rem' }}>
          <div className="form-group">
            <label htmlFor="agent-name">Agent Name</label>
            <input
              id="agent-name"
              type="text"
              value={form.name}
              onChange={e => setForm(f => ({ ...f, name: e.target.value }))}
              placeholder="e.g. John Doe"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="agent-node">Starting Location</label>
            <select
              id="agent-node"
              value={form.currentNode}
              onChange={e => setForm(f => ({ ...f, currentNode: e.target.value }))}
              required
            >
              <option value="">Select location…</option>
              {nodes.map(n => (
                <option key={n.id} value={n.id}>{n.id}. {n.name}</option>
              ))}
            </select>
          </div>
          <button type="submit" className="btn btn-primary" disabled={!form.name || !form.currentNode || loading}>
            {loading ? 'Registering…' : '✅ Register'}
          </button>
          {success && <p className="form-success">{success}</p>}
        </form>
      )}
    </div>
  );
}
