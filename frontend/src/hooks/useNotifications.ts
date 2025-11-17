import { useCallback, useEffect, useState } from 'react';

import type { Notification } from '../types/notification';

const NOTIFICATIONS_API_URL = 'http://localhost:8080/api/v1/notifications';

function extractNotifications(payload: unknown): Notification[] | null {
  if (Array.isArray(payload)) {
    return payload as Notification[];
  }

  if (typeof payload === 'object' && payload !== null) {
    const obj = payload as { data?: unknown };
    if (Array.isArray(obj.data)) {
      return obj.data as Notification[];
    }
  }

  return null;
}

interface UseNotificationsResult {
  notifications: Notification[];
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export function useNotifications(): UseNotificationsResult {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [reloadToken, setReloadToken] = useState(0);

  const refetch = useCallback(() => {
    setReloadToken((token) => token + 1);
  }, []);

  useEffect(() => {
    const controller = new AbortController();

    const fetchNotifications = async () => {
      setLoading(true);
      setError(null);

      try {
        const response = await fetch(NOTIFICATIONS_API_URL, { signal: controller.signal });

        if (!response.ok) {
          throw new Error(`Failed to fetch notifications (${response.status})`);
        }

        const payload = await response.json();
        const resolved = extractNotifications(payload);

        if (!resolved) {
          throw new Error('Invalid notifications response format');
        }

        setNotifications(resolved);
      } catch (fetchError) {
        if ((fetchError as Error).name === 'AbortError') return;
        console.error('Failed to load notifications', fetchError);
        setError(
          fetchError instanceof Error
            ? fetchError.message
            : 'An unexpected error occurred while loading notifications.'
        );
        setNotifications([]);
      } finally {
        setLoading(false);
      }
    };

    fetchNotifications();

    return () => controller.abort();
  }, [reloadToken]);

  return { notifications, loading, error, refetch };
}

