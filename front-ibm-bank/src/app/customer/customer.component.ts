import { Component, DestroyRef, HostListener, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { CustomerRegisterComponent } from '../customer-register/customer-register.component';
import { Router } from '@angular/router';
import { CustomerService } from '../core/services/api/customers/customer.service';
import { Subscription, interval, take } from 'rxjs';
import { Customer } from '../core/models/customer.model';
import { PageEvent } from '@angular/material/paginator';
import { Page } from '../core/models/page.model';
import { formatNumberWithHyphen } from '../shared/utils/formater-utils';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.scss'
})

export class CustomerComponent {

  formatNumberWithHyphen = formatNumberWithHyphen;
  displayedColumns: string[] = ['name', 'email', 'bankName', 'accountNumber', 'actions'];
  customers: Customer[] = [];
  visibleCustomers: Customer[] = [];
  totalPages: number = 0;
  totalElements: number = 0;
  size: number = 5;
  page: number = 0;
  searchTerm: string = '';

  isLoading: boolean = false;
  loadingMessage = 'Carregando dados, por favor aguarde...';

  public applyTooltip: boolean = true;

  private animation?: Subscription;
  private readonly destroyRef = inject(DestroyRef);

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private customerService: CustomerService
  ) {
    this.applyTooltip = window.innerWidth > 768;
  }

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.isLoading = true;
    this.customerService.listCustomers(this.page, this.size, this.searchTerm).subscribe({
      next: (data: Page<Customer>) => {
        this.customers = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.isLoading = false;
        this.visibleCustomers = [];
        this.animateCustomerVisibility();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  animateCustomerVisibility(): void {
    this.animation?.unsubscribe();
    this.animation = interval(100)
      .pipe(take(this.customers.length), takeUntilDestroyed(this.destroyRef))
      .subscribe((index) => {
        this.visibleCustomers.push(this.customers[index]);
      });
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.loadCustomers();
  }

  openRegisterModal(): void {
    const dialogRef = this.dialog.open(CustomerRegisterComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result?.id) this.loadCustomers();
    });
  }

  onSearchChanged(searchTerm: string) {
    this.searchTerm = searchTerm;
    this.page = 0;
    this.loadCustomers();
  }

  viewCustomerDetails(customerId: string): void {
    this.router.navigate(['/customers', customerId]);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: { target: { innerWidth: number; }; }) {
    this.applyTooltip = event.target.innerWidth > 768;
  }

}
