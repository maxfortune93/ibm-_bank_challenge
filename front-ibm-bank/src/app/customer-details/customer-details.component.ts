import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../core/services/api/customers/customer.service';
import { TransactionService } from '../core/services/api/transactions/transaction.service';
import { Customer } from '../core/models/customer.model';
import { Transaction } from '../core/models/transaction.model';
import { formatNumberWithHyphen, formattedCurrency } from '../shared/utils/formater-utils';
import { PageEvent } from '@angular/material/paginator';
import { Location } from '@angular/common';
import { FormBuilder, FormGroup } from '@angular/forms';



@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrl: './customer-details.component.scss'
})

export class CustomerDetailsComponent implements OnInit {

  formatNumberWithHyphen = formatNumberWithHyphen;
  formattedCurrency = formattedCurrency;
  customer!: Customer;
  transactions: Transaction[] = [];
  page: number = 0;
  size: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;
  filterForm!: FormGroup;

  isLoading: boolean = false;
  loadingMessage = 'Carregando dados, por favor aguarde...';

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private transactionService: TransactionService,
    private location: Location,
    private fb: FormBuilder,
  ) {
    this.filterForm = this.fb.group({
      month: [''],
      year: ['']
    });
  }

  ngOnInit(): void {
    const customerId = this.route.snapshot.paramMap.get('id');
    if (customerId) {
      this.isLoading = true;
        this.customerService.getCustomerById(customerId).subscribe(customer => {
          this.customer = customer;
          this.isLoading = false
        });
      this.loadTransactions(customerId);
    }
  }

  loadTransactions(customerId: string): void {
    const sort = 'timestamp,desc';
    const month = this.filterForm.get('month')?.value;
    const year = this.filterForm.get('year')?.value;
    this.transactionService.getTransactionsByCustomerId(customerId, this.page, this.size, sort,month, year).subscribe(page => {
      this.transactions = page.content;
      this.totalPages = page.totalPages;
      this.totalElements = page.totalElements;
    });
  }

  onFilterChange(): void {
    this.page = 0;
    this.loadTransactions(this.customer.id!);
  }

  onFilterClear(): void {
    this.page = 0;
    this.filterForm.reset({
      month: [''],
      year: ['']
    });
    this.loadTransactions(this.customer.id!);
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.loadTransactions(this.customer.id!);
  }

  goBack(): void {
    this.location.back();
  }
}
