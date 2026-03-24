export default function OrderQueue({ orders, nodes }) {
  const nodeMap = {};
  nodes.forEach(n => { nodeMap[n.id] = n.name; });

  const statusIcon = (status) => {
    switch (status) {
      case 'PENDING': return '🟡';
      case 'ASSIGNED': return '🟢';
      case 'DELIVERED': return '✅';
      default: return '⚪';
    }
  };

  const priorityLabel = (p) => {
    const labels = { 1: 'Critical', 2: 'High', 3: 'Normal', 4: 'Low', 5: 'Lowest' };
    return labels[p] || `P${p}`;
  };

  const priorityClass = (p) => {
    if (p <= 1) return 'priority-critical';
    if (p <= 2) return 'priority-high';
    return 'priority-normal';
  };

  return (
    <div className="card">
      <div className="card-header">
        <span className="card-icon">📋</span>
        <h3>Order Queue</h3>
        <span className="badge">{orders.length}</span>
        <span className="auto-refresh-badge">⟳ 3s</span>
      </div>

      {orders.length === 0 ? (
        <p className="empty-state">No orders yet. Place one from the form above!</p>
      ) : (
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Restaurant</th>
                <th>Customer</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Time</th>
              </tr>
            </thead>
            <tbody>
              {orders.map(order => (
                <tr key={order.id} className={`order-row ${order.status?.toLowerCase()}`}>
                  <td className="order-id">{order.id?.slice(0, 8)}…</td>
                  <td>{nodeMap[order.restaurantNode] || `Node ${order.restaurantNode}`}</td>
                  <td>{nodeMap[order.customerNode] || `Node ${order.customerNode}`}</td>
                  <td>
                    <span className={`priority-badge ${priorityClass(order.priority)}`}>
                      {priorityLabel(order.priority)}
                    </span>
                  </td>
                  <td>
                    <span className={`status-pill ${order.status?.toLowerCase()}`}>
                      {statusIcon(order.status)} {order.status}
                    </span>
                  </td>
                  <td className="order-time">
                    {order.timestamp ? new Date(order.timestamp).toLocaleTimeString() : '—'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <div className="complexity-note">
        <code>PriorityQueue&lt;Order&gt;</code> — sorted by priority, then timestamp
      </div>
    </div>
  );
}
