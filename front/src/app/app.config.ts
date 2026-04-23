import { ApplicationConfig, ErrorHandler } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';


import { routes } from './app.routes';
import { MonitoringErrorHandler } from './monitoring-error-handler';
import { monitoringHttpInterceptor } from './monitoring-http.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withFetch(), withInterceptors([monitoringHttpInterceptor])),
    provideRouter(routes),
    {
      provide: ErrorHandler,
      useClass: MonitoringErrorHandler,
    },
  ]
};
