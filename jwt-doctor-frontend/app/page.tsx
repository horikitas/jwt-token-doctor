"use client";

import { useState } from "react";
import { diagnoseToken, TokenDiagnosis } from "@/src/lib/diagnoseToken";

export default function Home() {
  const [token, setToken] = useState("");
  const [diagnosis, setDiagnosis] = useState<TokenDiagnosis | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function handleDiagnose() {
    setLoading(true);
    setError("");
    setDiagnosis(null);

    try {
      const result = await diagnoseToken(token);
      setDiagnosis(result);
    } catch {
      setError("Could not diagnose token. Check backend/API connection.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-slate-950 text-slate-100 p-8">
      <div className="mx-auto max-w-5xl space-y-6">
        <section>
          <h1 className="text-4xl font-bold">JWT Token Doctor</h1>
          <p className="mt-2 text-slate-400">
            Decode and diagnose JWT security issues.
          </p>
        </section>

        <section className="rounded-2xl bg-slate-900 p-6 shadow">
          <label className="block text-sm font-medium mb-2">
            Paste JWT
          </label>

          <textarea
            value={token}
            onChange={(e) => setToken(e.target.value)}
            rows={8}
            className="w-full rounded-xl bg-slate-950 border border-slate-700 p-4 font-mono text-sm outline-none focus:border-blue-500"
            placeholder="eyJhbGciOi..."
          />

          <button
            onClick={handleDiagnose}
            disabled={loading || token.trim().length === 0}
            className="mt-4 rounded-xl bg-blue-600 px-5 py-2 font-semibold disabled:opacity-50"
          >
            {loading ? "Diagnosing..." : "Diagnose Token"}
          </button>

          {error && (
            <p className="mt-4 text-red-400">{error}</p>
          )}
        </section>

        {diagnosis && (
          <section className="grid gap-6">
            <div className="rounded-2xl bg-slate-900 p-6">
              <h2 className="text-xl font-semibold">Risk Summary</h2>
              <div className="mt-3 flex gap-4">
                <span>Severity: {diagnosis.severity}</span>
                <span>Risk Score: {diagnosis.riskScore}</span>
              </div>
            </div>

            <div className="rounded-2xl bg-slate-900 p-6">
              <h2 className="text-xl font-semibold">Findings</h2>

              {diagnosis.findings.length === 0 ? (
                <p className="mt-3 text-green-400">No findings detected.</p>
              ) : (
                <div className="mt-4 space-y-3">
                  {diagnosis.findings.map((finding) => (
                    <div
                      key={finding.code}
                      className="rounded-xl border border-slate-700 p-4"
                    >
                      <div className="font-semibold">
                        {finding.severity} — {finding.code}
                      </div>
                      <p className="mt-1 text-slate-300">{finding.message}</p>
                      <p className="mt-1 text-slate-400">
                        {finding.recommendation}
                      </p>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {diagnosis.decodedJwt && (
              <>
                <JsonPanel title="Header" data={diagnosis.decodedJwt.header} />
                <JsonPanel title="Payload" data={diagnosis.decodedJwt.payload} />
              </>
            )}
          </section>
        )}
      </div>
    </main>
  );
}

function JsonPanel({
  title,
  data,
}: {
  title: string;
  data: Record<string, unknown>;
}) {
  return (
    <div className="rounded-2xl bg-slate-900 p-6">
      <h2 className="text-xl font-semibold">{title}</h2>
      <pre className="mt-4 overflow-auto rounded-xl bg-slate-950 p-4 text-sm">
        {JSON.stringify(data, null, 2)}
      </pre>
    </div>
  );
}