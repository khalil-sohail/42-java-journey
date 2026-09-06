public class Program {
    public static void main(String[] args) {
        User khalil = new User("Khalil", 2000);
        User sohail = new User("Sohail", 1000);

        Transaction t1 = new Transaction(sohail, khalil, Transaction.Category.DEBITS, -300);
        Transaction t2 = new Transaction(sohail, khalil, Transaction.Category.DEBITS, -150);

        khalil.getTransactions().addTransaction(t1);
        khalil.getTransactions().addTransaction(t2);

        System.out.println("--- Reading Khalil's History Ledger via toArray() ---");
        Transaction[] aliceHistory = khalil.getTransactions().toArray();
        for (Transaction tx : aliceHistory) {
            System.out.println("Tx UUID: " + tx.getId() + " | Amount: " + tx.getTransferAmount());
        }

        System.out.println("\n--- Removing Transaction 1 ---");
        khalil.getTransactions().removeTransactionById(t1.getId());
        
        System.out.println("Khalil's updated ledger count: " + khalil.getTransactions().toArray().length);

        System.out.println("\n--- Testing Invalid Removal Exception ---");
        try {
            khalil.getTransactions().removeTransactionById("fake-uuid-123");
        } catch (TransactionNotFoundException e) {
            System.out.println("Caught Expected Error: " + e.getMessage());
        }
    }
}
