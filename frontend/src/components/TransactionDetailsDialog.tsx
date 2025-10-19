import type { ReactNode } from 'react';
import { Transaction } from '../types/transaction';
import { Badge } from './ui/badge';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from './ui/dialog';
import { Separator } from './ui/separator';
import {
  formatTransactionAmount,
  formatTransactionTimestamp,
  getTransactionDirection,
  getTransactionStatusColor
} from '../utils/transaction-formatters';

interface TransactionDetailsDialogProps {
  open: boolean;
  transaction: Transaction | null;
  onOpenChange: (open: boolean) => void;
}

function DetailItem({ label, value }: { label: string; value: ReactNode }) {
  return (
    <div className="space-y-1">
      <p className="text-sm font-medium text-muted-foreground">{label}</p>
      <div className="text-sm font-semibold text-foreground">{value}</div>
    </div>
  );
}

export function TransactionDetailsDialog({
  open,
  transaction,
  onOpenChange
}: TransactionDetailsDialogProps) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      {transaction ? (
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Transaction Details</DialogTitle>
            <DialogDescription>
              {formatTransactionTimestamp(transaction.date)}
            </DialogDescription>
          </DialogHeader>

          <div className="space-y-6">
            <div className="space-y-2">
              <p className="text-sm font-medium text-muted-foreground">Amount</p>
              {(() => {
                const { formatted, isPositive } = formatTransactionAmount(transaction.amount);
                return (
                  <p
                    className={`text-2xl font-semibold ${
                      isPositive
                        ? 'text-green-600 dark:text-green-400'
                        : 'text-red-600 dark:text-red-400'
                    }`}
                  >
                    {isPositive ? '+' : '-'}{formatted}
                  </p>
                );
              })()}
              <Badge className={`border-0 capitalize ${getTransactionStatusColor(transaction.status)}`}>
                {transaction.status}
              </Badge>
            </div>

            <Separator />

            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <DetailItem label="Transaction ID" value={transaction.globalId} />
              <DetailItem label="Reference" value={transaction.reference} />
              <DetailItem label="Description" value={transaction.description} />
              <DetailItem
                label="Direction"
                value={getTransactionDirection(transaction) === 'income' ? 'Incoming' : 'Outgoing'}
              />
              <DetailItem label="Payment Method" value={transaction.paymentMethod} />
              <DetailItem
                label="Initiated By"
                value={
                  <span>
                    {transaction.user.fullName}
                    <span className="block text-xs text-muted-foreground">ID: {transaction.user.id}</span>
                  </span>
                }
              />
            </div>
          </div>
        </DialogContent>
      ) : null}
    </Dialog>
  );
}

