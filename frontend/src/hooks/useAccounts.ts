import { useCallback, useEffect, useState } from 'react';

import type { Account, AccountPage } from '../types/account';

const ACCOUNTS_API_URL = 'http://localhost:8080/api/v1/accounts';

function extractAccountPage(payload: unknown): AccountPage | null {
  if (typeof payload !== 'object' || payload === null) return null;

  const obj = payload as { data?: unknown };
  if (!obj.data || typeof obj.data !== 'object') return null;

  const data = obj.data as Partial<AccountPage> & { content?: unknown };
  if (!Array.isArray(data.content)) return null;

  return {
    content: data.content as Account[],
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

interface UseAccountsResult {
  accounts: Account[];
  loading: boolean;
  error: string | null;
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

export function useAccounts(): UseAccountsResult {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);
  const [page, setPage] = useState(1);
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

    const fetchAccounts = async () => {
      setLoading(true);
      setError(null);

      try {
        const url = new URL(ACCOUNTS_API_URL);
        url.searchParams.set('page', String(Math.max(0, page - 1)));
        url.searchParams.set('size', String(pageSize));

        const response = await fetch(url.toString(), {
          signal: controller.signal
        });

        if (!response.ok) {
          throw new Error(`Failed to fetch accounts (${response.status})`);
        }

        const payload = await response.json();
        const pageData = extractAccountPage(payload);

        if (!pageData) {
          throw new Error('Invalid accounts response format');
        }

        setAccounts(pageData.content);
        setTotalPages(pageData.totalPages);
        setTotalElements(pageData.totalElements);
        setHasNext(pageData.hasNext);
        setHasPrevious(pageData.hasPrevious);
        setPage(pageData.page + 1);
      } catch (fetchError) {
        if ((fetchError as Error).name === 'AbortError') return;
        console.error('Failed to load accounts', fetchError);
        setError(
          fetchError instanceof Error
            ? fetchError.message
            : 'An unexpected error occurred while loading accounts.'
        );
        setAccounts([]);
      } finally {
        setLoading(false);
      }
    };

    fetchAccounts();

    return () => controller.abort();
  }, [reloadToken, page, pageSize]);

  return {
    accounts,
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

