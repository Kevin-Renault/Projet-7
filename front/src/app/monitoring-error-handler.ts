import { ErrorHandler, Injectable } from '@angular/core';
import { MonitoringLoggerService } from './monitoring-logger.service';

@Injectable()
export class MonitoringErrorHandler implements ErrorHandler {
    constructor(private readonly monitoringLogger: MonitoringLoggerService) { }

    handleError(error: unknown): void {
        const normalizedError = error instanceof Error ? error : new Error(describeError(error));

        this.monitoringLogger.logError({
            level: 'ERROR',
            message: normalizedError.message,
            errorName: normalizedError.name,
            errorMessage: normalizedError.stack,
        });

        console.error(normalizedError);
    }
}

function describeError(error: unknown): string {
    if (typeof error === 'string') {
        return error;
    }

    try {
        return JSON.stringify(error);
    } catch {
        return 'Unknown front error';
    }
}