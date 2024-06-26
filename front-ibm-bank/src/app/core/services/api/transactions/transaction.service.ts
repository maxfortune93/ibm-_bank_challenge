import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction, TransactionDTO } from '../../../models/transaction.model';
import { Page } from 'src/app/core/models/page.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = environment.api + '/transactions';

  constructor(private http: HttpClient) {}

  saveTransaction(transactionDTO: TransactionDTO): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}`, transactionDTO);
  }


  getTransactionsByCustomerId(customerId: string, page: number, size: number, sort: string, month?: number, year?: number): Observable<Page<Transaction>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
      if (month) {
        params = params.set('month', month.toString());
      }

      if (year) {
        params = params.set('year', year.toString());
      }
    return this.http.get<Page<Transaction>>(`${this.apiUrl}/${customerId}`, { params });
  }
}
