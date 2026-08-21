const STATUS_MAP = {
  SUCCESS: "success",
  FAILED: "failed",
  TIMEOUT: "timeout",
  DECLINED: "declined",
  SUSPICIOUS: "suspicious",
  RECOVERED: "recovered",
  PENDING: "pending",
};

function StatusBadge({ status }) {
  const tone = STATUS_MAP[status] || "pending";
  return <span className={`badge badge-${tone}`}>{status}</span>;
}

export default StatusBadge;
