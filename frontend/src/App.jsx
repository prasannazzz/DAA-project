import { BrowserRouter, Routes, Route, NavLink } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Simulation from './pages/Simulation';
import './index.css';

export default function App() {
  return (
    <BrowserRouter>
      {/* ── Navigation ─────────────────────────────── */}
      <nav className="nav-bar">
        <span className="nav-brand">🚀 SmartDelivery</span>
        <div className="nav-links">
          <NavLink to="/" end className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            📊 Dashboard
          </NavLink>
          <NavLink to="/simulation" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
            🎬 Simulation
          </NavLink>
        </div>
      </nav>

      {/* ── Routes ─────────────────────────────────── */}
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/simulation" element={<Simulation />} />
      </Routes>
    </BrowserRouter>
  );
}
