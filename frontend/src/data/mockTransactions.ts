import { Transaction } from '../types/transaction';

export const mockTransactions: Transaction[] = [
  {
    id: '1',
    globalId: 'TXN-2025-001-SAL-5000',
    date: '2025-01-15',
    description: 'Salary Payment',
    amount: 5000,
    status: 'completed',
    paymentMethod: 'Direct Deposit',
    reference: 'REF-2025-001',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '2',
    globalId: 'TXN-2025-002-GRC-127',
    date: '2025-01-14',
    description: 'Grocery Shopping',
    amount: -127.50,
    status: 'completed',
    paymentMethod: 'Credit Card',
    reference: 'REF-2025-002',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '3',
    globalId: 'TXN-2025-003-FRL-800',
    date: '2025-01-13',
    description: 'Freelance Project',
    amount: 800,
    status: 'completed',
    paymentMethod: 'Bank Transfer',
    reference: 'REF-2025-003',
    user: { id: 'USR-1002', fullName: 'Taylor Smith' }
  },
  {
    id: '4',
    globalId: 'TXN-2025-004-UTL-089',
    date: '2025-01-12',
    description: 'Electric Bill',
    amount: -89.99,
    status: 'completed',
    paymentMethod: 'Auto Pay',
    reference: 'REF-2025-004',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '5',
    globalId: 'TXN-2025-005-COF-004',
    date: '2025-01-11',
    description: 'Coffee Shop',
    amount: -4.75,
    status: 'completed',
    paymentMethod: 'Debit Card',
    reference: 'REF-2025-005',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '6',
    globalId: 'TXN-2025-006-INV-150',
    date: '2025-01-10',
    description: 'Investment Dividend',
    amount: 150.25,
    status: 'completed',
    paymentMethod: 'Brokerage',
    reference: 'REF-2025-006',
    user: { id: 'USR-1002', fullName: 'Taylor Smith' }
  },
  {
    id: '7',
    globalId: 'TXN-2025-007-RNT-1200',
    date: '2025-01-09',
    description: 'Rent Payment',
    amount: -1200,
    status: 'completed',
    paymentMethod: 'Bank Transfer',
    reference: 'REF-2025-007',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '8',
    globalId: 'TXN-2025-008-EDU-099',
    date: '2025-01-08',
    description: 'Online Course',
    amount: -99.99,
    status: 'completed',
    paymentMethod: 'Credit Card',
    reference: 'REF-2025-008',
    user: { id: 'USR-1002', fullName: 'Taylor Smith' }
  },
  {
    id: '9',
    globalId: 'TXN-2025-009-UBR-018',
    date: '2025-01-07',
    description: 'Uber Ride',
    amount: -18.50,
    status: 'completed',
    paymentMethod: 'Credit Card',
    reference: 'REF-2025-009',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '10',
    globalId: 'TXN-2025-010-CSH-025',
    date: '2025-01-06',
    description: 'Cashback Reward',
    amount: 25.00,
    status: 'completed',
    paymentMethod: 'Credit Card',
    reference: 'REF-2025-010',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '11',
    globalId: 'TXN-2025-011-NTF-015',
    date: '2025-01-05',
    description: 'Netflix Subscription',
    amount: -15.99,
    status: 'completed',
    paymentMethod: 'Auto Pay',
    reference: 'REF-2025-011',
    user: { id: 'USR-1002', fullName: 'Taylor Smith' }
  },
  {
    id: '12',
    globalId: 'TXN-2025-012-GAS-045',
    date: '2025-01-04',
    description: 'Gas Station',
    amount: -45.20,
    status: 'completed',
    paymentMethod: 'Credit Card',
    reference: 'REF-2025-012',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '13',
    globalId: 'TXN-2025-013-TRF-500',
    date: '2025-01-03',
    description: 'Pending Transfer',
    amount: 500,
    status: 'pending',
    paymentMethod: 'Wire Transfer',
    reference: 'REF-2025-013',
    user: { id: 'USR-1002', fullName: 'Taylor Smith' }
  },
  {
    id: '14',
    globalId: 'TXN-2025-014-GYM-039',
    date: '2025-01-02',
    description: 'Gym Membership',
    amount: -39.99,
    status: 'completed',
    paymentMethod: 'Auto Pay',
    reference: 'REF-2025-014',
    user: { id: 'USR-1001', fullName: 'Alex Johnson' }
  },
  {
    id: '15',
    globalId: 'TXN-2025-015-DIN-085',
    date: '2025-01-01',
    description: 'New Year Dinner',
    amount: -85.30,
    status: 'completed',
    paymentMethod: 'Credit Card',
    reference: 'REF-2025-015',
    user: { id: 'USR-1002', fullName: 'Taylor Smith' }
  }
];
