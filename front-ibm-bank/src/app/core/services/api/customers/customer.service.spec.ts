import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CustomerService } from './customer.service';

describe('CustomerService', () => {
  let service: CustomerService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(CustomerService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('lists customers with pagination and search params', () => {
    service.listCustomers(1, 5, 'ana').subscribe();

    const req = http.expectOne(r => r.url.endsWith('/customers'));
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('page')).toBe('1');
    expect(req.request.params.get('size')).toBe('5');
    expect(req.request.params.get('searchTerm')).toBe('ana');
    req.flush({ content: [], totalPages: 0, totalElements: 0 });
  });

  it('posts a new customer', () => {
    const customer = { name: 'Ana', age: 30, email: 'a@a.com', bankName: 'B', branch: '1', accountNumber: '1-1', balance: 0 };
    service.saveCustomer(customer).subscribe();

    const req = http.expectOne(r => r.url.endsWith('/customers'));
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(customer);
    req.flush(customer);
  });

  it('autocompletes by name with a limit', () => {
    service.autocompleteCustomers('jo', 5).subscribe();

    const req = http.expectOne(r => r.url.endsWith('/customers/autocomplete'));
    expect(req.request.params.get('query')).toBe('jo');
    expect(req.request.params.get('limit')).toBe('5');
    req.flush([]);
  });
});
