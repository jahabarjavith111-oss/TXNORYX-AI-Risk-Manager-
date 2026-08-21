function Skeleton({ width = "100%", height = 14, style }) {
  return <div className="skeleton" style={{ width, height, ...style }} />;
}

export function TableSkeleton({ rows = 6, cols = 6 }) {
  return (
    <div style={{ padding: "8px 22px 22px" }}>
      {Array.from({ length: rows }).map((_, r) => (
        <div
          key={r}
          style={{
            display: "grid",
            gridTemplateColumns: `repeat(${cols}, 1fr)`,
            gap: 18,
            padding: "14px 0",
          }}
        >
          {Array.from({ length: cols }).map((_, c) => (
            <Skeleton key={c} height={12} />
          ))}
        </div>
      ))}
    </div>
  );
}

export default Skeleton;
