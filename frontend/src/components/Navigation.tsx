import { NavLink } from 'react-router-dom';

import { cn } from './ui/utils';

const navigationLinks = [
  { label: 'Dashboard', to: '/' },
  { label: 'Transactions', to: '/transactions' },
  { label: 'Audit Log', to: '/audit-log' }
];

export function Navigation() {
  return (
    <header className="border-b border-border/60 bg-card/60 backdrop-blur supports-[backdrop-filter]:bg-card/40">
      <a
        href="#main-content"
        className="sr-only focus:not-sr-only focus:absolute focus:top-3 focus:left-3 focus:z-50 focus:rounded-md focus:bg-primary focus:px-3 focus:py-2 focus:text-primary-foreground"
      >
        Skip to content
      </a>
      <div className="max-w-7xl mx-auto flex flex-col gap-4 px-6 py-4 md:flex-row md:items-center md:justify-between">
        <div className="flex items-center gap-3">
          <span className="inline-flex h-9 w-9 items-center justify-center rounded-lg bg-primary text-lg font-semibold text-primary-foreground shadow-sm">
            F
          </span>
          <div>
            <p className="text-lg font-semibold tracking-tight">Finance Platform</p>
            <p className="text-sm text-muted-foreground">Monitor transactions and audit activity</p>
          </div>
        </div>

        <nav className="flex items-center gap-1 rounded-full bg-muted/40 p-1 text-sm font-medium">
          {navigationLinks.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.to === '/'}
              className={({ isActive }) =>
                cn(
                  'inline-flex items-center gap-2 rounded-full px-4 py-2 transition-colors',
                  isActive
                    ? 'bg-background text-foreground shadow-sm'
                    : 'text-muted-foreground hover:text-foreground'
                )
              }
            >
              {link.label}
            </NavLink>
          ))}
        </nav>
      </div>
    </header>
  );
}
