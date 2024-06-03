import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../../../models/customer.model';
import { Transaction } from '../../../models/transaction.model';
import { environment } from 'src/environments/environment';
import { Page } from 'src/app/core/models/page.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl = environment.api + '/customers';
  constructor(private http: HttpClient) {}

  listCustomers(page: number, size: number, searchTerm: string): Observable<Page<Customer>> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());
    params = params.append('searchTerm', searchTerm);

    return this.http.get<Page<Customer>>(this.apiUrl, { params });
  }

  saveCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(`${this.apiUrl}`, customer);
  }

  getCustomerById(id: string): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`);
  }

  getTransactionsByCustomerId(id: string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/${id}/transactions`);
  }

  autocompleteCustomers(query: string, limit: number): Observable<Customer[]> {
    let params = new HttpParams().set('query', query).set('limit', limit.toString());
    return this.http.get<Customer[]>(`${this.apiUrl}/autocomplete`, { params });
  }

}
