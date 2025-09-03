import { useState } from 'react';
import { Card } from './ui/card';
import { Input } from './ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { Button } from './ui/button';
import { Label } from './ui/label';
import { Slider } from './ui/slider';
import { TransactionFilters as Filters } from '../types/transaction';
import { Search, Filter, X } from 'lucide-react';

interface TransactionFiltersProps {
  filters: Filters;
  onFiltersChange: (filters: Filters) => void;
}

export function TransactionFilters({ filters, onFiltersChange }: TransactionFiltersProps) {
  const [showFilters, setShowFilters] = useState(false);

  const updateFilters = (newFilters: Partial<Filters>) => {
    onFiltersChange({ ...filters, ...newFilters });
  };

  const clearFilters = () => {
    onFiltersChange({
      type: 'all',
      status: 'all',
      search: '',
      dateRange: undefined,
      amountRange: undefined
    });
  };

  const hasActiveFilters = 
    (filters.type && filters.type !== 'all') || 
    (filters.status && filters.status !== 'all') || 
    filters.search ||
    filters.dateRange?.from ||
    filters.dateRange?.to ||
    filters.amountRange?.min !== undefined ||
    filters.amountRange?.max !== undefined;

  // Calculate min and max amounts for slider
  const maxAmount = 5000; // Based on mock data, can be calculated dynamically
  const currentMinAmount = filters.amountRange?.min ?? 0;
  const currentMaxAmount = filters.amountRange?.max ?? maxAmount;

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(amount);
  };

  const formatDateForInput = (date: Date) => {
    return date.toISOString().split('T')[0];
  };

  const handleDateChange = (dateString: string, type: 'from' | 'to') => {
    if (!dateString) {
      const newDateRange = { ...filters.dateRange };
      if (type === 'from') {
        delete newDateRange.from;
      } else {
        delete newDateRange.to;
      }
      updateFilters({ 
        dateRange: Object.keys(newDateRange).length > 0 ? newDateRange : undefined 
      });
      return;
    }

    const date = new Date(dateString);
    updateFilters({ 
      dateRange: { 
        ...filters.dateRange, 
        [type]: date 
      } 
    });
  };

  return (
    <div className="space-y-4">
      {/* Search Bar */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
        <Input
          placeholder="Search transactions by ID or description..."
          value={filters.search || ''}
          onChange={(e) => updateFilters({ search: e.target.value })}
          className="pl-10 bg-input-background border-0 rounded-lg"
        />
      </div>

      {/* Filter Toggle */}
      <div className="flex items-center justify-between">
        <Button
          variant="outline"
          size="sm"
          onClick={() => setShowFilters(!showFilters)}
          className="gap-2"
        >
          <Filter className="w-4 h-4" />
          Filters
          {hasActiveFilters && (
            <div className="w-2 h-2 bg-primary rounded-full" />
          )}
        </Button>
        
        {hasActiveFilters && (
          <Button
            variant="ghost"
            size="sm"
            onClick={clearFilters}
            className="gap-2 text-muted-foreground"
          >
            <X className="w-4 h-4" />
            Clear
          </Button>
        )}
      </div>

      {/* Expandable Filters */}
      {showFilters && (
        <Card className="p-4 space-y-6 border-0 bg-accent/20">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Type Filter */}
            <div className="space-y-2">
              <Label>Transaction Type</Label>
              <Select
                value={filters.type || 'all'}
                onValueChange={(value) => 
                  updateFilters({ type: value as 'income' | 'expense' | 'all' })
                }
              >
                <SelectTrigger className="bg-input-background border-0">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Types</SelectItem>
                  <SelectItem value="income">Income</SelectItem>
                  <SelectItem value="expense">Expense</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Status Filter */}
            <div className="space-y-2">
              <Label>Status</Label>
              <Select
                value={filters.status || 'all'}
                onValueChange={(value) => 
                  updateFilters({ status: value as 'completed' | 'pending' | 'failed' | 'all' })
                }
              >
                <SelectTrigger className="bg-input-background border-0">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Status</SelectItem>
                  <SelectItem value="completed">Completed</SelectItem>
                  <SelectItem value="pending">Pending</SelectItem>
                  <SelectItem value="failed">Failed</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          {/* Date Range Filter */}
          <div className="space-y-3">
            <Label>Date Range</Label>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label className="text-sm text-muted-foreground">From Date</Label>
                <Input
                  type="date"
                  value={filters.dateRange?.from ? formatDateForInput(filters.dateRange.from) : ''}
                  onChange={(e) => handleDateChange(e.target.value, 'from')}
                  className="bg-input-background border-0"
                />
              </div>

              <div className="space-y-2">
                <Label className="text-sm text-muted-foreground">To Date</Label>
                <Input
                  type="date"
                  value={filters.dateRange?.to ? formatDateForInput(filters.dateRange.to) : ''}
                  onChange={(e) => handleDateChange(e.target.value, 'to')}
                  className="bg-input-background border-0"
                />
              </div>
            </div>
          </div>

          {/* Amount Range Filter */}
          <div className="space-y-3">
            <Label>Amount Range</Label>
            <div className="space-y-4">
              <div className="px-3">
                <Slider
                  value={[currentMinAmount, currentMaxAmount]}
                  onValueChange={([min, max]) => 
                    updateFilters({ 
                      amountRange: { min, max } 
                    })
                  }
                  max={maxAmount}
                  min={0}
                  step={50}
                  className="w-full"
                />
              </div>
              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>{formatCurrency(currentMinAmount)}</span>
                <span>{formatCurrency(currentMaxAmount)}</span>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label className="text-sm text-muted-foreground">Min Amount</Label>
                  <Input
                    type="number"
                    min="0"
                    max={maxAmount}
                    value={currentMinAmount}
                    onChange={(e) => 
                      updateFilters({ 
                        amountRange: { 
                          ...filters.amountRange, 
                          min: Number(e.target.value) 
                        } 
                      })
                    }
                    className="bg-input-background border-0"
                    placeholder="0"
                  />
                </div>
                <div className="space-y-2">
                  <Label className="text-sm text-muted-foreground">Max Amount</Label>
                  <Input
                    type="number"
                    min="0"
                    max={maxAmount}
                    value={currentMaxAmount}
                    onChange={(e) => 
                      updateFilters({ 
                        amountRange: { 
                          ...filters.amountRange, 
                          max: Number(e.target.value) 
                        } 
                      })
                    }
                    className="bg-input-background border-0"
                    placeholder={maxAmount.toString()}
                  />
                </div>
              </div>
            </div>
          </div>
        </Card>
      )}
    </div>
  );
}