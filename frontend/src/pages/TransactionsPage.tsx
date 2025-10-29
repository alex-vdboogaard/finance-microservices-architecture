import { useCallback, useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router-dom';

import type { TransferEventFilters as Filters } from '../types/transfer-event';
import { useTransferEvents } from '../hooks/useTransferEvents';
import { TransactionFilters } from '../components/transactions/TransactionFilters';
import { TransactionTable } from '../components/transactions/TransactionTable';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from '../components/ui/pagination';

const parseDateParam = (value: string | null) => {
  if (!value) return undefined;
  const parsed = new Date(value);
  return Number.isNaN(parsed.getTime()) ? undefined : parsed;
};

const serializeDate = (date?: Date) => (date ? date.toISOString() : undefined);

const areDatesEqual = (a?: Date, b?: Date) => {
  if (!a && !b) return true;
  if (!a || !b) return false;
  return a.getTime() === b.getTime();
};

const deriveFiltersFromParams = (params: URLSearchParams): Filters => {
  const fromDate = parseDateParam(params.get('from'));
  const toDate = parseDateParam(params.get('to'));

  const search = params.get('search') || undefined;
  return {
    search,
    dateRange: fromDate || toDate ? { from: fromDate, to: toDate } : undefined
  };
};

const areFiltersEqual = (left: Filters, right: Filters) => {
  const searchEqual = (left.search ?? '') === (right.search ?? '');
  const fromEqual = areDatesEqual(left.dateRange?.from, right.dateRange?.from);
  const toEqual = areDatesEqual(left.dateRange?.to, right.dateRange?.to);
  return searchEqual && fromEqual && toEqual;
};

export function TransactionsPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filters, setFilters] = useState<Filters>(() => deriveFiltersFromParams(searchParams));

  useEffect(() => {
    const nextFilters = deriveFiltersFromParams(searchParams);
    setFilters((current) => (areFiltersEqual(current, nextFilters) ? current : nextFilters));
  }, [searchParams]);

  useEffect(() => {
    const params = new URLSearchParams();
    if (filters.search) params.set('search', filters.search);
    const fromValue = serializeDate(filters.dateRange?.from);
    const toValue = serializeDate(filters.dateRange?.to);
    if (fromValue) params.set('from', fromValue);
    if (toValue) params.set('to', toValue);
    setSearchParams(params, { replace: true });
  }, [filters, setSearchParams]);

  const {
    events,
    loading,
    error,
    refetch,
    page,
    pageSize,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    setPage
  } = useTransferEvents(filters);

  const filtered = useMemo(() => {
    if (!Array.isArray(events)) return [];

    return events.filter((ev) => {
      const ts = new Date(ev.timestamp);
      if (filters.dateRange?.from && ts < filters.dateRange.from) return false;
      if (filters.dateRange?.to && ts > filters.dateRange.to) return false;

      if (filters.search) {
        const s = filters.search.toLowerCase();
        if (!ev.transactionId.toLowerCase().includes(s)) {
          return false;
        }
      }

      return true;
    });
  }, [events, filters]);

  const handleFiltersChange = useCallback((next: Filters) => setFilters(next), []);

  return (
    <div className="space-y-6">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Transactions</h1>
        <p className="text-muted-foreground">Fetch and review transaction events from the transaction-service.</p>
      </div>

      <TransactionFilters filters={filters} onFiltersChange={handleFiltersChange} isLoading={loading} onRefresh={refetch} />

      <section className="space-y-4">
        <header className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-2xl font-semibold tracking-tight">Recent transactions</h2>
            <p className="text-sm text-muted-foreground">
              {totalElements > 0
                ? `Showing ${(page - 1) * pageSize + 1} to ${Math.min(page * pageSize, totalElements)} of ${totalElements} transactions`
                : 'No transactions to display'}
            </p>
          </div>
        </header>

        <TransactionTable events={filtered} isLoading={loading} error={error} />

        {/* Server-side pagination controls */}
        {totalPages > 1 && (
          <div className="flex items-center justify-between">
            <Pagination>
              <PaginationContent>
                <PaginationItem>
                  <PaginationPrevious
                    onClick={() => hasPrevious && setPage(page - 1)}
                    className={!hasPrevious ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                  />
                </PaginationItem>

                {Array.from({ length: totalPages }, (_, i) => i + 1)
                  .filter((p) => p === 1 || p === totalPages || Math.abs(p - page) <= 1)
                  .map((p, index, arr) => {
                    const showEllipsisBefore = index > 0 && p - arr[index - 1] > 1;
                    return (
                      <div key={p} className="flex items-center">
                        {showEllipsisBefore && (
                          <PaginationItem>
                            <PaginationEllipsis />
                          </PaginationItem>
                        )}
                        <PaginationItem>
                          <PaginationLink
                            onClick={() => setPage(p)}
                            isActive={page === p}
                            className="cursor-pointer"
                          >
                            {p}
                          </PaginationLink>
                        </PaginationItem>
                      </div>
                    );
                  })}

                <PaginationItem>
                  <PaginationNext
                    onClick={() => hasNext && setPage(page + 1)}
                    className={!hasNext ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                  />
                </PaginationItem>
              </PaginationContent>
            </Pagination>
          </div>
        )}
      </section>
    </div>
  );
}
