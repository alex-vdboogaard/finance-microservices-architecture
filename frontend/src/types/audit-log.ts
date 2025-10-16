export interface AuditLog {
  id: number;
  action: string;
  timestamp: string;
  service?: string | null;
  user?: string | null;
  metadata?: Record<string, unknown> | null;
}

export interface AuditLogFilters {
  search?: string;
  service?: string;
  user?: string;
  dateRange?: {
    from?: Date;
    to?: Date;
  };
}
