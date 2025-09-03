import { useState, useMemo } from 'react';
import { TransactionFilters } from './components/TransactionFilters';
import { TransactionTable } from './components/TransactionTable';
import { mockTransactions } from './data/mockTransactions';
import { TransactionFilters as Filters } from './types/transaction';

export default function App() {
  const [filters, setFilters] = useState<Filters>({
    type: 'all',
    status: 'all'
  });

  const filteredTransactions = useMemo(() => {
    return mockTransactions.filter(transaction => {
      // Search filter
      if (filters.search) {
        const searchLower = filters.search.toLowerCase();
        if (!transaction.description.toLowerCase().includes(searchLower) &&
            !transaction.globalId.toLowerCase().includes(searchLower)) {
          return false;
        }
      }

      // Date range filter
      if (filters.dateRange?.from || filters.dateRange?.to) {
        const transactionDate = new Date(transaction.date);
        
        if (filters.dateRange.from && transactionDate < filters.dateRange.from) {
          return false;
        }
        
        if (filters.dateRange.to && transactionDate > filters.dateRange.to) {
          return false;
        }
      }

      // Amount range filter
      if (filters.amountRange?.min !== undefined || filters.amountRange?.max !== undefined) {
        const absAmount = Math.abs(transaction.amount);
        
        if (filters.amountRange.min !== undefined && absAmount < filters.amountRange.min) {
          return false;
        }
        
        if (filters.amountRange.max !== undefined && absAmount > filters.amountRange.max) {
          return false;
        }
      }

      // Type filter
      if (filters.type && filters.type !== 'all' && transaction.type !== filters.type) {
        return false;
      }

      // Status filter
      if (filters.status && filters.status !== 'all' && transaction.status !== filters.status) {
        return false;
      }

      return true;
    });
  }, [filters]);

  return (
    <div className="min-h-screen bg-background">
      <div className="max-w-7xl mx-auto p-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <h1>Finance Dashboard</h1>
          <p className="text-muted-foreground">
            Track and manage all your financial transactions in one place
          </p>
        </div>

        {/* Filters */}
        <TransactionFilters filters={filters} onFiltersChange={setFilters} />

        {/* Transactions Table */}
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h2>Transactions</h2>
              <p className="text-muted-foreground">
                {filteredTransactions.length === mockTransactions.length 
                  ? `${filteredTransactions.length} transactions`
                  : `${filteredTransactions.length} of ${mockTransactions.length} transactions`
                }
              </p>
            </div>
          </div>
          
          <TransactionTable transactions={filteredTransactions} />
        </div>
      </div>
    </div>
  );
}