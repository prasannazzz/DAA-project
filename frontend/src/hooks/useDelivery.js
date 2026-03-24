import { useState, useEffect, useCallback, useRef } from 'react';
import {
  fetchGraph,
  fetchOrders,
  fetchAgents,
  placeOrder as apiPlaceOrder,
  registerAgent as apiRegisterAgent,
  computeRoute as apiComputeRoute,
  assignOrder as apiAssignOrder,
} from '../services/api';

export default function useDelivery() {
  const [graph, setGraph] = useState({ nodes: [], edges: [] });
  const [orders, setOrders] = useState([]);
  const [agents, setAgents] = useState([]);
  const [route, setRoute] = useState(null);
  const [assignment, setAssignment] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const intervalRef = useRef(null);

  // ── Initial fetch ────────────────────────────────────
  const loadAll = useCallback(async () => {
    try {
      const [g, o, a] = await Promise.all([fetchGraph(), fetchOrders(), fetchAgents()]);
      setGraph(g);
      setOrders(o);
      setAgents(a);
    } catch (err) {
      setError(err.response?.data?.message || err.message);
    }
  }, []);

  useEffect(() => {
    loadAll();
  }, [loadAll]);

  // ── Auto-refresh orders & agents every 3 seconds ────
  useEffect(() => {
    intervalRef.current = setInterval(async () => {
      try {
        const [o, a] = await Promise.all([fetchOrders(), fetchAgents()]);
        setOrders(o);
        setAgents(a);
      } catch { /* silent */ }
    }, 3000);
    return () => clearInterval(intervalRef.current);
  }, []);

  // ── Actions ──────────────────────────────────────────
  const placeOrder = useCallback(async (orderData) => {
    setLoading(true);
    setError(null);
    try {
      const created = await apiPlaceOrder(orderData);
      setOrders(prev => [...prev, created]);
      return created;
    } catch (err) {
      setError(err.response?.data?.message || err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const addAgent = useCallback(async (agentData) => {
    setLoading(true);
    setError(null);
    try {
      const created = await apiRegisterAgent(agentData);
      setAgents(prev => [...prev, created]);
      return created;
    } catch (err) {
      setError(err.response?.data?.message || err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const findRoute = useCallback(async (from, to) => {
    setLoading(true);
    setError(null);
    try {
      const result = await apiComputeRoute(from, to);
      setRoute(result);
      return result;
    } catch (err) {
      setError(err.response?.data?.message || err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const triggerAssign = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await apiAssignOrder();
      setAssignment(result);
      // Refresh agents & orders
      const [o, a] = await Promise.all([fetchOrders(), fetchAgents()]);
      setOrders(o);
      setAgents(a);
      return result;
    } catch (err) {
      setError(err.response?.data?.message || err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const clearRoute = useCallback(() => setRoute(null), []);
  const clearAssignment = useCallback(() => setAssignment(null), []);
  const clearError = useCallback(() => setError(null), []);

  return {
    graph, orders, agents, route, assignment,
    loading, error,
    placeOrder, addAgent, findRoute, triggerAssign,
    clearRoute, clearAssignment, clearError, loadAll,
  };
}
