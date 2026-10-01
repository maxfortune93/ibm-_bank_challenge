import { ChangeDetectionStrategy, Component, Input, OnChanges } from '@angular/core';
import { Transaction } from 'src/app/core/models/transaction.model';
import { formatNumberWithHyphen, formattedCurrency } from '../utils/formater-utils';
import { TransactionType } from '../enum/transaction-type.enum';

@Component({
  selector: 'app-custom-transaction-item',
  templateUrl: './custom-transaction-item.component.html',
  styleUrl: './custom-transaction-item.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomTransactionItemComponent implements OnChanges {
  @Input() transaction!: Transaction;
  @Input() currentCustomerId!: string;

  label = '';
  description = '';
  amount = '';
  icon = 'swap_horiz';
  incoming = false;

  ngOnChanges(): void {
    const t = this.transaction;
    this.label = TransactionType[t.transactionType as keyof typeof TransactionType] ?? t.transactionType;

    switch (t.transactionType) {
      case 'DEPOSIT':
        this.incoming = true;
        this.description = 'Depósito em conta';
        break;
      case 'WITHDRAWAL':
        this.incoming = false;
        this.description = 'Saque em conta';
        break;
      default:
        this.incoming = t.receiverId === this.currentCustomerId;
        this.description = this.incoming
          ? `De ${formatNumberWithHyphen(t.senderAccountNumber)}`
          : `Para ${formatNumberWithHyphen(t.receiverAccountNumber)}`;
    }

    this.icon = this.incoming ? 'arrow_downward' : 'arrow_upward';
    this.amount = `${this.incoming ? '+' : '-'} ${formattedCurrency(t.amount)}`;
  }
}
