package ex01;
import java.util.UUID;

public class Transaction {
    private UUID identifier;
    private User recipient;
    private User sender;
    private Transaction.TransactionCategory transferCategory;
    private Integer transferAmount;

    enum TransactionCategory {
        DEBIT, CREDIT
    }

    public Transaction(User sender, User recipient, Integer transferAmount, Transaction.TransactionCategory transferCategory) {
        validateTransactionParams(transferAmount, transferCategory, sender);
        this.identifier = UUID.randomUUID();
        this.recipient = recipient;
        this.sender = sender;
        this.transferCategory = transferCategory;
        this.transferAmount = transferAmount;

        processTransaction(transferCategory, sender, recipient, transferAmount);
    }

    private void validateTransactionParams(Integer transferAmount, Transaction.TransactionCategory transferCategory, User sender) {
        if (transferAmount < 0 && transferCategory == Transaction.TransactionCategory.DEBIT) {
            System.out.println("Неверная транзакция: отрицательная сумма для дебетовой транзакции");
        }

        if ((sender.getBalance() < transferAmount && transferCategory == Transaction.TransactionCategory.DEBIT)
                || (sender.getBalance() < -transferAmount && transferCategory == Transaction.TransactionCategory.CREDIT)) {
            System.out.println("Недостаточно средств на счете пользователя " + sender.getName());
        }
    }
    private void processTransaction(Transaction.TransactionCategory category, User sender, User recipient, Integer transferAmount){
        int absoluteAmount = (transferAmount < 0) ? -transferAmount : transferAmount;
        sender.setBalance(sender.getBalance() - absoluteAmount);
        recipient.setBalance(recipient.getBalance() + absoluteAmount);
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

    public Transaction.TransactionCategory getTransferCategory() {
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
