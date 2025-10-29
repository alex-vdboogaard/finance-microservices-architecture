import { AlertCircle, Receipt } from 'lucide-react';

import { Alert, AlertDescription, AlertTitle } from '../ui/alert';
import { Card } from '../ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '../ui/table';
import type { TransferEvent } from '../../types/transfer-event';
import { formatTransactionAmount, formatTransactionTimestamp } from '../../utils/transaction-formatters';

interface TransactionTableProps {
  events: TransferEvent[];
  isLoading?: boolean;
  error?: string | null;
}

export function TransactionTable({ events, isLoading = false, error = null }: TransactionTableProps) {
  if (error) {
    return (
      <Alert variant="destructive">
        <AlertCircle className="h-4 w-4" />
        <AlertTitle>Unable to load transactions</AlertTitle>
        <AlertDescription>{error}</AlertDescription>
      </Alert>
    );
  }

  if (isLoading) {
    return (
      <Card className="border-0 bg-accent/10 p-6 text-sm text-muted-foreground">
        Fetching the latest transactions...
      </Card>
    );
  }

  if (events.length === 0) {
    return (
      <Card className="border border-dashed border-border/60 bg-background p-8 text-center">
        <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-muted">
          <Receipt className="h-6 w-6 text-muted-foreground" />
        </div>
        <h3 className="mt-4 text-lg font-semibold">No transactions found</h3>
        <p className="mt-2 text-sm text-muted-foreground">
          Adjust the filters or trigger new activity to populate transactions.
        </p>
      </Card>
    );
  }

  const getStatusBadge = (status: string) => {
    const normalized = status.toLowerCase();
    const styles =
      normalized === 'completed' || normalized === 'success'
        ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200'
        : normalized === 'pending'
          ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200'
          : 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
    return <span className={`inline-flex rounded-full px-2 py-1 text-xs font-medium ${styles}`}>{status}</span>;
  };

  const sorted = [...events].sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());

  return (
    <div className="overflow-hidden rounded-lg border border-border/60">
      <Table>
        <TableHeader>
          <TableRow className="bg-muted/60">
            <TableHead className="w-[240px] uppercase tracking-wide text-muted-foreground">Transaction ID</TableHead>
            <TableHead className="w-[160px] uppercase tracking-wide text-muted-foreground">Timestamp</TableHead>
            <TableHead className="uppercase tracking-wide text-muted-foreground">From</TableHead>
            <TableHead className="uppercase tracking-wide text-muted-foreground">To</TableHead>
            <TableHead className="uppercase tracking-wide text-muted-foreground">Amount</TableHead>
            <TableHead className="uppercase tracking-wide text-muted-foreground">Status</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {sorted.map((ev) => {
            const { formatted, isPositive } = formatTransactionAmount(ev.amount);
            const ts = formatTransactionTimestamp(ev.timestamp);
            return (
              <TableRow key={`${ev.transactionId}-${ev.timestamp}`} className="hover:bg-muted/40">
                <TableCell className="font-medium text-muted-foreground">{ev.transactionId}</TableCell>
                <TableCell className="font-medium">{ts}</TableCell>
                <TableCell>{ev.fromAccountId}</TableCell>
                <TableCell>{ev.toAccountId}</TableCell>
                <TableCell className={isPositive ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'}>
                  {isPositive ? '+' : '-'}{formatted}
                </TableCell>
                <TableCell>{getStatusBadge(ev.status)}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </div>
  );
}

