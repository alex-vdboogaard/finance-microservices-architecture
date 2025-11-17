import { useCallback, useEffect, useState } from 'react';

import type { User } from '../types/user';

const USERS_API_URL = 'http://localhost:8080/api/v1/users';

function extractUsers(payload: unknown): User[] | null {
  if (Array.isArray(payload)) {
    return payload as User[];
  }

  if (typeof payload === 'object' && payload !== null) {
    const obj = payload as { data?: unknown };
    if (Array.isArray(obj.data)) {
      return obj.data as User[];
    }
  }

  return null;
}

interface UseUsersResult {
  users: User[];
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export function useUsers(): UseUsersResult {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);

  const refetch = useCallback(() => {
    setReloadToken((token) => token + 1);
  }, []);

  useEffect(() => {
    const controller = new AbortController();

    const fetchUsers = async () => {
      setLoading(true);
      setError(null);

      try {
        const response = await fetch(USERS_API_URL, { signal: controller.signal });

        if (!response.ok) {
          throw new Error(`Failed to fetch users (${response.status})`);
        }

        const payload = await response.json();
        const resolved = extractUsers(payload);

        if (!resolved) {
          throw new Error('Invalid users response format');
        }

        setUsers(resolved);
      } catch (fetchError) {
        if ((fetchError as Error).name === 'AbortError') return;
        console.error('Failed to load users', fetchError);
        setError(
          fetchError instanceof Error
            ? fetchError.message
            : 'An unexpected error occurred while loading users.'
        );
        setUsers([]);
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();

    return () => controller.abort();
  }, [reloadToken]);

  return { users, loading, error, refetch };
}

