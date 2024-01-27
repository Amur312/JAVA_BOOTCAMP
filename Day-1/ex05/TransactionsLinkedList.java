package ex05;

import java.util.LinkedList;
import java.util.UUID;

public class TransactionsLinkedList implements TransactionsList {
    private LinkedList<Transaction> transactions;

    public TransactionsLinkedList() {
        this.transactions = new LinkedList<>();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.addLast(transaction);
    }

    @Override
    public Transaction removeTransaction(UUID transactionId) {
        Transaction transactionToRemove = null;

        for (Transaction transaction : transactions) {
            if (transaction.getIdentifier().equals(transactionId)) {
                transactionToRemove = transaction;
                break;
            }
        }

        if (transactionToRemove != null) {
            transactions.remove(transactionToRemove);
        } else {
            throw new TransactionNotFoundException("Транзакция с идентификатором " + transactionId + " не найдена.");
        }
        return null;
    }

    @Override
    public Transaction[] toArray() {
        return transactions.toArray(new Transaction[0]);
    }
}
