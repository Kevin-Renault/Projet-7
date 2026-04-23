import { HttpErrorResponse, HttpEvent, HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize, tap } from 'rxjs';
import { MonitoringLoggerService } from './monitoring-logger.service';

export const monitoringHttpInterceptor: HttpInterceptorFn = (request, next) => {
    const monitoringLogger = inject(MonitoringLoggerService);
    const startedAt = performance.now();
    let status: number | undefined;
    let level: 'INFO' | 'ERROR' = 'INFO';
    let message = 'HTTP request completed';

    return next(request).pipe(
        tap({
            next: (event: HttpEvent<unknown>) => {
                if (event instanceof HttpResponse) {
                    status = event.status;
                }
            },
            error: (error: unknown) => {
                if (error instanceof HttpErrorResponse) {
                    status = error.status;
                    level = 'ERROR';
                    message = 'HTTP request failed';
                }
            },
        }),
        finalize(() => {
            monitoringLogger.logHttp({
                level,
                message,
                method: request.method,
                url: stripQueryString(request.url),
                status,
                durationMs: Math.round(performance.now() - startedAt),
            });
        })
    );
};

function stripQueryString(url: string): string {
    return url.split('?')[0];
}