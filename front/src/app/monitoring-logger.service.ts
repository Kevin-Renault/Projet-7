import { Injectable } from '@angular/core';
import { API_BASE_URL } from './config';

export interface FrontMonitoringEntry {
    label: '[Front]';
    service: 'front';
    component: 'http' | 'error';
    level: 'INFO' | 'ERROR';
    message: string;
    url?: string;
    method?: string;
    status?: number;
    durationMs?: number;
    errorName?: string;
    errorMessage?: string;
}

type FrontMonitoringPayload = Omit<FrontMonitoringEntry, 'label' | 'service' | 'component'>;

const FRONT_MONITORING_ENDPOINT = `${API_BASE_URL}/api/telemetry/front-logs`;

@Injectable({ providedIn: 'root' })
export class MonitoringLoggerService {
    logHttp(entry: FrontMonitoringPayload) {
        this.dispatch({
            label: '[Front]',
            service: 'front',
            component: 'http',
            ...entry,
        });
    }

    logError(entry: FrontMonitoringPayload) {
        this.dispatch({
            label: '[Front]',
            service: 'front',
            component: 'error',
            ...entry,
        });
    }

    private dispatch(entry: FrontMonitoringEntry) {
        void globalThis.fetch(FRONT_MONITORING_ENDPOINT, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(entry),
            keepalive: true,
        }).catch(() => {
            console.warn('[front-monitoring] telemetry endpoint unavailable', entry);
        });
    }
}