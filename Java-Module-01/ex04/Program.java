public class Program {
    public static void main(String[] args) {
        // Initialize the centralized service Facade
        TransactionsService service = new TransactionsService();

        System.out.println("==================================================");
        System.out.println("1. CREATING AND REGISTERING USERS");
        System.out.println("==================================================");
        User khalil = new User("Khalil", 1000);
        User sohail = new User("Sohail", 500);
        
        service.addUser(khalil);
        service.addUser(sohail);
        
        System.out.println("Khalil (ID " + khalil.getId() + ") Balance: $" + service.getUserBalance(khalil.getId()));
        System.out.println("Sohail (ID " + sohail.getId() + ") Balance: $" + service.getUserBalance(sohail.getId()));

        System.out.println("\n==================================================");
        System.out.println("2. PERFORMING VALID TRANSFER ($200 from Khalil to Sohail)");
        System.out.println("==================================================");
        // This operation automatically generates TWO transactions (Debit for Khalil, Credit for Sohail)
        // Both transactions share the EXACT same UUID.
        service.transfer(khalil.getId(), sohail.getId(), 200);

        System.out.println("Khalil's New Balance: $" + service.getUserBalance(khalil.getId()));
        System.out.println("Sohail's New Balance: $" + service.getUserBalance(sohail.getId()));

        System.out.println("\n==================================================");
        System.out.println("3. RETRIEVING INDIVIDUAL USER TRANSACTIONS");
        System.out.println("==================================================");
        Transaction[] aliceTransfers = service.getUserTransactions(khalil.getId());
        Transaction[] bobTransfers = service.getUserTransactions(sohail.getId());

        System.out.println("Khalil has " + aliceTransfers.length + " transaction(s) in her ledger.");
        String sharedId = aliceTransfers[0].getId();
        System.out.println("Khalil's receipt UUID:  " + sharedId + " | Amount: " + aliceTransfers[0].getTransferAmount());
        System.out.println("Sohail's receipt UUID:    " + bobTransfers[0].getId() + " | Amount: " + bobTransfers[0].getTransferAmount());

        System.out.println("\n==================================================");
        System.out.println("4. SYSTEM VALIDITY CHECK (EXPECTING 0 UNPAIRED)");
        System.out.println("==================================================");
        Transaction[] initialOrphans = service.unpairedTransactions();
        System.out.println("Number of unpaired transactions found: " + initialOrphans.length);

        System.out.println("\n==================================================");
        System.out.println("5. SIMULATING SINGLE-SIDED DELETION (CREATING AN ORPHAN)");
        System.out.println("==================================================");
        System.out.println("Removing the transaction receipt ONLY from Khalil's profile...");
        service.removeTransaction(sharedId, khalil.getId());

        System.out.println("Khalil now has " + service.getUserTransactions(khalil.getId()).length + " transactions.");
        System.out.println("Sohail still has " + service.getUserTransactions(sohail.getId()).length + " transactions.");

        System.out.println("\n==================================================");
        System.out.println("6. RUNNING SYSTEM VALIDITY CHECK AGAIN (EXPECTING 1 ORPHAN)");
        System.out.println("==================================================");
        Transaction[] updatedOrphans = service.unpairedTransactions();
        System.out.println("Number of unpaired transactions found: " + updatedOrphans.length);
        if (updatedOrphans.length > 0) {
            System.out.println("Orphaned Transaction details -> Holder: " 
                + updatedOrphans[0].getRecipient().getName() 
                + " | ID: " + updatedOrphans[0].getId() 
                + " | Amount: " + updatedOrphans[0].getTransferAmount());
        }

        System.out.println("\n==================================================");
        System.out.println("7. TESTING ILLEGAL TRANSACTION EXCEPTION (OVERDRAFT)");
        System.out.println("==================================================");
        try {
            System.out.println("Attempting to transfer $10,000 from Sohail (Current balance: $700)...");
            service.transfer(sohail.getId(), khalil.getId(), 10000);
        } catch (RuntimeException e) {
            System.out.println("Caught Expected Exception: " + e.getClass().getName());
            System.out.println("Error Message: " + e.getMessage());
        }
    }
}
