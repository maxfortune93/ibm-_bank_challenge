import { Component, Input } from '@angular/core';
import { Transaction } from 'src/app/core/models/transaction.model';
import { formatNumberWithHyphen } from '../utils/formater-utils';
import { TransactionType } from '../enum/transaction-type.enum';

@Component({
  selector: 'app-custom-transaction-item',
  templateUrl: './custom-transaction-item.component.html',
  styleUrl: './custom-transaction-item.component.scss'
})
export class CustomTransactionItemComponent {
  formatNumberWithHyphen = formatNumberWithHyphen;
  @Input() transaction!: Transaction;
  @Input() currentCustomerId!: string;

  transactionTypeColor!: string;
  transactionAmount!: string;
  transactionTypeLabel: string = '';

  constructor() {}

  ngOnInit(): void {
    this.setTransactionDetails();
  }

   TransactionTypeColors = {
    [TransactionType.TRANSFER]: '#FF9800',
    [TransactionType.DEPOSIT]: '#009688',
    [TransactionType.WITHDRAWAL]: '#FF5722',
  };

  // setTransactionDetails(): void {
  //   if (this.transaction.transactionType === 'DEPOSIT') {
  //     this.transactionTypeColor = '#009688';
  //     this.transactionAmount = `R$ ${this.transaction.amount.toFixed(2)}`;
  //   } else if (this.transaction.transactionType === 'WITHDRAWAL') {
  //     this.transactionTypeColor = '#FF5722';
  //     this.transactionAmount = `- R$ ${this.transaction.amount.toFixed(2)}`;
  //   } else if (this.transaction.transactionType === 'TRANSFER') {
  //     if (this.transaction.senderId === this.currentCustomerId) {
  //       this.transactionTypeColor = '#7b1fa2;';
  //       this.transactionAmount = `- R$ ${this.transaction.amount.toFixed(2)}`;
  //     } else {
  //       this.transactionTypeColor = '#FF9800';
  //       this.transactionAmount = `R$ ${this.transaction.amount.toFixed(2)}`;
  //     }
  //   }
  // }

  setTransactionDetails(): void {
    this.transactionTypeLabel = TransactionType[this.transaction.transactionType as keyof typeof TransactionType];

    switch (this.transaction.transactionType) {
      case 'DEPOSIT':
        this.transactionTypeColor = this.TransactionTypeColors[TransactionType.DEPOSIT];
        this.transactionAmount = `R$ ${this.transaction.amount.toFixed(2)}`;
        break;
      case 'WITHDRAWAL':
        this.transactionTypeColor = this.TransactionTypeColors[TransactionType.WITHDRAWAL];
        this.transactionAmount = `- R$ ${this.transaction.amount.toFixed(2)}`;
        break;
      case 'TRANSFER':
        if (this.transaction.senderId === this.currentCustomerId) {
          this.transactionTypeColor = this.TransactionTypeColors[TransactionType.TRANSFER];
          this.transactionAmount = `- R$ ${this.transaction.amount.toFixed(2)}`;
        } else {
          this.transactionTypeColor = this.TransactionTypeColors[TransactionType.TRANSFER];
          this.transactionAmount = `R$ ${this.transaction.amount.toFixed(2)}`;
        }
        break;
      default:
        this.transactionTypeColor = '#000000';
        this.transactionAmount = `R$ ${this.transaction.amount.toFixed(2)}`;
    }
  }
}
