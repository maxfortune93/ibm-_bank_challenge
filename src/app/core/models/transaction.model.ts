export interface Transaction {
  senderId: string | null;
  receiverId: string | null;
  senderAccountNumber: string | null;
  receiverAccountNumber: string;
  amount: number;
  transactionType: string;
  timestamp: string;
}

export interface TransactionDTO {
  senderId: string | null;
  receiverId: string | null;
  amount: number;
  transactionType: string;
}
