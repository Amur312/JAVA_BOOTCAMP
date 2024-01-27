package ex04;

import java.util.UUID;

public class Transaction {
    private UUID identifier;
    private User recipient;
    private User sender;
    private TransactionCategory transferCategory;
    private Integer transferAmount;

    enum TransactionCategory {
        DEBIT, CREDIT
    }
    public Transaction(User sender, User recipient, Integer transferAmount, TransactionCategory transferCategory) {
        validateTransactionParams(transferAmount, transferCategory, sender);
        this.identifier = UUID.randomUUID();
        this.recipient = recipient;
        this.sender = sender;
        this.transferCategory = transferCategory;
        this.transferAmount = transferAmount;

    }

    private void validateTransactionParams(Integer transferAmount, TransactionCategory transferCategory, User sender) {
        if (transferAmount < 0 && transferCategory == TransactionCategory.DEBIT) {
            System.out.println("Неверная транзакция: отрицательная сумма для дебетовой транзакции");
        }

        if ((sender.getBalance() < transferAmount && transferCategory == TransactionCategory.DEBIT)
                || (sender.getBalance() < -transferAmount && transferCategory == TransactionCategory.CREDIT)) {
            System.out.println("Недостаточно средств на счете пользователя " + sender.getName());
        }
    }



    public UUID getIdentifier() {
        return identifier;
    }

    public User getRecipient() {
        return recipient;
    }

    public User getSender() {
        return sender;
    }

    public TransactionCategory getTransferCategory() {
        return transferCategory;
    }

    public Integer getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Integer transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public String toString() {
        return "Transaction {" +
                "\n  Identifier: " + identifier +
                "\n  Recipient: " + recipient +
                "\n  Sender: " + sender +
                "\n  Transfer Category: " + transferCategory +
                "\n  Transfer Amount: " + transferAmount +
                "\n}";
    }
}
