import useDelivery from '../hooks/useDelivery';
import GraphMap from '../components/GraphMap';
import OrderForm from '../components/OrderForm';
import AgentList from '../components/AgentList';
import RouteDisplay from '../components/RouteDisplay';
import OrderQueue from '../components/OrderQueue';

export default function Dashboard() {
  const {
    graph, orders, agents, route, assignment,
    loading, error,
    placeOrder, addAgent, findRoute, triggerAssign,
    clearRoute, clearAssignment, clearError,
  } = useDelivery();

  return (
    <div className="dashboard">
      {/* ── Header ───────────────────────────────────── */}
      <header className="dashboard-header">
        <div className="header-left">
          <h1>🚀 Smart Delivery</h1>
          <p className="subtitle">Food Delivery Optimization Platform</p>
        </div>
        <div className="header-stats">
          <div className="header-stat">
            <span className="stat-number">{orders.length}</span>
            <span className="stat-text">Orders</span>
          </div>
          <div className="header-stat">
            <span className="stat-number">{agents.filter(a => a.status === 'AVAILABLE').length}</span>
            <span className="stat-text">Available</span>
          </div>
          <div className="header-stat">
            <span className="stat-number">{graph.nodes?.length || 0}</span>
            <span className="stat-text">Nodes</span>
          </div>
        </div>
      </header>

      {/* ── Error Banner ─────────────────────────────── */}
      {error && (
        <div className="error-banner">
          <span>⚠️ {error}</span>
          <button onClick={clearError} className="btn-link">Dismiss</button>
        </div>
      )}

      {/* ── Assignment Result ────────────────────────── */}
      {assignment && (
        <div className="assignment-banner">
          <div className="assignment-content">
            <span className="card-icon">✅</span>
            <div>
              <strong>{assignment.agentName}</strong> assigned to order <code>{assignment.orderId?.slice(0, 8)}…</code>
              <br />
              <span className="assignment-detail">
                🏍️ Travel to restaurant: <strong>{assignment.travelTimeToRestaurant} min</strong> ·
                Total delivery: <strong>{assignment.totalDeliveryTime} min</strong>
              </span>
              {assignment.message && <p className="assignment-message">{assignment.message}</p>}
            </div>
          </div>
          <button onClick={clearAssignment} className="btn-link">Dismiss</button>
        </div>
      )}

      {/* ── Main Grid ────────────────────────────────── */}
      <div className="dashboard-grid">
        {/* Left Column — Graph */}
        <div className="col-graph">
          <GraphMap graph={graph} highlightPath={route?.path || []} />
        </div>

        {/* Right Column — Controls */}
        <div className="col-controls">
          <RouteDisplay
            nodes={graph.nodes || []}
            route={route}
            onFindRoute={findRoute}
            onClear={clearRoute}
            loading={loading}
          />
          <OrderForm nodes={graph.nodes || []} onSubmit={placeOrder} loading={loading} />
          <div className="assign-section">
            <button
              className="btn btn-accent btn-full"
              onClick={triggerAssign}
              disabled={loading}
            >
              {loading ? '⏳ Assigning…' : '⚡ Auto-Assign Next Order'}
            </button>
            <p className="algo-note">Greedy Assignment · O(A × (V+E) log V)</p>
          </div>
        </div>
      </div>

      {/* ── Bottom Row ───────────────────────────────── */}
      <div className="dashboard-bottom">
        <OrderQueue orders={orders} nodes={graph.nodes || []} />
        <AgentList agents={agents} nodes={graph.nodes || []} onRegister={addAgent} loading={loading} />
      </div>
    </div>
  );
}
