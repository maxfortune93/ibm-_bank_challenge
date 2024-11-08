import { ChangeDetectionStrategy, Component, Input, signal } from '@angular/core';
import { Transaction } from 'src/app/core/models/transaction.model';
import { formatNumberWithHyphen, formattedCurrency } from '../utils/formater-utils';
import { TransactionType } from '../enum/transaction-type.enum';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-custom-transaction-item',
  templateUrl: './custom-transaction-item.component.html',
  styleUrl: './custom-transaction-item.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomTransactionItemComponent {
  formatNumberWithHyphen = formatNumberWithHyphen;
  @Input() transaction!: Transaction;
  @Input() currentCustomerId!: string;

  transactionTypeColor!: string;
  transactionAmount!: string;
  transactionTypeLabel: string = '';
  readonly panelOpenState = signal(false);
  isSmallScreen$!: Observable<boolean>;

  constructor(private breakpointObserver: BreakpointObserver) {
    this.isSmallScreen$ = this.breakpointObserver.observe(['(max-width: 768px)'])
      .pipe(
        map(result => result.matches)
      );
  }

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
        this.transactionAmount = formattedCurrency(this.transaction.amount);
        break;
      case 'WITHDRAWAL':
        this.transactionTypeColor = this.TransactionTypeColors[TransactionType.WITHDRAWAL];
        this.transactionAmount = `- ${formattedCurrency(this.transaction.amount)}`;
        break;
      case 'TRANSFER':
        if (this.transaction.senderId === this.currentCustomerId) {
          this.transactionTypeColor = this.TransactionTypeColors[TransactionType.TRANSFER];
          this.transactionAmount = `- ${formattedCurrency(this.transaction.amount)}`;
        } else {
          this.transactionTypeColor = this.TransactionTypeColors[TransactionType.TRANSFER];
          this.transactionAmount = formattedCurrency(this.transaction.amount);
        }
        break;
      default:
        this.transactionTypeColor = '#000000';
        this.transactionAmount = formattedCurrency(this.transaction.amount);
    }
  }

  isSmallScreen(): boolean {
    return this.breakpointObserver.isMatched('(max-width: 668px)');
  }
}
