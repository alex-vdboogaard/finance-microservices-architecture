import { useAccounts } from '../hooks/useAccounts';
import { Card } from '../components/ui/card';
import { Alert, AlertDescription, AlertTitle } from '../components/ui/alert';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '../components/ui/table';
import { Pagination, PaginationContent, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from '../components/ui/pagination';

export function AccountsPage() {
  const {
    accounts,
    loading,
    error,
    page,
    pageSize,
    totalPages,
    totalElements,
    hasNext,
    hasPrevious,
    setPage
  } = useAccounts();

  return (
    <div className="space-y-6">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Accounts</h1>
        <p className="text-muted-foreground">Browse accounts from the account-service.</p>
      </div>

      {error && (
        <Alert variant="destructive">
          <AlertTitle>Unable to load accounts</AlertTitle>
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}

      {loading && !error && (
        <Card className="border-0 bg-accent/10 p-6 text-sm text-muted-foreground">
          Fetching accounts...
        </Card>
      )}

      {!loading && !error && accounts.length === 0 && (
        <Card className="border border-dashed border-border/60 bg-background p-8 text-center">
          <h3 className="mt-4 text-lg font-semibold">No accounts found</h3>
          <p className="mt-2 text-sm text-muted-foreground">
            Once accounts are created, they will appear here.
          </p>
        </Card>
      )}

      {!loading && !error && accounts.length > 0 && (
        <section className="space-y-4">
          <header className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h2 className="text-2xl font-semibold tracking-tight">Account list</h2>
              <p className="text-sm text-muted-foreground">
                {totalElements > 0
                  ? `Showing ${(page - 1) * pageSize + 1} to ${Math.min(page * pageSize, totalElements)} of ${totalElements} accounts`
                  : 'No accounts to display'}
              </p>
            </div>
          </header>

          <div className="overflow-hidden rounded-lg border border-border/60">
            <Table>
              <TableHeader>
                <TableRow className="bg-muted/60">
                  <TableHead className="uppercase tracking-wide text-muted-foreground">ID</TableHead>
                  <TableHead className="uppercase tracking-wide text-muted-foreground">Account Number</TableHead>
                  <TableHead className="uppercase tracking-wide text-muted-foreground">Balance</TableHead>
                  <TableHead className="uppercase tracking-wide text-muted-foreground">Created At</TableHead>
                  <TableHead className="uppercase tracking-wide text-muted-foreground">Updated At</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {accounts.map((account) => (
                  <TableRow key={account.id} className="hover:bg-muted/40">
                    <TableCell>{account.id}</TableCell>
                    <TableCell className="font-mono text-sm">{account.accountNumber}</TableCell>
                    <TableCell>{account.balance.toFixed(2)}</TableCell>
                    <TableCell>{new Date(account.createdAt).toLocaleString()}</TableCell>
                    <TableCell>{new Date(account.updatedAt).toLocaleString()}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {totalPages > 1 && (
            <div className="flex items-center justify-between">
              <Pagination>
                <PaginationContent>
                  <PaginationItem>
                    <PaginationPrevious
                      onClick={() => hasPrevious && setPage(page - 1)}
                      className={!hasPrevious ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                    />
                  </PaginationItem>

                  {Array.from({ length: totalPages }, (_, i) => i + 1)
                    .filter((p) => p === 1 || p === totalPages || Math.abs(p - page) <= 1)
                    .map((p, index, arr) => {
                      const showEllipsisBefore = index > 0 && p - arr[index - 1] > 1;
                      return (
                        <div key={p} className="flex items-center">
                          {showEllipsisBefore && (
                            <PaginationItem>
                              <PaginationLink className="pointer-events-none">
                                ...
                              </PaginationLink>
                            </PaginationItem>
                          )}
                          <PaginationItem>
                            <PaginationLink
                              onClick={() => setPage(p)}
                              isActive={page === p}
                              className="cursor-pointer"
                            >
                              {p}
                            </PaginationLink>
                          </PaginationItem>
                        </div>
                      );
                    })}

                  <PaginationItem>
                    <PaginationNext
                      onClick={() => hasNext && setPage(page + 1)}
                      className={!hasNext ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                    />
                  </PaginationItem>
                </PaginationContent>
              </Pagination>
            </div>
          )}
        </section>
      )}
    </div>
  );
}

