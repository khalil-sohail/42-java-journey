import java.util.Scanner;

public class Menu {
    private final TransactionsService   service;
    private final boolean               isDevMode;
    private final Scanner               scanner;

    public Menu(TransactionsService service, boolean isDevMode) {
        this.service = service;
        this.isDevMode = isDevMode;
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        while (true) {
            printMenuItems();
            System.out.print("-> ");
            if (!scanner.hasNextLine()) {
                break;
            }

            String choice = scanner.nextLine().trim();
            try {
                if (choice.equals("1")) {
                    addUser();
                } else if (choice.equals("2")) {
                    viewUserBalance();
                } else if (choice.equals("3")) {
                    performTransfer();
                } else if (choice.equals("4")) {
                    viewUserTransactions();
                } else if (isDevMode && choice.equals("5")) {
                    removeTransfer();
                } else if (isDevMode && choice.equals("6")) {
                    checkValidity();
                } else if ((isDevMode && choice.equals("7")) || (!isDevMode && choice.equals("5"))) {
                    break;
                } else {
                    System.out.println("Invalid command. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println("---------------------------------------------------------");
        }
    }

    private void printMenuItems() {
        System.out.println("1. Add a user");
        System.out.println("2. View user balances");
        System.out.println("3. Perform a transfer");
        System.out.println("4. View all transactions for a specific user");

        if (isDevMode) {
            System.out.println("5. DEV - remove a transfer by ID");
            System.out.println("6. DEV - check transfer validity");
            System.out.println("7. Finish execution");
        } else {
            System.out.println("5. Finish execution");
        }
    }

    private void addUser() {
        System.out.println("Enter a user name and a balance");
        System.out.print("-> ");
        String line = scanner.nextLine().trim();
        String[] parts = line.split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid input format. Expected: [Name] [Balance]");
        }
        
        String name = parts[0];
        int balance = Integer.parseInt(parts[1]);
        
        User user = new User(name, balance);
        service.addUser(user);
        System.out.println("User with id = " + user.getId() + " is added");
    }

    private void viewUserBalance() {
        System.out.println("Enter a user ID");
        System.out.print("-> ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        
        int balance = service.getUserBalance(id);
        String userName = service.getUserName(id); 
        System.out.println(userName + " - " + balance);
    }

    private void performTransfer() {
        System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
        System.out.print("-> ");
        String line = scanner.nextLine().trim();
        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid input format. Expected: [SenderID] [RecipientID] [Amount]");
        }
        
        int senderId = Integer.parseInt(parts[0]);
        int recipientId = Integer.parseInt(parts[1]);
        int amount = Integer.parseInt(parts[2]);
        
        service.transfer(senderId, recipientId, amount);
        System.out.println("The transfer is completed");
    }

    private void viewUserTransactions() {
        System.out.println("Enter a user ID");
        System.out.print("-> ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        
        Transaction[] transactions = service.getUserTransactions(id);
        for (Transaction tx : transactions) {
            if (tx.getTransferCategory() == Transaction.Category.DEBITS) {
                System.out.println("To " + tx.getRecipient().getName() + "(id = " + tx.getRecipient().getId() + ") " 
                        + tx.getTransferAmount() + " with id = " + tx.getId());
            } else {
                System.out.println("From " + tx.getSender().getName() + "(id = " + tx.getSender().getId() + ") " 
                        + "+" + tx.getTransferAmount() + " with id = " + tx.getId());
            }
        }
    }

    private void removeTransfer() {
        System.out.println("Enter a user ID and a transfer ID");
        System.out.print("-> ");
        String line = scanner.nextLine().trim();
        String[] parts = line.split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid input format. Expected: [UserID] [TransferUUID]");
        }
        
        int userId = Integer.parseInt(parts[0]);
        String txId = parts[1];
        
        Transaction[] txs = service.getUserTransactions(userId);
        Transaction target = null;
        for (Transaction t : txs) {
            if (t.getId().equals(txId)) {
                target = t;
                break;
            }
        }
        
        service.removeTransaction(txId, userId);
        
        if (target != null) {
            int displayAmt = Math.abs(target.getTransferAmount());
            String pairedName = (target.getTransferCategory() == Transaction.Category.DEBITS) 
                    ? target.getRecipient().getName() : target.getSender().getName();
            int pairedId = (target.getTransferCategory() == Transaction.Category.DEBITS) 
                    ? target.getRecipient().getId() : target.getSender().getId();
            
            System.out.println("Transfer To " + pairedName + "(id = " + pairedId + ") " + displayAmt + " removed");
        } else {
            System.out.println("Transfer removed.");
        }
    }

    private void checkValidity() {
        System.out.println("Check results:");
        Transaction[] orphans = service.unpairedTransactions();
        
        for (Transaction tx : orphans) {
            if (tx.getTransferCategory() == Transaction.Category.DEBITS) {
                System.out.println(tx.getSender().getName() + "(id = " + tx.getSender().getId() 
                        + ") has an unacknowledged transfer id = " + tx.getId() 
                        + " to " + tx.getRecipient().getName() + "(id = " + tx.getRecipient().getId() 
                        + ") for " + Math.abs(tx.getTransferAmount()));
            } else { 
                System.out.println(tx.getRecipient().getName() + "(id = " + tx.getRecipient().getId() 
                        + ") has an unacknowledged transfer id = " + tx.getId() 
                        + " from " + tx.getSender().getName() + "(id = " + tx.getSender().getId() 
                        + ") for " + tx.getTransferAmount());
            }
        }
    }
}
