import { useState } from 'react';
import type { ReactNode } from 'react';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from './ui/table';
import { Button } from './ui/button';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from './ui/pagination';
import { ArrowUpDown, ArrowUp, ArrowDown, Expand } from 'lucide-react';
import { Transaction } from '../types/transaction';
import { TransactionDetailsDialog } from './TransactionDetailsDialog';
import { formatTransactionAmount, formatTransactionTimestamp } from '../utils/transaction-formatters';

interface TransactionTableProps {
  transactions: Transaction[];
}

type SortField = 'globalId' | 'date' | 'amount';
type SortDirection = 'asc' | 'desc';

const ITEMS_PER_PAGE = 10;

export function TransactionTable({ transactions }: TransactionTableProps) {
  const [sortField, setSortField] = useState<SortField>('date');
  const [sortDirection, setSortDirection] = useState<SortDirection>('desc');
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedTransaction, setSelectedTransaction] = useState<Transaction | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const handleDialogOpenChange = (open: boolean) => {
    setIsDialogOpen(open);
    if (!open) {
      setSelectedTransaction(null);
    }
  };

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortDirection('desc');
    }
    setCurrentPage(1); // Reset to first page when sorting
  };

  const sortedTransactions = [...transactions].sort((a, b) => {
    let aValue: any = a[sortField];
    let bValue: any = b[sortField];

    if (sortField === 'date') {
      aValue = new Date(aValue).getTime();
      bValue = new Date(bValue).getTime();
    } else if (sortField === 'amount') {
      aValue = Math.abs(aValue);
      bValue = Math.abs(bValue);
    }

    if (sortDirection === 'asc') {
      return aValue < bValue ? -1 : aValue > bValue ? 1 : 0;
    } else {
      return aValue > bValue ? -1 : aValue < bValue ? 1 : 0;
    }
  });

  // Calculate pagination
  const totalPages = Math.ceil(sortedTransactions.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = startIndex + ITEMS_PER_PAGE;
  const paginatedTransactions = sortedTransactions.slice(startIndex, endIndex);

  const SortButton = ({ field, children }: { field: SortField; children: ReactNode }) => (
    <Button
      variant="ghost"
      size="sm"
      className="h-auto p-0 hover:bg-transparent font-medium"
      onClick={() => handleSort(field)}
    >
      <span className="flex items-center gap-1">
        {children}
        {sortField === field ? (
          sortDirection === 'desc' ? (
            <ArrowDown className="w-4 h-4" />
          ) : (
            <ArrowUp className="w-4 h-4" />
          )
        ) : (
          <ArrowUpDown className="w-4 h-4 text-muted-foreground" />
        )}
      </span>
    </Button>
  );

  if (transactions.length === 0) {
    return (
      <div className="text-center py-8 text-muted-foreground">
        No transactions found matching your filters.
      </div>
    );
  }

  return (
    <div className="space-y-4">
      <div className="rounded-lg border-0 overflow-hidden bg-card">
        <Table>
          <TableHeader>
            <TableRow className="bg-accent/40 hover:bg-accent/60 border-0">
              <TableHead className="font-medium">
                <SortButton field="globalId">ID</SortButton>
              </TableHead>
              <TableHead className="font-medium">
                <SortButton field="date">Date</SortButton>
              </TableHead>
              <TableHead className="text-right font-medium">
                <SortButton field="amount">Amount</SortButton>
              </TableHead>
              <TableHead className="w-0 text-right font-medium">
                <span className="sr-only">Actions</span>
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {paginatedTransactions.map((transaction) => {
              const { formatted, isPositive } = formatTransactionAmount(transaction.amount);
              return (
                <TableRow key={transaction.id} className="hover:bg-accent/20 border-0">
                  <TableCell className="font-mono text-sm">
                    {transaction.globalId}
                  </TableCell>
                  <TableCell className="text-muted-foreground">
                    {formatTransactionTimestamp(transaction.date)}
                  </TableCell>
                  <TableCell className="text-right font-medium">
                    <span className={isPositive ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'}>
                      {isPositive ? '+' : '-'}{formatted}
                    </span>
                  </TableCell>
                  <TableCell className="w-0 text-right">
                    <Button
                      variant="ghost"
                      size="icon"
                      aria-label="View transaction details"
                      onClick={() => {
                        setSelectedTransaction(transaction);
                        setIsDialogOpen(true);
                      }}
                    >
                      <Expand className="h-4 w-4" />
                    </Button>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </div>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="flex items-center justify-between">
          <p className="text-muted-foreground">
            Showing {startIndex + 1} to {Math.min(endIndex, sortedTransactions.length)} of {sortedTransactions.length} transactions
          </p>
          
          <Pagination>
            <PaginationContent>
              <PaginationItem>
                <PaginationPrevious 
                  onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                  className={currentPage === 1 ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                />
              </PaginationItem>
              
              {Array.from({ length: totalPages }, (_, i) => i + 1)
                .filter(page => {
                  // Show first page, last page, current page, and pages around current
                  return page === 1 || 
                         page === totalPages || 
                         Math.abs(page - currentPage) <= 1;
                })
                .map((page, index, array) => {
                  // Add ellipsis if there's a gap
                  const showEllipsisBefore = index > 0 && page - array[index - 1] > 1;
                  
                  return (
                    <div key={page} className="flex items-center">
                      {showEllipsisBefore && (
                        <PaginationItem>
                          <PaginationEllipsis />
                        </PaginationItem>
                      )}
                      <PaginationItem>
                        <PaginationLink
                          onClick={() => setCurrentPage(page)}
                          isActive={currentPage === page}
                          className="cursor-pointer"
                        >
                          {page}
                        </PaginationLink>
                      </PaginationItem>
                    </div>
                  );
                })}
              
              <PaginationItem>
                <PaginationNext 
                  onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                  className={currentPage === totalPages ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                />
              </PaginationItem>
            </PaginationContent>
          </Pagination>
        </div>
      )}

      <TransactionDetailsDialog
        open={isDialogOpen}
        transaction={selectedTransaction}
        onOpenChange={handleDialogOpenChange}
      />
    </div>
  );
}
