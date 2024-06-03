import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../core/services/api/customers/customer.service';
import { TransactionService } from '../core/services/api/transactions/transaction.service';
import { Customer } from '../core/models/customer.model';
import { Transaction } from '../core/models/transaction.model';
import { formatNumberWithHyphen } from '../shared/utils/formater-utils';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrl: './customer-details.component.scss'
})

export class CustomerDetailsComponent implements OnInit {

  formatNumberWithHyphen = formatNumberWithHyphen;
  customer!: Customer;
  transactions: Transaction[] = [];
  page: number = 0;
  size: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private transactionService: TransactionService
  ) {}

  ngOnInit(): void {
    const customerId = this.route.snapshot.paramMap.get('id');
    if (customerId) {
      this.customerService.getCustomerById(customerId).subscribe(customer => {
        this.customer = customer;
      });
      this.loadTransactions(customerId);
    }
  }

  loadTransactions(customerId: string): void {
    const sort = 'timestamp,desc';
    this.transactionService.getTransactionsByCustomerId(customerId, this.page, this.size, sort).subscribe(page => {
      this.transactions = page.content;
      this.totalPages = page.totalPages;
      this.totalElements = page.totalElements;
    });
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.loadTransactions(this.customer.id!);
  }
}
