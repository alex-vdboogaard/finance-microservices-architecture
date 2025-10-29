export interface TransferEvent {
  transactionId: string;
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  status: string;
  timestamp: string; // ISO datetime string
}

export interface TransferEventFilters {
  search?: string;
  dateRange?: {
    from?: Date;
    to?: Date;
  };
}

// Server pagination metadata returned by the backend
export interface TransferEventPage {
  content: TransferEvent[];
  page: number; // zero-based page index from backend
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}
