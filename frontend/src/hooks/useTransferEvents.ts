import { useCallback, useEffect, useState } from 'react';

import type { TransferEvent, TransferEventFilters, TransferEventPage } from '../types/transfer-event';

// Mirror audit-log hook style; adjust host if running inside Docker network
const TRANSACTIONS_API_URL = 'http://localhost:8080/transaction-service/transactions';

function extractTransferEvents(payload: unknown): TransferEvent[] | null {
  // Direct array response
  if (Array.isArray(payload)) {
    return payload as TransferEvent[];
  }

  if (typeof payload === 'object' && payload !== null) {
    const obj = payload as { data?: unknown; content?: unknown };

    // Backend shape: { success, message, data: { content: [...] } }
    if (obj.data && typeof obj.data === 'object') {
      const dataObj = obj.data as { content?: unknown };
      if (Array.isArray(dataObj.content)) {
        return dataObj.content as TransferEvent[];
      }

      // Fallback: sometimes data might already be an array
      if (Array.isArray(obj.data)) {
        return obj.data as TransferEvent[];
      }
    }

    // Fallback: content directly on the root
    if (Array.isArray(obj.content)) {
      return obj.content as TransferEvent[];
    }
  }

  return null;
}

function extractTransferPage(payload: unknown): TransferEventPage | null {
  if (typeof payload !== 'object' || payload === null) return null;

  const obj = payload as { data?: unknown };
  if (!obj.data || typeof obj.data !== 'object') return null;

  const data = obj.data as Partial<TransferEventPage> & { content?: unknown };
  if (!Array.isArray(data.content)) return null;

  return {
    content: data.content as TransferEvent[],
    page: typeof data.page === 'number' ? data.page : 0,
    size: typeof data.size === 'number' ? data.size : (Array.isArray(data.content) ? data.content.length : 0),
    totalElements: typeof data.totalElements === 'number' ? data.totalElements : data.content.length,
    totalPages: typeof data.totalPages === 'number' ? data.totalPages : 1,
    first: typeof data.first === 'boolean' ? data.first : true,
    last: typeof data.last === 'boolean' ? data.last : true,
    hasNext: typeof data.hasNext === 'boolean' ? data.hasNext : false,
    hasPrevious: typeof data.hasPrevious === 'boolean' ? data.hasPrevious : false
  };
}

interface UseTransferEventsResult {
  events: TransferEvent[];
  loading: boolean;
  error: string | null;
  // one-based page number for the UI
  page: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  hasNext: boolean;
  hasPrevious: boolean;
  setPage: (page: number) => void;
  setPageSize: (size: number) => void;
  refetch: () => void;
}

export function useTransferEvents(filters: TransferEventFilters): UseTransferEventsResult {
  const [events, setEvents] = useState<TransferEvent[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);
  const [page, setPage] = useState(1); // one-based for UI
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  const refetch = useCallback(() => {
    setReloadToken((token) => token + 1);
  }, []);

  useEffect(() => {
    const controller = new AbortController();

    const fetchEvents = async () => {
      setLoading(true);
      setError(null);

      try {
        const url = new URL(TRANSACTIONS_API_URL);
        // Spring pageable uses zero-based pages
        url.searchParams.set('page', String(Math.max(0, page - 1)));
        url.searchParams.set('size', String(pageSize));

        const response = await fetch(url.toString(), {
          signal: controller.signal
        });

        if (!response.ok) {
          throw new Error(`Failed to fetch transactions (${response.status})`);
        }

        const payload = await response.json();
        // Prefer page-aware extraction; fallback to simple list
        const pageData = extractTransferPage(payload);
        if (pageData) {
          setEvents(pageData.content);
          setTotalPages(pageData.totalPages);
          setTotalElements(pageData.totalElements);
          setHasNext(pageData.hasNext);
          setHasPrevious(pageData.hasPrevious);
          // keep UI page in sync in case backend adjusted
          setPage(pageData.page + 1);
        } else {
          const resolved = extractTransferEvents(payload);
          if (!resolved) {
            throw new Error('Invalid transactions response format');
          }
          setEvents(resolved);
          setTotalPages(1);
          setTotalElements(resolved.length);
          setHasNext(false);
          setHasPrevious(false);
          setPage(1);
          setPageSize(resolved.length || 10);
        }
      } catch (fetchError) {
        if ((fetchError as Error).name === 'AbortError') return;
        console.error('Failed to load transactions', fetchError);
        setError(
          fetchError instanceof Error
            ? fetchError.message
            : 'An unexpected error occurred while loading transactions.'
        );
        setEvents([]);
      } finally {
        setLoading(false);
      }
    };

    fetchEvents();
    return () => controller.abort();
  }, [reloadToken, page, pageSize]);

  return {
    events,
    loading,
    error,
    page,
    pageSize,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    setPage,
    setPageSize,
    refetch
  };
}
