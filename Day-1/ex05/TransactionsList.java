package ex05;

import java.util.UUID;

public interface TransactionsList {
    void addTransaction(Transaction transaction);
    Transaction removeTransaction(UUID transactionId);
    Transaction[] toArray();
}
