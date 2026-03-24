import { useState } from 'react';

export default function OrderForm({ nodes, onSubmit, loading }) {
  const [form, setForm] = useState({ restaurantNode: '', customerNode: '', priority: '3' });
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    setSuccess('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const result = await onSubmit({
        restaurantNode: parseInt(form.restaurantNode),
        customerNode: parseInt(form.customerNode),
        priority: parseInt(form.priority),
      });
      setSuccess(`Order ${result.id} placed!`);
      setForm({ restaurantNode: '', customerNode: '', priority: '3' });
    } catch { /* error handled in hook */ }
  };

  const isValid = form.restaurantNode && form.customerNode && form.restaurantNode !== form.customerNode;

  return (
    <div className="card">
      <div className="card-header">
        <span className="card-icon">🍔</span>
        <h3>Place Order</h3>
      </div>
      <form onSubmit={handleSubmit} className="form-grid">
        <div className="form-group">
          <label htmlFor="order-restaurant">Restaurant</label>
          <select
            id="order-restaurant"
            name="restaurantNode"
            value={form.restaurantNode}
            onChange={handleChange}
            required
          >
            <option value="">Select restaurant…</option>
            {nodes.map(n => (
              <option key={n.id} value={n.id}>{n.id}. {n.name}</option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="order-customer">Customer</label>
          <select
            id="order-customer"
            name="customerNode"
            value={form.customerNode}
            onChange={handleChange}
            required
          >
            <option value="">Select customer…</option>
            {nodes.map(n => (
              <option key={n.id} value={n.id}>{n.id}. {n.name}</option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="order-priority">Priority (1=highest)</label>
          <select
            id="order-priority"
            name="priority"
            value={form.priority}
            onChange={handleChange}
          >
            {[1, 2, 3, 4, 5].map(p => (
              <option key={p} value={p}>{p} — {['Critical', 'High', 'Normal', 'Low', 'Lowest'][p - 1]}</option>
            ))}
          </select>
        </div>
        <button type="submit" className="btn btn-primary" disabled={!isValid || loading}>
          {loading ? 'Placing…' : '📦 Place Order'}
        </button>
        {success && <p className="form-success">{success}</p>}
      </form>
    </div>
  );
}
