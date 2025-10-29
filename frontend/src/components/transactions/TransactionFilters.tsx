import { ChangeEvent, useState } from 'react';
import { Filter, RefreshCw, Search, X } from 'lucide-react';

import { Button } from '../ui/button';
import { Card } from '../ui/card';
import { Input } from '../ui/input';
import { Label } from '../ui/label';
import type { TransferEventFilters } from '../../types/transfer-event';

interface TransactionFiltersProps {
  filters: TransferEventFilters;
  onFiltersChange: (filters: TransferEventFilters) => void;
  isLoading?: boolean;
  onRefresh?: () => void;
}

const hasFiltersApplied = (filters: TransferEventFilters) =>
  Boolean(filters.search || filters.dateRange?.from || filters.dateRange?.to);

export function TransactionFilters({
  filters,
  onFiltersChange,
  isLoading = false,
  onRefresh
}: TransactionFiltersProps) {
  const [showFilters, setShowFilters] = useState(() => hasFiltersApplied(filters));

  const updateFilters = (partial: Partial<TransferEventFilters>) => {
    onFiltersChange({ ...filters, ...partial });
  };

  const handleDateChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;

    if (!value) {
      const nextDateRange = { ...filters.dateRange };
      if (name === 'from') delete nextDateRange?.from;
      else delete nextDateRange?.to;

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

  const clearFilters = () => onFiltersChange({});
  const filtersActive = hasFiltersApplied(filters);
  const formatDate = (date?: Date) => (date ? date.toISOString().split('T')[0] : '');

  return (
    <div className="space-y-4">
      <div className="space-y-3">
        <Label htmlFor="search" className="text-sm font-medium">
          Search by transaction ID
        </Label>
        <div className="relative">
          <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 transform text-muted-foreground" />
          <Input
            id="search"
            placeholder="Filter by transaction ID..."
            className="pl-10 bg-input-background border-0 rounded-lg"
            value={filters.search ?? ''}
            onChange={(event) =>
              updateFilters({ search: event.target.value ? event.target.value : undefined })
            }
          />
        </div>
      </div>

      <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex items-center gap-3">
          <Button
            type="button"
            variant="outline"
            size="sm"
            className="gap-2"
            onClick={() => setShowFilters((prev) => !prev)}
            aria-expanded={showFilters}
          >
            <Filter className="h-4 w-4" />
            Filters
            {filtersActive && <span className="h-2 w-2 rounded-full bg-primary" />}
          </Button>

          <span className="text-sm text-muted-foreground">
            {filtersActive ? 'Filters active' : 'No filters applied'}
          </span>
        </div>

        <div className="flex flex-wrap items-center gap-2">
          <Button
            type="button"
            variant="outline"
            size="sm"
            className="gap-2"
            onClick={clearFilters}
            disabled={!filtersActive}
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

      {showFilters && (
        <Card className="space-y-6 border-0 bg-accent/20 p-4">
          <div className="grid w-full grid-cols-1 gap-4 sm:grid-cols-2 lg:max-w-5xl">
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
          </div>
        </Card>
      )}
    </div>
  );
}

