import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { BANCOBRASIL } from '../shared/constants';
import { Customer } from '../core/models/customer.model';
import { CustomerService } from '../core/services/api/customers/customer.service';
import { MatDialogRef } from '@angular/material/dialog';
import { SnackbarService } from '../shared/snackbar/snackbar.service';

@Component({
  selector: 'app-customer-register',
  templateUrl: './customer-register.component.html',
  styleUrl: './customer-register.component.scss'
})
export class CustomerRegisterComponent implements OnInit {

  customerForm!: FormGroup;
  banks = BANCOBRASIL;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private snackbarService: SnackbarService,
    private dialogRef: MatDialogRef<CustomerRegisterComponent>
  ) { }

  ngOnInit(): void {
    this.customerForm = this.fb.group({
      name: ['', Validators.required],
      age: ['', [Validators.required, Validators.min(0)]],
      email: ['', [Validators.required, Validators.email]],
      bank: ['', Validators.required],
      branchNumber: ['', Validators.required],
      accountNumber: ['', Validators.required],
      digit: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.customerForm.valid) {
      const customerFormData = this.customerForm.value;
      const customer: Customer = {
        name: customerFormData.name,
        age: customerFormData.age,
        email: customerFormData.email,
        bankName: customerFormData.bank,
        branch: customerFormData.branchNumber,
        accountNumber: `${customerFormData.accountNumber}-${customerFormData.digit}`,
        balance: 0
      };

      this.customerService.saveCustomer(customer).subscribe({
        next: (response: any) => {
          this.snackbarService.success('Customer registered successfully');
          this.dialogRef.close(response);
        },
        error:  error => {
          console.error('Error registering customer:', error.error);
          this.snackbarService.error(error.error || 'Error registering customer');
        }
      });
    }
  }
}
