import { AlertCircle, History } from 'lucide-react';

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
import type { AuditLog } from '../../types/audit-log';

interface AuditLogTableProps {
  logs: AuditLog[];
  isLoading?: boolean;
  error?: string | null;
}

const dateFormatter = new Intl.DateTimeFormat('en-US', {
  dateStyle: 'medium',
  timeStyle: 'short'
});

export function AuditLogTable({ logs, isLoading = false, error = null }: AuditLogTableProps) {
  if (error) {
    return (
      <Alert variant="destructive">
        <AlertCircle className="h-4 w-4" />
        <AlertTitle>Unable to load audit logs</AlertTitle>
        <AlertDescription>{error}</AlertDescription>
      </Alert>
    );
  }

  if (isLoading) {
    return (
      <Card className="border-0 bg-accent/10 p-6 text-sm text-muted-foreground">
        Fetching the latest audit activity...
      </Card>
    );
  }

  if (logs.length === 0) {
    return (
      <Card className="border border-dashed border-border/60 bg-background p-8 text-center">
        <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-muted">
          <History className="h-6 w-6 text-muted-foreground" />
        </div>
        <h3 className="mt-4 text-lg font-semibold">No audit events found</h3>
        <p className="mt-2 text-sm text-muted-foreground">
          Adjust the filters or generate new activity to populate the audit log.
        </p>
      </Card>
    );
  }

  return (
    <div className="overflow-hidden rounded-lg border border-border/60">
      <Table>
        <TableHeader>
          <TableRow className="bg-muted/60">
            <TableHead className="w-[160px] uppercase tracking-wide text-muted-foreground">
              Timestamp
            </TableHead>
            <TableHead className="uppercase tracking-wide text-muted-foreground">
              Action
            </TableHead>
            <TableHead className="uppercase tracking-wide text-muted-foreground">
              Identifier
            </TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {logs.map((log) => {
            const timestamp = dateFormatter.format(new Date(log.timestamp));

            return (
              <TableRow key={`${log.id}-${log.timestamp}`} className="hover:bg-muted/40">
                <TableCell className="font-medium">{timestamp}</TableCell>
                <TableCell>{log.action}</TableCell>
                <TableCell className="text-muted-foreground">{log.id}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </div>
  );
}
