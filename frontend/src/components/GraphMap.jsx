import { useEffect, useRef, useCallback } from 'react';
import { Network } from 'vis-network';

export default function GraphMap({ graph, highlightPath = [] }) {
  const containerRef = useRef(null);
  const networkRef = useRef(null);

  const buildNetwork = useCallback(() => {
    if (!containerRef.current || !graph.nodes?.length) return;

    // Build node set
    const nodes = graph.nodes.map((n) => ({
      id: n.id,
      label: `${n.id}. ${n.name}`,
      x: (n.lng - 77.585) * 18000,
      y: -(n.lat - 12.960) * 18000,
      fixed: true,
      font: { color: '#e2e8f0', size: 13, face: 'Inter, sans-serif' },
      color: highlightPath.includes(n.id)
        ? { background: '#ffffff', border: '#ffffff', highlight: { background: '#ffffff', border: '#e4e4e7' } }
        : { background: '#171717', border: '#52525b', highlight: { background: '#27272a', border: '#a1a1aa' } },
      borderWidth: highlightPath.includes(n.id) ? 3 : 1,
      shape: 'dot',
      size: highlightPath.includes(n.id) ? 22 : 16,
      shadow: highlightPath.includes(n.id),
    }));

    // Build edge set
    const pathEdgeSet = new Set();
    for (let i = 0; i < highlightPath.length - 1; i++) {
      pathEdgeSet.add(`${highlightPath[i]}-${highlightPath[i + 1]}`);
    }

    const edges = graph.edges.map((e, idx) => {
      const isHighlighted = pathEdgeSet.has(`${e.from}-${e.to}`);
      return {
        id: `edge-${idx}`,
        from: e.from,
        to: e.to,
        label: `${e.weight} min`,
        arrows: { to: { enabled: true, scaleFactor: 0.6 } },
        color: isHighlighted
          ? { color: '#ffffff', highlight: '#ffffff' }
          : { color: '#3f3f46', highlight: '#71717a' },
        width: isHighlighted ? 3 : 1,
        font: {
          color: isHighlighted ? '#ffffff' : '#71717a',
          size: 11,
          strokeWidth: 2,
          strokeColor: '#000000',
          face: 'Inter, sans-serif',
        },
        smooth: { type: 'curvedCW', roundness: 0.15 },
        shadow: isHighlighted,
      };
    });

    const options = {
      autoResize: true,
      physics: false,
      interaction: {
        dragView: true,
        zoomView: true,
        hover: true,
        tooltipDelay: 100,
      },
      layout: { randomSeed: 42 },
    };

    if (networkRef.current) {
      networkRef.current.destroy();
    }
    networkRef.current = new Network(containerRef.current, { nodes, edges }, options);
    networkRef.current.fit({ animation: { duration: 400, easingFunction: 'easeInOutQuad' } });
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

  return (
    <div className="card graph-card">
      <div className="card-header">
        <span className="card-icon">🗺️</span>
        <h3>City Graph Network</h3>
        {highlightPath.length > 0 && (
          <span className="badge badge-route">Route Active</span>
        )}
      </div>
      <div ref={containerRef} className="graph-container" />
      <div className="graph-legend">
        <span><span className="dot dot-default" /> Location</span>
        <span><span className="dot dot-highlight" /> Route Node</span>
        <span><span className="line line-path" /> Shortest Path</span>
      </div>
    </div>
  );
}
