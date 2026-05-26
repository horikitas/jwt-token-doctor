export type Finding = {
  severity: string;
  code: string;
  message: string;
  recommendation: string;
};

export interface TokenDiagnosis {
  validFormat: boolean;
  header?: Record<string, unknown>;
  payload?: Record<string, unknown>;
  signature?: string;
  findings: Finding[];
  riskScore: number;
  severity: string;
}

export async function diagnoseToken(token: string): Promise<TokenDiagnosis> {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/tokens/diagnose`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ token }),
    }
  );

  if (!response.ok) {
    throw new Error("Failed to diagnose token");
  }

  return response.json();
}