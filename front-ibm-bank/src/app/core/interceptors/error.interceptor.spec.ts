import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { errorInterceptor } from './error.interceptor';
import { SnackbarService } from '../../shared/snackbar/snackbar.service';

describe('errorInterceptor', () => {
  let http: HttpClient;
  let controller: HttpTestingController;
  let snackbar: jasmine.SpyObj<SnackbarService>;

  beforeEach(() => {
    snackbar = jasmine.createSpyObj('SnackbarService', ['error']);
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([errorInterceptor])),
        provideHttpClientTesting(),
        { provide: SnackbarService, useValue: snackbar }
      ]
    });
    http = TestBed.inject(HttpClient);
    controller = TestBed.inject(HttpTestingController);
  });

  it('shows the API message and re-throws', () => {
    let failed = false;
    http.get('/api/x').subscribe({ error: () => (failed = true) });

    controller.expectOne('/api/x').flush({ message: 'Saldo insuficiente' }, { status: 400, statusText: 'Bad Request' });

    expect(failed).toBeTrue();
    expect(snackbar.error).toHaveBeenCalledWith('Saldo insuficiente');
  });

  it('shows a connection message when the server is unreachable', () => {
    http.get('/api/x').subscribe({ error: () => {} });

    controller.expectOne('/api/x').error(new ProgressEvent('error'));

    expect(snackbar.error).toHaveBeenCalledWith('Não foi possível conectar ao servidor');
  });
});
