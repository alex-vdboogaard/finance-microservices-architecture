import { ChangeEvent } from 'react';
import { Filter, RefreshCw, Search, X } from 'lucide-react';

import { Button } from '../ui/button';
import { Card } from '../ui/card';
import { Input } from '../ui/input';
import { Label } from '../ui/label';
import type { AuditLogFilters } from '../../types/audit-log';

interface AuditLogFiltersProps {
  filters: AuditLogFilters;
  onFiltersChange: (filters: AuditLogFilters) => void;
  isLoading?: boolean;
  onRefresh?: () => void;
}

export function AuditLogFilters({
  filters,
  onFiltersChange,
  isLoading = false,
  onRefresh
}: AuditLogFiltersProps) {
  const updateFilters = (partial: Partial<AuditLogFilters>) => {
    onFiltersChange({ ...filters, ...partial });
  };

  const handleDateChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;

    if (!value) {
      const nextDateRange = { ...filters.dateRange };
      if (name === 'from') {
        delete nextDateRange?.from;
      } else {
        delete nextDateRange?.to;
      }

      const normalized =
        nextDateRange && Object.keys(nextDateRange).length > 0
          ? nextDateRange
          : undefined;

      updateFilters({ dateRange: normalized });
      return;
    }

    const date = new Date(value);
    updateFilters({
      dateRange: {
        ...filters.dateRange,
        [name]: date
      }
    });
  };

  const clearFilters = () => {
    onFiltersChange({});
  };

  const hasActiveFilters = Boolean(
    filters.search ||
      filters.service ||
      filters.user ||
      filters.dateRange?.from ||
      filters.dateRange?.to
  );

  const formatDate = (date?: Date) => (date ? date.toISOString().split('T')[0] : '');

  return (
    <Card className="border-0 bg-accent/20 p-4">
      <div className="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
        <div className="flex-1 space-y-3">
          <Label htmlFor="search" className="text-sm font-medium">
            Search by action
          </Label>
          <div className="relative">
            <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              id="search"
              placeholder="Filter by action keyword..."
              className="pl-9"
              value={filters.search ?? ''}
              onChange={(event) =>
                updateFilters({
                  search: event.target.value ? event.target.value : undefined
                })
              }
            />
          </div>
        </div>

        <div className="grid w-full flex-1 grid-cols-1 gap-4 sm:grid-cols-2 lg:max-w-xl">
          <div className="space-y-2">
            <Label htmlFor="from" className="text-sm font-medium">
              From date
            </Label>
            <Input
              id="from"
              name="from"
              type="date"
              value={formatDate(filters.dateRange?.from)}
              onChange={handleDateChange}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="to" className="text-sm font-medium">
              To date
            </Label>
            <Input
              id="to"
              name="to"
              type="date"
              value={formatDate(filters.dateRange?.to)}
              onChange={handleDateChange}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="service" className="text-sm font-medium">
              Service (optional)
            </Label>
            <Input
              id="service"
              placeholder="payments-service"
              value={filters.service ?? ''}
              onChange={(event) =>
                updateFilters({
                  service: event.target.value ? event.target.value : undefined
                })
              }
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="user" className="text-sm font-medium">
              User (optional)
            </Label>
            <Input
              id="user"
              placeholder="user@example.com"
              value={filters.user ?? ''}
              onChange={(event) =>
                updateFilters({ user: event.target.value ? event.target.value : undefined })
              }
            />
          </div>
        </div>
      </div>

      <div className="mt-4 flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex items-center gap-2 text-sm text-muted-foreground">
          <Filter className="h-4 w-4" />
          {hasActiveFilters ? 'Filters active' : 'No filters applied'}
        </div>

        <div className="flex flex-wrap items-center gap-2">
          <Button
            type="button"
            variant="outline"
            size="sm"
            className="gap-2"
            onClick={clearFilters}
            disabled={!hasActiveFilters}
          >
            <X className="h-4 w-4" />
            Clear
          </Button>

          <Button
            type="button"
            variant="secondary"
            size="sm"
            className="gap-2"
            onClick={onRefresh}
            disabled={isLoading || !onRefresh}
          >
            <RefreshCw className={isLoading ? 'h-4 w-4 animate-spin' : 'h-4 w-4'} />
            Refresh
          </Button>
        </div>
      </div>
    </Card>
  );
}
