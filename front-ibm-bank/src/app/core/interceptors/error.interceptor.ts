import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { SnackbarService } from '../../shared/snackbar/snackbar.service';

/** Shows a snackbar for every failed API call and re-throws so callers can reset their state. */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackbar = inject(SnackbarService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      snackbar.error(extractErrorMessage(error));
      return throwError(() => error);
    })
  );
};

export function extractErrorMessage(error: HttpErrorResponse): string {
  if (error.status === 0) {
    return 'Não foi possível conectar ao servidor';
  }
  return error.error?.message || 'Ocorreu um erro inesperado';
}
