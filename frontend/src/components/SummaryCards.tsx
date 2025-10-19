import { Card, CardContent } from './ui/card';
import { TrendingUp, TrendingDown, DollarSign, Activity } from 'lucide-react';
import { Transaction } from '../types/transaction';
import { getTransactionDirection } from '../utils/transaction-formatters';

interface SummaryCardsProps {
  transactions: Transaction[];
}

export function SummaryCards({ transactions }: SummaryCardsProps) {
  const totalIncome = transactions
    .filter(t => getTransactionDirection(t) === 'income' && t.status === 'completed')
    .reduce((sum, t) => sum + t.amount, 0);

  const totalExpenses = Math.abs(transactions
    .filter(t => getTransactionDirection(t) === 'expense' && t.status === 'completed')
    .reduce((sum, t) => sum + t.amount, 0));

  const netBalance = totalIncome - totalExpenses;
  const totalTransactions = transactions.length;

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const cards = [
    {
      title: 'Total Income',
      value: formatCurrency(totalIncome),
      icon: TrendingUp,
      color: 'text-green-600 dark:text-green-400',
      bgColor: 'bg-green-100 dark:bg-green-900/20'
    },
    {
      title: 'Total Expenses',
      value: formatCurrency(totalExpenses),
      icon: TrendingDown,
      color: 'text-red-600 dark:text-red-400',
      bgColor: 'bg-red-100 dark:bg-red-900/20'
    },
    {
      title: 'Net Balance',
      value: formatCurrency(netBalance),
      icon: DollarSign,
      color: netBalance >= 0 ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400',
      bgColor: netBalance >= 0 ? 'bg-green-100 dark:bg-green-900/20' : 'bg-red-100 dark:bg-red-900/20'
    },
    {
      title: 'Total Transactions',
      value: totalTransactions.toString(),
      icon: Activity,
      color: 'text-blue-600 dark:text-blue-400',
      bgColor: 'bg-blue-100 dark:bg-blue-900/20'
    }
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      {cards.map((card, index) => {
        const Icon = card.icon;
        return (
          <Card key={index} className="border-0 bg-card hover:bg-accent/20 transition-colors">
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-muted-foreground mb-2">{card.title}</p>
                  <p className={`font-medium ${card.color}`}>{card.value}</p>
                </div>
                <div className={`p-2 rounded-lg ${card.bgColor}`}>
                  <Icon className={`w-5 h-5 ${card.color}`} />
                </div>
              </div>
            </CardContent>
          </Card>
        );
      })}
    </div>
  );
}