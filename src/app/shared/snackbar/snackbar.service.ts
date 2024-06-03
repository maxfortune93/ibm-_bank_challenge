import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class SnackbarService {
  constructor(private _snackBar: MatSnackBar) {}

  success(
    message: string,
    actionText = 'OK',
    timeout = 9000,
    disableAutoClose = false
  ) {
    if (!message) {
      throw new TypeError('message attribute must have a string value');
    }

    const opts: MatSnackBarConfig<any> = {
      verticalPosition: 'top',
      horizontalPosition: 'center',
      panelClass: 'green-snackbar',
    };

    if (!disableAutoClose) {
      opts.duration = timeout;
    }

    return this._snackBar.open(message, actionText, opts);
  }

  info(message: string, actionText = 'OK', disableAutoClose = false) {
    if (!message) {
      throw new TypeError('message attribute must have a string value');
    }

    const opts: MatSnackBarConfig<any> = {
      verticalPosition: 'top',
      horizontalPosition: 'center',
    };

    if (!disableAutoClose) {
      opts.duration = 9000;
    }

    return this._snackBar.open(message, actionText, opts);
  }

  error(message: string, actionText = 'OK', disableAutoClose = false) {
    if (!message) {
      throw new TypeError('message attribute must have a string value');
    }

    const opts: MatSnackBarConfig<any> = {
      verticalPosition: 'top',
      horizontalPosition: 'center',
      panelClass: 'red-snackbar',
    };

    if (!disableAutoClose) {
      opts.duration = 9000;
    }

    return this._snackBar.open(message, actionText, opts);
  }
}
