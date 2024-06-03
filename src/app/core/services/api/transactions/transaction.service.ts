import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction, TransactionDTO } from '../../../models/transaction.model';
import { Page } from 'src/app/core/models/page.model';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = 'api/transactions';

  constructor(private http: HttpClient) {}

  saveTransaction(transactionDTO: TransactionDTO): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}`, transactionDTO);
  }


  getTransactionsByCustomerId(customerId: string, page: number, size: number, sort: string): Observable<Page<Transaction>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<Transaction>>(`${this.apiUrl}/${customerId}`, { params });
  }
}
