import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../core/services/api/customers/customer.service';
import { TransactionService } from '../core/services/api/transactions/transaction.service';
import { Customer } from '../core/models/customer.model';
import { Transaction } from '../core/models/transaction.model';
import { initials, formatNumberWithHyphen, formattedCurrency } from '../shared/utils/formater-utils';
import { PageEvent } from '@angular/material/paginator';
import { Location } from '@angular/common';
import { SnackbarService } from '../shared/snackbar/snackbar.service';
import { FormBuilder, FormGroup } from '@angular/forms';



@Component({
  selector: 'app-customer-details',
  templateUrl: './customer-details.component.html',
  styleUrl: './customer-details.component.scss'
})

export class CustomerDetailsComponent implements OnInit {

  formatNumberWithHyphen = formatNumberWithHyphen;
  initials = initials;
  months = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
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
    private snackbarService: SnackbarService,
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
      this.customerService.getCustomerById(customerId).subscribe({
        next: customer => {
          this.customer = customer;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
          this.goBack();
        }
      });
      this.loadTransactions(customerId);
    }
  }

  loadTransactions(customerId: string): void {
    const sort = 'timestamp,desc';
    const month = this.filterForm.get('month')?.value;
    const year = this.filterForm.get('year')?.value;
    this.transactionService.getTransactionsByCustomerId(customerId, this.page, this.size, sort,month, year).subscribe({
      next: page => {
        this.transactions = page.content;
        this.totalPages = page.totalPages;
        this.totalElements = page.totalElements;
      },
      error: () => {
        this.transactions = [];
      }
    });
  }

  onFilterChange(): void {
    this.page = 0;
    // The API filters by month + year together, so a month alone means "this year".
    const { month, year } = this.filterForm.value;
    if (month && !year) {
      this.filterForm.patchValue({ year: new Date().getFullYear() });
    } else if (year && !month) {
      this.snackbarService.info('Selecione também o mês para filtrar.');
      return;
    }
    this.loadTransactions(this.customer.id!);
  }

  onFilterClear(): void {
    this.page = 0;
    this.filterForm.reset({
      month: '',
      year: ''
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
