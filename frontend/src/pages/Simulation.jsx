import { useState, useEffect, useRef, useCallback } from 'react';
import useDelivery from '../hooks/useDelivery';
import GraphMap from '../components/GraphMap';

const SIM_STATES = { IDLE: 'IDLE', RUNNING: 'RUNNING', PAUSED: 'PAUSED', DONE: 'DONE' };

export default function Simulation() {
  const {
    graph, orders, agents,
    placeOrder, triggerAssign, findRoute,
    loading, error, clearError,
  } = useDelivery();

  const [simState, setSimState] = useState(SIM_STATES.IDLE);
  const [logs, setLogs] = useState([]);
  const [currentPath, setCurrentPath] = useState([]);
  const [speed, setSpeed] = useState(1500);
  const intervalRef = useRef(null);
  const logsEndRef = useRef(null);

  const log = useCallback((msg, type = 'info') => {
    setLogs(prev => [...prev, { time: new Date().toLocaleTimeString(), msg, type }]);
  }, []);

  // Auto-scroll logs
  useEffect(() => {
    logsEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [logs]);

  const runSimulation = useCallback(async () => {
    setSimState(SIM_STATES.RUNNING);
    setLogs([]);
    setCurrentPath([]);
    log('🚀 Simulation started');

    // Step 1: Place random orders
    const nodeIds = graph.nodes.map(n => n.id);
    const randomOrders = [];
    for (let i = 0; i < 3; i++) {
      const rest = nodeIds[Math.floor(Math.random() * nodeIds.length)];
      let cust = rest;
      while (cust === rest) cust = nodeIds[Math.floor(Math.random() * nodeIds.length)];
      randomOrders.push({ restaurantNode: rest, customerNode: cust, priority: Math.floor(Math.random() * 3) + 1 });
    }

    for (const [i, o] of randomOrders.entries()) {
      const restName = graph.nodes.find(n => n.id === o.restaurantNode)?.name;
      const custName = graph.nodes.find(n => n.id === o.customerNode)?.name;
      try {
        await placeOrder(o);
        log(`📦 Order ${i + 1}: ${restName} → ${custName} (P${o.priority})`, 'order');
      } catch {
        log(`❌ Failed to place order ${i + 1}`, 'error');
      }
      await new Promise(r => setTimeout(r, speed / 2));
    }

    log('✅ All orders placed. Starting assignments…');
    await new Promise(r => setTimeout(r, speed));

    // Step 2: Assign orders
    for (let i = 0; i < randomOrders.length; i++) {
      try {
        const result = await triggerAssign();
        if (result && result.agentName) {
          log(`🚴 ${result.agentName} → Order ${result.orderId?.slice(0, 8)}…`, 'assign');
          log(`  └ Travel: ${result.travelTimeToRestaurant}min · Total: ${result.totalDeliveryTime}min`, 'detail');
          if (result.routeToRestaurant) {
            setCurrentPath(result.routeToRestaurant);
            await new Promise(r => setTimeout(r, speed));
          }
          if (result.routeToCustomer) {
            setCurrentPath(result.routeToCustomer);
            await new Promise(r => setTimeout(r, speed));
          }
        } else {
          log('⏸️ No more pending orders or available agents.', 'warn');
          break;
        }
      } catch {
        log('⚠️ Assignment failed — no pending orders or agents.', 'warn');
        break;
      }
      await new Promise(r => setTimeout(r, speed / 2));
    }

    setCurrentPath([]);
    log('🏁 Simulation complete!', 'done');
    setSimState(SIM_STATES.DONE);
  }, [graph, placeOrder, triggerAssign, log, speed]);

  const stopSimulation = () => {
    clearInterval(intervalRef.current);
    setSimState(SIM_STATES.IDLE);
    setCurrentPath([]);
    log('🛑 Simulation stopped.');
  };

  return (
    <div className="simulation-page">
      <header className="dashboard-header">
        <div className="header-left">
          <h1>🎬 Live Simulation</h1>
          <p className="subtitle">Watch the delivery optimization algorithms in action</p>
        </div>
        <div className="sim-controls">
          <label className="speed-label">
            Speed
            <select value={speed} onChange={e => setSpeed(Number(e.target.value))} className="speed-select">
              <option value={2500}>0.5x</option>
              <option value={1500}>1x</option>
              <option value={800}>2x</option>
              <option value={400}>4x</option>
            </select>
          </label>
          {simState === SIM_STATES.IDLE || simState === SIM_STATES.DONE ? (
            <button className="btn btn-accent" onClick={runSimulation} disabled={loading || !graph.nodes?.length}>
              ▶ Run Simulation
            </button>
          ) : (
            <button className="btn btn-danger" onClick={stopSimulation}>
              ■ Stop
            </button>
          )}
        </div>
      </header>

      {error && (
        <div className="error-banner">
          <span>⚠️ {error}</span>
          <button onClick={clearError} className="btn-link">Dismiss</button>
        </div>
      )}

      <div className="sim-grid">
        {/* Graph View */}
        <div className="sim-graph">
          <GraphMap graph={graph} highlightPath={currentPath} />
        </div>

        {/* Live Log */}
        <div className="card sim-log-card">
          <div className="card-header">
            <span className="card-icon">📜</span>
            <h3>Simulation Log</h3>
            {simState === SIM_STATES.RUNNING && (
              <span className="badge badge-live">● LIVE</span>
            )}
          </div>
          <div className="sim-log">
            {logs.length === 0 && <p className="empty-state">Press "Run Simulation" to begin.</p>}
            {logs.map((entry, i) => (
              <div key={i} className={`log-entry log-${entry.type}`}>
                <span className="log-time">{entry.time}</span>
                <span className="log-msg">{entry.msg}</span>
              </div>
            ))}
            <div ref={logsEndRef} />
          </div>
        </div>

        {/* Algorithm Info */}
        <div className="card sim-algo-card">
          <div className="card-header">
            <span className="card-icon">🧠</span>
            <h3>Algorithms Used</h3>
          </div>
          <div className="algo-list">
            <div className="algo-item">
              <h4>Dijkstra's Shortest Path</h4>
              <p>Finds optimal routes using a min-heap priority queue.</p>
              <code>O((V + E) log V)</code>
            </div>
            <div className="algo-item">
              <h4>Greedy Agent Assignment</h4>
              <p>Selects the closest available agent for each order.</p>
              <code>O(A × (V + E) log V)</code>
            </div>
            <div className="algo-item">
              <h4>Priority Queue Scheduler</h4>
              <p>Orders processed by priority then timestamp.</p>
              <code>O(log n) insert/extract</code>
            </div>
            <div className="algo-item">
              <h4>0/1 Knapsack (DP)</h4>
              <p>Batch optimizer for order grouping.</p>
              <code>O(n × W)</code>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
