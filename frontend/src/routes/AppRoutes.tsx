import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';

import { AppLayout } from '../layouts/AppLayout';
import { AuditLogPage } from '../pages/AuditLogPage';
import { TransactionsPage } from '../pages/TransactionsPage';
import { AccountsPage } from '../pages/AccountsPage';
import { UsersPage } from '../pages/UsersPage';
import { NotificationsPage } from '../pages/NotificationsPage';

export function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<AppLayout />}>
        <Route index element={<TransactionsPage />} />
        <Route path="transactions" element={<TransactionsPage />} />
        <Route path="accounts" element={<AccountsPage />} />
        <Route path="users" element={<UsersPage />} />
        <Route path="notifications" element={<NotificationsPage />} />
        <Route path="audit-log" element={<AuditLogPage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}
