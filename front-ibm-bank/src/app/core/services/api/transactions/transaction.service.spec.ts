import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TransactionService } from './transaction.service';

describe('TransactionService', () => {
  let service: TransactionService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(TransactionService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('posts a transaction', () => {
    const dto = { senderId: null, receiverId: 'abc', amount: 10, transactionType: 'DEPOSIT' };
    service.saveTransaction(dto).subscribe();

    const req = http.expectOne(r => r.url.endsWith('/transactions'));
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(dto);
    req.flush({ message: 'Transaction saved successfully' });
  });

  it('only sends month and year when provided', () => {
    service.getTransactionsByCustomerId('c1', 0, 10, 'timestamp,desc').subscribe();
    const plain = http.expectOne(r => r.url.endsWith('/transactions/c1'));
    expect(plain.request.params.has('month')).toBeFalse();
    plain.flush({ content: [], totalPages: 0, totalElements: 0 });

    service.getTransactionsByCustomerId('c1', 0, 10, 'timestamp,desc', 3, 2024).subscribe();
    const filtered = http.expectOne(r => r.url.endsWith('/transactions/c1'));
    expect(filtered.request.params.get('month')).toBe('3');
    expect(filtered.request.params.get('year')).toBe('2024');
    filtered.flush({ content: [], totalPages: 0, totalElements: 0 });
  });
});
