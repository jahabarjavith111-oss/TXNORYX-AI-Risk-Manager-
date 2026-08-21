import { formatCurrency, formatDate } from "../utils/format";
import StatusBadge from "./StatusBadge";

export default function TransactionTable({ transactions, onView, compact = false }) {
  if (!transactions || transactions.length === 0) {
    return <p style={{ color: "#64748b", textAlign: "center", padding: "20px" }}>No transactions found.</p>;
  }

  const rows = compact ? transactions.slice(0, 5) : transactions;

  return (
    <div className="table-wrap">
      <table className="data-table" style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr style={{ background: "rgba(102,126,255,0.04)", color: "#94a3b8" }}>
            <th style={{ padding: "10px 8px", textAlign: "left" }}>ID</th>
            <th style={{ padding: "10px 8px", textAlign: "left" }}>Amount</th>
            <th style={{ padding: "10px 8px", textAlign: "left" }}>Method</th>
            <th style={{ padding: "10px 8px", textAlign: "left" }}>Status</th>
            <th style={{ padding: "10px 8px", textAlign: "left" }}>Date</th>
            {onView && <th style={{ padding: "10px 8px", textAlign: "center" }}>Action</th>}
          </tr>
        </thead>
        <tbody>
          {rows.map((txn) => (
            <tr key={txn.transactionId} style={{ borderBottom: "1px solid rgba(0,0,0,0.06)" }}>
              <td style={{ padding: "10px 8px" }}><strong className="txn-id" style={{ fontFamily: "monospace", fontSize: 12 }}>{txn.transactionId}</strong></td>
              <td style={{ padding: "10px 8px", fontWeight: 700 }}>{formatCurrency(txn.amount)}</td>
              <td style={{ padding: "10px 8px" }}>{txn.paymentMethod || "—"}</td>
              <td style={{ padding: "10px 8px" }}><StatusBadge status={txn.status} /></td>
              <td style={{ padding: "10px 8px", color: "#64748b", fontSize: 12 }}>{formatDate(txn.createdAt)}</td>
              {onView && (
                <td style={{ padding: "10px 8px", textAlign: "center" }}>
                  <button
                    onClick={() => onView(txn.transactionId)}
                    style={{ padding: "5px 12px", borderRadius: 6, border: "1px solid #e2e8f0", background: "#fff", color: "#2b84ea", fontWeight: 600, cursor: "pointer", fontSize: 12 }}
                  >
                    View
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
