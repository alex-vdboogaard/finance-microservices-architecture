import { Navigate, Route, Routes } from 'react-router-dom';
import React from "react"

import { AppLayout } from '../layouts/AppLayout';
import { AuditLogPage } from '../pages/AuditLogPage';
import { DashboardPage } from '../pages/DashboardPage';

export function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<AppLayout />}>
        <Route index element={<DashboardPage />} />
        <Route path="audit-log" element={<AuditLogPage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}
