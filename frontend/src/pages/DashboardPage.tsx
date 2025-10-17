import { useMemo, useState } from 'react';

import { TransactionFilters } from '../components/TransactionFilters';
import { TransactionTable } from '../components/TransactionTable';
import { mockTransactions } from '../data/mockTransactions';
import { TransactionFilters as Filters } from '../types/transaction';
import { getTransactionDirection } from '../utils/transaction-formatters';

export function DashboardPage() {
  const [filters, setFilters] = useState<Filters>({
    type: 'all',
    status: 'all'
  });

  const filteredTransactions = useMemo(() => {
    return mockTransactions.filter((transaction) => {
      if (filters.search) {
        const searchLower = filters.search.toLowerCase();
        if (
          !transaction.description.toLowerCase().includes(searchLower) &&
          !transaction.globalId.toLowerCase().includes(searchLower)
        ) {
          return false;
        }
      }

      if (filters.dateRange?.from || filters.dateRange?.to) {
        const transactionDate = new Date(transaction.date);

        if (filters.dateRange.from && transactionDate < filters.dateRange.from) {
          return false;
        }

        if (filters.dateRange.to && transactionDate > filters.dateRange.to) {
          return false;
        }
      }

      if (
        filters.amountRange?.min !== undefined ||
        filters.amountRange?.max !== undefined
      ) {
        const absAmount = Math.abs(transaction.amount);

        if (
          filters.amountRange.min !== undefined &&
          absAmount < filters.amountRange.min
        ) {
          return false;
        }

        if (
          filters.amountRange.max !== undefined &&
          absAmount > filters.amountRange.max
        ) {
          return false;
        }
      }

      if (
        filters.type &&
        filters.type !== 'all' &&
        getTransactionDirection(transaction) !== filters.type
      ) {
        return false;
      }

      if (
        filters.status &&
        filters.status !== 'all' &&
        transaction.status !== filters.status
      ) {
        return false;
      }

      return true;
    });
  }, [filters]);

  return (
    <div className="space-y-6">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Finance Dashboard</h1>
        <p className="text-muted-foreground">
          Track and manage all your financial transactions in one place
        </p>
      </div>

      <TransactionFilters filters={filters} onFiltersChange={setFilters} />

      <section className="space-y-4">
        <header className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-2xl font-semibold tracking-tight">Transactions</h2>
            <p className="text-muted-foreground">
              {filteredTransactions.length === mockTransactions.length
                ? `${filteredTransactions.length} transactions`
                : `${filteredTransactions.length} of ${mockTransactions.length} transactions`}
            </p>
          </div>
        </header>

        <TransactionTable transactions={filteredTransactions} />
      </section>
    </div>
  );
}
