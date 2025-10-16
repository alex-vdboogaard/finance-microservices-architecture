import { useCallback, useEffect, useMemo, useState } from 'react';
import React from "react"
import { useSearchParams } from 'react-router-dom';

import type { AuditLogFilters as AuditLogFiltersType } from '../types/audit-log';
import { useAuditLogs } from '../hooks/useAuditLogs';
import { AuditLogFilters } from '../components/audit-log/AuditLogFilters';
import { AuditLogTable } from '../components/audit-log/AuditLogTable';

const parseDateParam = (value: string | null) => {
  if (!value) {
    return undefined;
  }

  const parsed = new Date(value);
  return Number.isNaN(parsed.getTime()) ? undefined : parsed;
};

const serializeDate = (date?: Date) => {
  if (!date) {
    return undefined;
  }

  return date.toISOString();
};

const areDatesEqual = (a?: Date, b?: Date) => {
  if (!a && !b) {
    return true;
  }

  if (!a || !b) {
    return false;
  }

  return a.getTime() === b.getTime();
};

const deriveFiltersFromParams = (params: URLSearchParams): AuditLogFiltersType => {
  const fromDate = parseDateParam(params.get('from'));
  const toDate = parseDateParam(params.get('to'));

  const search = params.get('search') || undefined;
  const service = params.get('service') || undefined;
  const user = params.get('user') || undefined;

  return {
    search,
    service,
    user,
    dateRange:
      fromDate || toDate
        ? {
            from: fromDate,
            to: toDate
          }
        : undefined
  };
};

const areFiltersEqual = (left: AuditLogFiltersType, right: AuditLogFiltersType) => {
  const searchEqual = (left.search ?? '') === (right.search ?? '');
  const serviceEqual = (left.service ?? '') === (right.service ?? '');
  const userEqual = (left.user ?? '') === (right.user ?? '');
  const fromEqual = areDatesEqual(left.dateRange?.from, right.dateRange?.from);
  const toEqual = areDatesEqual(left.dateRange?.to, right.dateRange?.to);

  return searchEqual && serviceEqual && userEqual && fromEqual && toEqual;
};

export function AuditLogPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [filters, setFilters] = useState<AuditLogFiltersType>(() =>
    deriveFiltersFromParams(searchParams)
  );

  useEffect(() => {
    const nextFilters = deriveFiltersFromParams(searchParams);

    setFilters((current) => (areFiltersEqual(current, nextFilters) ? current : nextFilters));
  }, [searchParams]);

  useEffect(() => {
    const params = new URLSearchParams();

    if (filters.search) {
      params.set('search', filters.search);
    }

    if (filters.service) {
      params.set('service', filters.service);
    }

    if (filters.user) {
      params.set('user', filters.user);
    }

    const fromValue = serializeDate(filters.dateRange?.from);
    const toValue = serializeDate(filters.dateRange?.to);

    if (fromValue) {
      params.set('from', fromValue);
    }

    if (toValue) {
      params.set('to', toValue);
    }

    setSearchParams(params, { replace: true });
  }, [filters, setSearchParams]);

  const { logs, loading, error, refetch } = useAuditLogs(filters);

  const filteredLogs = useMemo(() => {
    return logs.filter((log) => {
      const timestamp = new Date(log.timestamp);

      if (filters.dateRange?.from && timestamp < filters.dateRange.from) {
        return false;
      }

      if (filters.dateRange?.to && timestamp > filters.dateRange.to) {
        return false;
      }

      if (filters.service) {
        const service = log.service?.toLowerCase() ?? '';
        if (!service.includes(filters.service.toLowerCase())) {
          return false;
        }
      }

      if (filters.user) {
        const actor = log.user?.toLowerCase() ?? '';
        if (!actor.includes(filters.user.toLowerCase())) {
          return false;
        }
      }

      return true;
    });
  }, [logs, filters]);

  const sortedLogs = useMemo(() => {
    return [...filteredLogs].sort((a, b) => {
      return new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime();
    });
  }, [filteredLogs]);

  const handleFiltersChange = useCallback((nextFilters: AuditLogFiltersType) => {
    setFilters(nextFilters);
  }, []);

  return (
    <div className="space-y-6">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Audit Log</h1>
        <p className="text-muted-foreground">
          Review platform activity and trace events across services. Use filters to isolate
          specific actions or time windows when investigating incidents.
        </p>
      </div>

      <AuditLogFilters
        filters={filters}
        onFiltersChange={handleFiltersChange}
        isLoading={loading}
        onRefresh={refetch}
      />

      <section className="space-y-4">
        <header className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-2xl font-semibold tracking-tight">Recent activity</h2>
            <p className="text-sm text-muted-foreground">
              Showing {sortedLogs.length} event{sortedLogs.length === 1 ? '' : 's'}
              {filters.search ? ` for "${filters.search}"` : ''}
            </p>
          </div>
        </header>

        <AuditLogTable logs={sortedLogs} isLoading={loading} error={error} />
      </section>
    </div>
  );
}
