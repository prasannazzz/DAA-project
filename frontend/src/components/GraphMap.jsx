import { useEffect, useRef, useCallback } from 'react';
import { Network } from 'vis-network';
import { DataSet } from 'vis-data';

export default function GraphMap({ graph, highlightPath = [] }) {
  const containerRef = useRef(null);
  const networkRef = useRef(null);

  const buildNetwork = useCallback(() => {
    if (!containerRef.current) return;

    // If no graph data yet, show placeholder
    if (!graph?.nodes?.length) {
      containerRef.current.innerHTML =
        '<div style="display:flex;align-items:center;justify-content:center;height:100%;color:#52525b;font-size:0.8rem;letter-spacing:0.1em;text-transform:uppercase;font-family:Inter,sans-serif;">Loading graph…</div>';
      return;
    }

    containerRef.current.innerHTML = '';

    const pathSet = new Set(highlightPath);
    const pathEdgeSet = new Set();
    for (let i = 0; i < highlightPath.length - 1; i++) {
      pathEdgeSet.add(`${highlightPath[i]}-${highlightPath[i + 1]}`);
    }

    const nodesData = new DataSet(
      graph.nodes.map((n) => ({
        id: n.id,
        label: `${n.id}. ${n.name}`,
        // Use geo coords if available, otherwise lay out in a circle
        x: n.lng != null ? (n.lng - 77.585) * 18000 : null,
        y: n.lat != null ? -(n.lat - 12.960) * 18000 : null,
        fixed: n.lat != null && n.lng != null,
        font: {
          color: pathSet.has(n.id) ? '#000000' : '#e2e8f0',
          size: 12,
          face: 'Inter, sans-serif',
          bold: pathSet.has(n.id) ? { vadjust: 0 } : false,
        },
        color: pathSet.has(n.id)
          ? { background: '#ffffff', border: '#ffffff', highlight: { background: '#e4e4e7', border: '#ffffff' } }
          : { background: '#171717', border: '#52525b', highlight: { background: '#27272a', border: '#a1a1aa' } },
        borderWidth: pathSet.has(n.id) ? 3 : 1,
        shape: 'dot',
        size: pathSet.has(n.id) ? 20 : 14,
        shadow: pathSet.has(n.id)
          ? { enabled: true, color: 'rgba(255,255,255,0.4)', size: 12, x: 0, y: 0 }
          : false,
      }))
    );

    const edgesData = new DataSet(
      graph.edges.map((e, idx) => {
        const isHighlighted = pathEdgeSet.has(`${e.from}-${e.to}`);
        return {
          id: `edge-${idx}`,
          from: e.from,
          to: e.to,
          label: `${e.weight}m`,
          arrows: { to: { enabled: true, scaleFactor: 0.5 } },
          color: isHighlighted
            ? { color: '#ffffff', highlight: '#ffffff', opacity: 1.0 }
            : { color: '#3f3f46', highlight: '#71717a', opacity: 0.8 },
          width: isHighlighted ? 3 : 1,
          font: {
            color: isHighlighted ? '#ffffff' : '#52525b',
            size: 10,
            strokeWidth: 3,
            strokeColor: '#000000',
            face: 'Inter, sans-serif',
            align: 'middle',
          },
          smooth: { type: 'curvedCW', roundness: 0.12 },
          shadow: isHighlighted
            ? { enabled: true, color: 'rgba(255,255,255,0.3)', size: 8, x: 0, y: 0 }
            : false,
        };
      })
    );

    const options = {
      autoResize: true,
      physics: {
        enabled: !(graph.nodes[0]?.lat != null && graph.nodes[0]?.lng != null),
        solver: 'forceAtlas2Based',
        forceAtlas2Based: {
          gravitationalConstant: -50,
          centralGravity: 0.01,
          springLength: 100,
          springConstant: 0.08,
        },
        stabilization: { iterations: 150 },
      },
      interaction: {
        dragView: true,
        zoomView: true,
        hover: true,
        tooltipDelay: 100,
        navigationButtons: false,
        keyboard: false,
      },
      layout: {
        randomSeed: 42,
        improvedLayout: true,
      },
    };

    if (networkRef.current) {
      networkRef.current.destroy();
      networkRef.current = null;
    }

    networkRef.current = new Network(
      containerRef.current,
      { nodes: nodesData, edges: edgesData },
      options
    );

    networkRef.current.once('stabilizationIterationsDone', () => {
      networkRef.current.fit({ animation: { duration: 600, easingFunction: 'easeInOutQuad' } });
    });

    // If physics disabled (fixed layout), fit immediately
    if (!options.physics.enabled) {
      setTimeout(() => {
        networkRef.current?.fit({ animation: { duration: 600, easingFunction: 'easeInOutQuad' } });
      }, 100);
    }
  }, [graph, highlightPath]);

  useEffect(() => {
    buildNetwork();
    return () => {
      if (networkRef.current) {
        networkRef.current.destroy();
        networkRef.current = null;
      }
    };
  }, [buildNetwork]);

  // Re-fit on window resize
  useEffect(() => {
    const handleResize = () => networkRef.current?.fit({ animation: false });
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className="card graph-card">
      <div className="card-header">
        <span className="card-icon">🗺️</span>
        <h3>City Graph Network</h3>
        {highlightPath.length > 0 && (
          <span className="badge badge-route">Route Active</span>
        )}
      </div>
      <div
        ref={containerRef}
        className="graph-container"
        style={{ width: '100%', height: '480px', minHeight: '480px' }}
      />
      <div className="graph-legend">
        <span><span className="dot dot-default" /> Location</span>
        <span><span className="dot dot-highlight" /> Route Node</span>
        <span><span className="line line-path" /> Shortest Path</span>
      </div>
    </div>
  );
}