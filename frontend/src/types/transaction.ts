export interface TransactionUser {
  id: string;
  fullName: string;
}

export interface Transaction {
  id: string;
  globalId: string; // Global unique identifier
  date: string;
  description: string;
  amount: number;
  status: 'completed' | 'pending' | 'failed';
  paymentMethod: string;
  reference: string;
  user: TransactionUser;
}

export interface TransactionFilters {
  type?: 'income' | 'expense' | 'all';
  status?: 'completed' | 'pending' | 'failed' | 'all';
  dateRange?: {
    from?: Date;
    to?: Date;
  };
  amountRange?: {
    min?: number;
    max?: number;
  };
  search?: string;
}
