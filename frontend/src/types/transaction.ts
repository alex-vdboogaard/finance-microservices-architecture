export interface TransactionUser {
  id: string;
  fullName: string;
}

export interface Transaction {
  id: string;
  globalId: string; // Global unique identifier
  date: string;
  description: string;
  category: string;
  amount: number;
  type: 'income' | 'expense';
  status: 'completed' | 'pending' | 'failed';
  paymentMethod: string;
  tags: string[];
  reference: string;
  accountName: string;
  user: TransactionUser;
}

export interface TransactionFilters {
  category?: string;
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
