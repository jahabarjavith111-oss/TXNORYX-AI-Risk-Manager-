import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import StatusBadge from "../components/StatusBadge";
import { TableSkeleton } from "../components/Skeleton";
import { getTransactions } from "../services/transactionService";
import {
  formatCurrency,
  formatDateTime,
  formatNumber,
} from "../utils/format";


function SearchIcon() {
  return (
    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="11" cy="11" r="7" />
      <line x1="21" y1="21" x2="16.65" y2="16.65" />
    </svg>
  );
}

const STATUS_OPTIONS = [
  "SUCCESS",
  "FAILED",
  "TIMEOUT",
  "DECLINED",
  "SUSPICIOUS",
  "RECOVERED",
  "PENDING",
];

function Transactions() {
  const navigate = useNavigate();
  const [txns, setTxns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [query, setQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");

  const load = useCallback(() => {
    setLoading(true);
    getTransactions()
      .then((data) => {
        setTxns(data);
        setError(null);
      })
      .catch(() => {
        setError("Could not load transactions");
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    load();
    const onChange = () => load();
    window.addEventListener("txns:changed", onChange);
    return () => window.removeEventListener("txns:changed", onChange);
  }, [load]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return txns.filter((t) => {
      const matchesQuery =
        !q ||
        String(t.transactionId || "").toLowerCase().includes(q) ||
        String(t.merchant || "").toLowerCase().includes(q);
      const matchesStatus =
        statusFilter === "ALL" || (t.status || "PENDING") === statusFilter;
      return matchesQuery && matchesStatus;
    });
  }, [txns, query, statusFilter]);

  const availableStatuses = useMemo(
    () =>
      STATUS_OPTIONS.filter((s) =>
        txns.some((t) => (t.status || "PENDING") === s)
      ),
    [txns]
  );

  return (
    <div className="card">
      <div className="table-toolbar">
        <div className="search-input">
          <SearchIcon />
          <input
            type="text"
            placeholder="Search by ID or merchant…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
        <select
          className="select"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option value="ALL">All statuses</option>
          {availableStatuses.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
        <span className="result-count">
          {loading
            ? "Loading…"
            : `${formatNumber(filtered.length)} of ${formatNumber(txns.length)} transactions`}
        </span>
      </div>

      {loading ? (
        <TableSkeleton rows={7} cols={6} />
      ) : error ? (
        <div className="empty-state">
          <h3>Something went wrong</h3>
          <p>{error}</p>
        </div>
      ) : filtered.length === 0 ? (
        <div className="empty-state">
          <h3>No transactions found</h3>
          <p>Try adjusting your search or filters.</p>
        </div>
      ) : (
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Transaction ID</th>
                <th>Amount</th>
                <th>Method</th>
                <th>Status</th>
                <th>Merchant</th>
                <th>Date &amp; Time</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((txn) => (
                <tr key={txn.transactionId} style={{ cursor: "pointer" }} onClick={() => navigate(`/investigations/${txn.transactionId}`)}>
                  <td className="txn-id">{txn.transactionId}</td>
                  <td className="amount-cell">{formatCurrency(txn.amount)}</td>
                  <td>{txn.paymentMethod || "—"}</td>
                  <td>
                    <StatusBadge status={txn.status} />
                  </td>
                  <td>{txn.merchant || "—"}</td>
                  <td>{formatDateTime(txn.createdAt)}</td>
                  <td>
                    <button
                      onClick={(e) => { e.stopPropagation(); navigate(`/investigations/${txn.transactionId}`); }}
                      style={{ padding: "4px 10px", borderRadius: 6, border: "1px solid #e2e8f0", background: "#fff", color: "#2b84ea", fontWeight: 600, fontSize: 11, cursor: "pointer" }}
                    >
                      View
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default Transactions;
