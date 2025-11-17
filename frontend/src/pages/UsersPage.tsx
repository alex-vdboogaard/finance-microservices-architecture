import { useUsers } from '../hooks/useUsers';
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

export function UsersPage() {
  const { users, loading, error } = useUsers();

  return (
    <div className="space-y-6">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Users</h1>
        <p className="text-muted-foreground">View users managed by the account-service.</p>
      </div>

      {error && (
        <Alert variant="destructive">
          <AlertTitle>Unable to load users</AlertTitle>
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}

      {loading && !error && (
        <Card className="border-0 bg-accent/10 p-6 text-sm text-muted-foreground">
          Fetching users...
        </Card>
      )}

      {!loading && !error && users.length === 0 && (
        <Card className="border border-dashed border-border/60 bg-background p-8 text-center">
          <h3 className="mt-4 text-lg font-semibold">No users found</h3>
          <p className="mt-2 text-sm text-muted-foreground">
            Once users are created, they will appear here.
          </p>
        </Card>
      )}

      {!loading && !error && users.length > 0 && (
        <section className="space-y-4">
          <header className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h2 className="text-2xl font-semibold tracking-tight">User list</h2>
              <p className="text-sm text-muted-foreground">
                {users.length} user{users.length === 1 ? '' : 's'}
              </p>
            </div>
          </header>

          <div className="overflow-hidden rounded-lg border border-border/60">
            <Table>
              <TableHeader>
                <TableRow className="bg-muted/60">
                  <TableHead className="uppercase tracking-wide text-muted-foreground">ID</TableHead>
                  <TableHead className="uppercase tracking-wide text-muted-foreground">Name</TableHead>
                  <TableHead className="uppercase tracking-wide text-muted-foreground">Email</TableHead>
                  <TableHead className="uppercase tracking-wide text-muted-foreground">Government ID</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {users.map((user) => (
                  <TableRow key={user.userId} className="hover:bg-muted/40">
                    <TableCell>{user.userId}</TableCell>
                    <TableCell>{`${user.firstName} ${user.lastName}`}</TableCell>
                    <TableCell>{user.email}</TableCell>
                    <TableCell>{user.governmentId}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </section>
      )}
    </div>
  );
}

