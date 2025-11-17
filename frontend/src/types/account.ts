export interface Account {
  id: number;
  accountNumber: string;
  balance: number;
  createdAt: string;
  updatedAt: string;
}

export interface AccountPage {
  content: Account[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}

