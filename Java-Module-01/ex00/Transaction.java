import java.util.UUID;

public class Transaction {
    public enum Category {
        DEBITS,
        CREDITS
    }

    private String id;
    private User recipient;
    private User sender;
    private Category transferCategory;
    private Integer transferAmount;

    public Transaction(User recipient, User sender, Category transferCategory, Integer transferAmount) {
        this.id = UUID.randomUUID().toString();
        this.recipient = recipient;
        this.sender = sender;
        this.transferCategory = transferCategory;
        setTransferAmount(transferAmount);
    }

    public String   getId() { return id; }
    public void     setId(String id) { this.id = id; }
    public User     getRecipient() { return recipient; }
    public void     setRecipient(User recipient) { this.recipient = recipient; }
    public User     getSender() { return sender; }
    public void     setSender(User sender) { this.sender = sender; }
    public Category getTransferCategory() { return transferCategory; }
    public void     setTransferCategory(Category transferCategory) { this.transferCategory = transferCategory; }
    public Integer  getTransferAmount() { return transferAmount; }

    public void setTransferAmount(Integer transferAmount) {
        if (this.transferCategory == Category.DEBITS && transferAmount >= 0) {
            System.out.println("Error: Outgoing debit transactions must have negative amounts.");
            this.transferAmount = 0;
            return;
        }
        if (this.transferCategory == Category.CREDITS && transferAmount <= 0) {
            System.out.println("Error: Incoming credit transactions must have positive amounts.");
            this.transferAmount = 0;
            return;
        }
        this.transferAmount = transferAmount;
    }
}
