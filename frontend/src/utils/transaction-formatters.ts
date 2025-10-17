import { Transaction } from '../types/transaction';

export function formatTransactionAmount(amount: number) {
  const isPositive = amount >= 0;
  const formatted = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(Math.abs(amount));

  return { formatted, isPositive };
}

export function formatTransactionTimestamp(dateString: string) {
  return new Date(dateString).toLocaleString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: true
  });
}

export function getTransactionStatusColor(status: Transaction['status']) {
  switch (status) {
    case 'completed':
      return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
    case 'pending':
      return 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200';
    case 'failed':
      return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
    default:
      return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
  }
}

export function getTransactionDirection(transaction: Pick<Transaction, 'amount'>) {
  return transaction.amount >= 0 ? 'income' : 'expense';
}

