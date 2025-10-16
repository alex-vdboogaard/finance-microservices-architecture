import { Outlet } from 'react-router-dom';

import { Navigation } from '../components/Navigation';

export function AppLayout() {
  return (
    <div className="min-h-screen bg-background text-foreground">
      <Navigation />
      <main id="main-content" className="max-w-7xl mx-auto p-6">
        <Outlet />
      </main>
    </div>
  );
}
