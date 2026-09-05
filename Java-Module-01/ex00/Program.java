
public class Program {
    public static void main(String[] args) {
        User john = new User(1, "John Doe", 1000);
        User mike = new User(2, "Mike Smith", 500);

        System.out.println("--- Initial State ---");
        System.out.println("User: " + john.getName() + " | ID: " + john.getId() + " | Balance: " + john.getBalance());
        System.out.println("User: " + mike.getName() + " | ID: " + mike.getId() + " | Balance: " + mike.getBalance());

        Transaction debitTx = new Transaction(mike, john, Transaction.Category.DEBITS, -200);
        Transaction creditTx = new Transaction(mike, john, Transaction.Category.CREDITS, 200);

        System.out.println("\n--- Outgoing ---");
        System.out.println("Tx ID: " + debitTx.getId());
        System.out.println("Sender: " + debitTx.getSender().getName());
        System.out.println("Recipient: " + debitTx.getRecipient().getName());
        System.out.println("Category: " + debitTx.getTransferCategory());
        System.out.println("Amount: " + debitTx.getTransferAmount());

        System.out.println("\n--- Incoming ---");
        System.out.println("Tx ID: " + creditTx.getId());
        System.out.println("Amount: " + creditTx.getTransferAmount());
        System.out.println("Category: " + creditTx.getTransferCategory());
        
    }
}
