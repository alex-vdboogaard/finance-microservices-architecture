import { useMemo } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line } from 'recharts';
import { Transaction } from '../types/transaction';
import { getTransactionDirection } from '../utils/transaction-formatters';

interface TransactionChartsProps {
  transactions: Transaction[];
}

export function TransactionCharts({ transactions }: TransactionChartsProps) {
  const chartData = useMemo(() => {
    // Payment method breakdown for expense pie chart
    const paymentMethodTotals = transactions
      .filter((transaction) => transaction.amount < 0 && transaction.status === 'completed')
      .reduce((acc, transaction) => {
        const paymentMethod = transaction.paymentMethod || 'Unknown';
        acc[paymentMethod] = (acc[paymentMethod] || 0) + Math.abs(transaction.amount);
        return acc;
      }, {} as Record<string, number>);

    const totalExpenses = Object.values(paymentMethodTotals).reduce((a, b) => a + b, 0);

    const pieData = Object.entries(paymentMethodTotals).map(([name, value]) => ({
      name,
      value,
      percentage: totalExpenses === 0 ? '0.0' : ((value / totalExpenses) * 100).toFixed(1)
    }));

    // Monthly income vs expenses for bar chart
    const monthlyData = transactions
      .filter(t => t.status === 'completed')
      .reduce((acc, transaction) => {
        const date = new Date(transaction.date);
        const monthKey = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;

        if (!acc[monthKey]) {
          acc[monthKey] = { month: monthKey, income: 0, expenses: 0 };
        }

        if (getTransactionDirection(transaction) === 'income') {
          acc[monthKey].income += transaction.amount;
        } else {
          acc[monthKey].expenses += Math.abs(transaction.amount);
        }
        
        return acc;
      }, {} as Record<string, { month: string; income: number; expenses: number }>);

    const barData = Object.values(monthlyData).sort((a, b) => a.month.localeCompare(b.month));

    // Daily transaction trend for line chart
    const dailyData = transactions
      .filter(t => t.status === 'completed')
      .reduce((acc, transaction) => {
        const date = transaction.date;
        if (!acc[date]) {
          acc[date] = { date, count: 0, totalAmount: 0 };
        }
        acc[date].count += 1;
        acc[date].totalAmount += Math.abs(transaction.amount);
        return acc;
      }, {} as Record<string, { date: string; count: number; totalAmount: number }>);

    const lineData = Object.values(dailyData)
      .sort((a, b) => a.date.localeCompare(b.date))
      .slice(-14); // Last 14 days

    return { pieData, barData, lineData };
  }, [transactions]);

  const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff7300', '#8dd1e1', '#d084d0', '#ffb347'];

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric'
    });
  };

  if (transactions.length === 0) {
    return (
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="border-0 bg-card">
          <CardContent className="p-6 text-center text-muted-foreground">
            No data available for charts
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
      {/* Expenses by Category - Pie Chart */}
      <Card className="border-0 bg-card">
        <CardHeader>
          <CardTitle>Expenses by Payment Method</CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={chartData.pieData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percentage }) => `${name}: ${percentage}%`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {chartData.pieData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip formatter={(value) => formatCurrency(value as number)} />
            </PieChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* Income vs Expenses - Bar Chart */}
      <Card className="border-0 bg-card">
        <CardHeader>
          <CardTitle>Monthly Income vs Expenses</CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={chartData.barData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis tickFormatter={formatCurrency} />
              <Tooltip formatter={(value) => formatCurrency(value as number)} />
              <Legend />
              <Bar dataKey="income" fill="#82ca9d" name="Income" />
              <Bar dataKey="expenses" fill="#ff7300" name="Expenses" />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* Daily Transaction Activity - Combined Chart */}
      <Card className="border-0 bg-card lg:col-span-2">
        <CardHeader>
          <CardTitle>Daily Transaction Activity (Last 14 Days)</CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={chartData.lineData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis 
                dataKey="date" 
                tickFormatter={formatDate}
              />
              <YAxis yAxisId="left" />
              <YAxis yAxisId="right" orientation="right" tickFormatter={formatCurrency} />
              <Tooltip 
                labelFormatter={(label) => formatDate(label as string)}
                formatter={(value, name) => [
                  name === 'totalAmount' ? formatCurrency(value as number) : value,
                  name === 'count' ? 'Transaction Count' : 'Total Amount'
                ]}
              />
              <Legend />
              <Bar yAxisId="left" dataKey="count" fill="#8884d8" name="Transaction Count" />
              <Bar yAxisId="right" dataKey="totalAmount" fill="#ff7300" name="Total Amount" />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>
    </div>
  );
}