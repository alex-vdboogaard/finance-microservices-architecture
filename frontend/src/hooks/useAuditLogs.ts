import { useCallback, useEffect, useState } from 'react';

import type { AuditLog, AuditLogFilters } from '../types/audit-log';

const API_BASE_URL =
  (import.meta.env.VITE_API_GATEWAY_URL as string | undefined) ?? 'http://localhost:8080';
const isAbsoluteBaseUrl = /^https?:\/\//i.test(API_BASE_URL);
const normalizedBaseUrl = API_BASE_URL.endsWith('/') ? API_BASE_URL : `${API_BASE_URL}/`;

const buildEndpointUrl = (path: string) => {
  if (isAbsoluteBaseUrl) {
    return new URL(path, normalizedBaseUrl).toString();
  }

  return `${normalizedBaseUrl}${path}`;
};

const createRequestUrl = (path: string) => {
  const endpoint = buildEndpointUrl(path);

  if (isAbsoluteBaseUrl) {
    return new URL(endpoint);
  }

  const origin = typeof window !== 'undefined' ? window.location.origin : 'http://localhost:5173';
  return new URL(endpoint, origin);
};

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
        const requestUrl = createRequestUrl(searchTerm ? 'logs/action' : 'logs');

        if (searchTerm) {
          requestUrl.searchParams.set('action', searchTerm);
        }

        const response = await fetch(requestUrl.toString(), {
          signal: controller.signal
        });

        if (!response.ok) {
          throw new Error(`Failed to fetch audit logs (${response.status})`);
        }

        const payload: AuditLog[] = await response.json();
        setLogs(payload);
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
