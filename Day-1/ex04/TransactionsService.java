package ex04;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionsService {
    private UsersList usersList;

    public TransactionsService(UsersList usersList) {
        this.usersList = usersList;
    }

    public void addUser(User user) {
        usersList.addUser(user);
    }

    public Integer getUserBalance(int userId) {
        User user = usersList.getUserById(userId);
        return user.getBalance();
    }

    public void performTransfer(int senderId, int recipientId, int transferAmount) {

        User sender = usersList.getUserById(senderId);
        User recipient = usersList.getUserById(recipientId);
        if (sender.getBalance() < transferAmount) {
            throw new IllegalTransactionException("Недостаточно средств у пользователя " + sender.getName());
        }
        Transaction debitTransaction = new Transaction(sender, recipient, transferAmount, Transaction.TransactionCategory.DEBIT);
        Transaction creditTransaction = new Transaction(sender, recipient, transferAmount, Transaction.TransactionCategory.CREDIT);

        sender.getTransactionsList().addTransaction(debitTransaction);
        recipient.getTransactionsList().addTransaction(creditTransaction);

        if (transferAmount > sender.getBalance()) {
            throw new IllegalTransactionException("Сумма перевода превышает баланс отправителя.");
        }

        sender.setBalance(sender.getBalance() - transferAmount);
        recipient.setBalance(recipient.getBalance() + transferAmount);
    }

    public Transaction[] getUserTransactions(int userId) {
        User user = usersList.getUserById(userId);
        if (user == null) {
            System.out.println("Пользователь с ID " + userId + " не найден.");
            return new Transaction[0];
        }
        return user.getTransactionsList().toArray();
    }

    public void removeTransaction(int userId, UUID transactionId) {
        User user = usersList.getUserById(userId);
        if (user == null) {
            System.out.println("Пользователь с ID " + userId + " не найден.");
            return;
        }
        try {
            user.getTransactionsList().removeTransaction(transactionId);
            System.out.println("Транзакция с ID " + transactionId + " успешно удалена для пользователя " + user.getName());
        } catch (TransactionNotFoundException e) {
            System.out.println("Ошибка при удалении транзакции: " + e.getMessage());
        }
    }

    public Transaction[] checkValidityOfTransactions() {
        List<Transaction> unpairedTransactions = new ArrayList<>();

        for (User user : usersList.getAllUsers()) {
            for (Transaction transaction : user.getTransactionsList().toArray()) {
                if (!hasPairedTransaction(transaction)) {
                    unpairedTransactions.add(transaction);
                }
            }
        }

        return unpairedTransactions.toArray(new Transaction[0]);
    }

    private boolean hasPairedTransaction(Transaction transaction) {
        User sender = transaction.getSender();
        User recipient = transaction.getRecipient();
        Transaction.TransactionCategory oppositeCategory = getOppositeCategory(transaction.getTransferCategory());
        for (Transaction recipientTransaction : recipient.getTransactionsList().toArray()) {
            if (recipientTransaction.getSender() == recipient && recipientTransaction.getRecipient() == sender
                    && recipientTransaction.getTransferCategory() == oppositeCategory) {
                return true;
            }
        }

        return false;
    }

    private Transaction.TransactionCategory getOppositeCategory(Transaction.TransactionCategory category) {
        return (category == Transaction.TransactionCategory.DEBIT)
                ? Transaction.TransactionCategory.CREDIT : Transaction.TransactionCategory.DEBIT;
    }
}
