import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

// ── Graph ──────────────────────────────────────────────
export const fetchGraph = () => API.get('/graph').then(r => r.data);

// ── Orders ─────────────────────────────────────────────
export const fetchOrders = () => API.get('/orders').then(r => r.data);
export const fetchPendingOrders = () => API.get('/orders/pending').then(r => r.data);
export const placeOrder = (order) => API.post('/orders', order).then(r => r.data);

// ── Agents ─────────────────────────────────────────────
export const fetchAgents = () => API.get('/agents').then(r => r.data);
export const fetchAvailableAgents = () => API.get('/agents/available').then(r => r.data);
export const registerAgent = (agent) => API.post('/agents', agent).then(r => r.data);

// ── Routes (Dijkstra) ──────────────────────────────────
export const computeRoute = (from, to) =>
  API.get('/route', { params: { from, to } }).then(r => r.data);

// ── Assignment (Greedy) ────────────────────────────────
export const assignOrder = () => API.post('/assign').then(r => r.data);

export default API;
