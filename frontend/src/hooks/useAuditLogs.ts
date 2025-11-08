import { useCallback, useEffect, useState } from 'react';

import type { AuditLog, AuditLogFilters } from '../types/audit-log';

const API_BASE_URL = 'http://localhost:8080/api/v1';

function extractAuditLogs(payload: unknown): AuditLog[] | null {
  if (Array.isArray(payload)) {
    return payload as AuditLog[];
  }

  if (typeof payload === 'object' && payload !== null) {
    const data = (payload as { data?: unknown }).data;

    if (Array.isArray(data)) {
      return data as AuditLog[];
    }
  }

  return null;
}

interface UseAuditLogsResult {
  logs: AuditLog[];
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export function useAuditLogs(filters: AuditLogFilters): UseAuditLogsResult {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);

  const refetch = useCallback(() => {
    setReloadToken((token) => token + 1);
  }, []);

  useEffect(() => {
    const controller = new AbortController();

    const fetchLogs = async () => {
      setLoading(true);
      setError(null);

      try {
        const searchTerm = filters.search?.trim();
        const path = searchTerm ? 'logs/action' : 'logs';
        const url = new URL(`${API_BASE_URL}/${path}`);

        if (searchTerm) {
          url.searchParams.set('action', searchTerm);
        }

        const response = await fetch(url.toString(), {
          signal: controller.signal
        });

        if (!response.ok) {
          throw new Error(`Failed to fetch audit logs (${response.status})`);
        }

        const payload = await response.json();
        const resolvedLogs = extractAuditLogs(payload);

        if (!resolvedLogs) {
          throw new Error('Invalid audit log response format');
        }

        setLogs(resolvedLogs);
      } catch (fetchError) {
        if ((fetchError as Error).name === 'AbortError') {
          return;
        }

        console.error('Failed to load audit logs', fetchError);
        setError(
          fetchError instanceof Error
            ? fetchError.message
            : 'An unexpected error occurred while loading audit logs.'
        );
        setLogs([]);
      } finally {
        setLoading(false);
      }
    };

    fetchLogs();

    return () => controller.abort();
  }, [filters.search, reloadToken]);

  return { logs, loading, error, refetch };
}
