import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { startWith, Observable, switchMap, tap, combineLatest, map } from 'rxjs';
import { Customer } from '../core/models/customer.model';
import { TransactionDTO } from '../core/models/transaction.model';
import { CustomerService } from '../core/services/api/customers/customer.service';
import { TransactionService } from '../core/services/api/transactions/transaction.service';
import { MatDialog } from '@angular/material/dialog';
import { CustomerRegisterComponent } from '../customer-register/customer-register.component';
import { SnackbarService } from '../shared/snackbar/snackbar.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrl: './transaction.component.scss'
})
export class TransactionComponent {

  transactionForm: FormGroup;
  filteredCustomers$: Observable<Customer[]> | undefined;
  filteredRecipients$: Observable<Customer[]> | undefined;
  customers: Customer[] = [];
  recipients: Customer[] = [];

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private transactionService: TransactionService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private router: Router
  ) {
    this.transactionForm = this.fb.group({
      customerName: ['', Validators.required],
      transactionType: ['', Validators.required],
      amount: ['',[Validators.required, Validators.min(0.01)]],
      recipientName: ['']
    });
  }

  ngOnInit(): void {
    const customerName$ = this.transactionForm.get('customerName')?.valueChanges.pipe(startWith(''))!;
    const recipientName$ = this.transactionForm.get('recipientName')?.valueChanges.pipe(startWith(''))!;

    this.filteredCustomers$ = customerName$.pipe(
      switchMap(value => this._filterCustomers(value || '')),
      tap(customers => this.customers = customers)
    );

    this.filteredRecipients$ = combineLatest([recipientName$, customerName$]).pipe(
      switchMap(([recipientName, customerName]) => this._filterCustomers(recipientName || '').pipe(
        map(recipients => recipients.filter(v => v.name !== customerName))
      )),
      tap(recipients => this.recipients = recipients)
    );
  }

  private _filterCustomers(query: string): Observable<Customer[]> {
    const limit = 5;
    return this.customerService.autocompleteCustomers(query, limit);
  }

  onCustomerSelected(event: any): void {
    const selectedCustomerName = event.option.value;
    const selectedCustomer = this.customers.find(c => c.name === selectedCustomerName);
    if (selectedCustomer) {
      this.transactionForm.get('customerName')?.setValue(selectedCustomer.name);
    }
  }

  onRecipientSelected(event: any): void {
    const selectedRecipientName = event.option.value;
    const selectedRecipient = this.recipients.find(c => c.name === selectedRecipientName);
    if (selectedRecipient) {
      this.transactionForm.get('recipientName')?.setValue(selectedRecipient.name);
    }
  }

  onTransactionTypeChange(transactionType: string): void {
    if (transactionType === 'TRANSFER') {
      this.transactionForm.get('recipientName')?.setValidators([Validators.required]);
    } else {
      this.transactionForm.get('recipientName')?.clearValidators();
    }
    this.transactionForm.get('recipientName')?.updateValueAndValidity();
  }

  isTransfer(): boolean {
    return this.transactionForm.get('transactionType')?.value === 'TRANSFER';
  }

  getSubmitButtonLabel(): string {
    const transactionType = this.transactionForm.get('transactionType')?.value;
    if (transactionType === 'DEPOSIT') {
      return 'Depositar';
    } else if (transactionType === 'WITHDRAWAL') {
      return 'Sacar';
    } else if (transactionType === 'TRANSFER') {
      return 'Transferir';
    }
    return 'Enviar';
  }

  submitTransaction(): void {
    if (this.transactionForm.valid) {
      const customerName = this.transactionForm.get('customerName')?.value;
      const recipientName = this.transactionForm.get('recipientName')?.value;
      const selectedCustomer = this.customers.find(c => c.name === customerName);
      const selectedRecipient = this.recipients.find(c => c.name === recipientName);

      const transactionDTO: TransactionDTO = {
        senderId: this.isTransfer() ? (selectedCustomer ? selectedCustomer.id || null : null) : null,
        receiverId: this.isTransfer() ? (selectedRecipient ? selectedRecipient.id || null : null) : (selectedCustomer ? selectedCustomer.id || null : null),
        amount: this.transactionForm.get('amount')?.value,
        transactionType: this.transactionForm.get('transactionType')?.value
      };

      if (!this.isTransfer()) {
        if (transactionDTO.transactionType === 'DEPOSIT') {
          transactionDTO.receiverId = selectedCustomer ? selectedCustomer.id || null : null;
        } else if (transactionDTO.transactionType === 'WITHDRAWAL') {
          transactionDTO.senderId = selectedCustomer ? selectedCustomer.id || null : null;
        }
      }

      if ((transactionDTO.senderId === null && transactionDTO.transactionType === 'WITHDRAWAL') ||
          (transactionDTO.receiverId === null && transactionDTO.transactionType !== 'WITHDRAWAL')) {
        this.snackbarService.error('Cliente ou destinatário inválido');
        return;
      }

      this.transactionService.saveTransaction(transactionDTO).subscribe({
        next: response => {
          if (response.message === 'Transaction saved successfully') {
            this.snackbarService.success('Transação realizada com sucesso');
            this.resetForm();
            if (selectedCustomer) {
              this.router.navigate(['/customers', selectedCustomer.id]);
            }
          } else {
            this.snackbarService.error('Erro inesperado na resposta da API');
          }
        },
        error: error => {
          console.error('Erro ao realizar a transação:', error);
          const errorMessage = error.error && error.error.message ? error.error.message : 'Erro ao realizar a transação';
          this.snackbarService.error(errorMessage);
        }
      });
    }
  }
  private getCustomerIdByName(name: string, customerList: Customer[]): string | null {
    const customer = customerList.find(c => c.name === name);
    return customer && customer.id ? customer.id : null;
  }

  openRegisterModal(): void {
    const dialogRef = this.dialog.open(CustomerRegisterComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.info('Dialog result:', result);
    });
  }

  onCancel(event: Event) {
    event.preventDefault();
    this.resetForm()
 }

 resetForm(): void {
   Object.keys(this.transactionForm.controls).forEach(key => {
     const control = this.transactionForm.get(key);
     control?.markAsPristine();
     control?.markAsUntouched();
     control?.updateValueAndValidity();
     control?.setErrors(null);
    });
    this.transactionForm.reset({
      customerName: '',
      transactionType: '',
      amount: 0,
      recipientName: ''
    });
    this.transactionForm.clearValidators();
}
}
