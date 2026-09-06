
public class TransactionsService {
    private UsersArrayList users = new UsersArrayList();


    public void addUser(User user) {
        users.addUser(user);
    }

    public Integer getUserBalance(Integer userId) throws UserNotFoundException {
        User user = users.getUserById(userId);
        return user.getBalance();
    }

    public void transfer(Integer recipientId, Integer senderId, Integer amount) throws IllegalTransactionException {
        User recipient                      = users.getUserById(recipientId);
        User sender                         = users.getUserById(senderId);
        Transaction recipientTransaction    = new Transaction(recipient, sender, Transaction.Category.CREDITS, amount); 
        Transaction senderTransaction       = new Transaction(recipient, sender, Transaction.Category.DEBITS, -1 * amount); 

        if (sender.getBalance() < amount) {
            throw new IllegalTransactionException("the amount: " + amount + "exceeding user's residual balance");
        }

        senderTransaction.setId(recipientTransaction.getId());

        recipient.getTransactions().addTransaction(recipientTransaction);
        sender.getTransactions().addTransaction(senderTransaction);
    }

    public Transaction[] getUserTransactions(Integer userId) {
        User user = users.getUserById(userId);

        return user.getTransactions().toArray();
    }

    public void removeTransaction(String transactionId, Integer userId) {
        User user = users.getUserById(userId);

        user.getTransactions().removeTransactionById(transactionId);
    }

    public Transaction[] unpairedTransactions() {
        TransactionsList allTxList = new TransactionsLinkedList();
    
        for (int i = 0; i < users.getNumberOfUsers(); i++) {
            User user = users.getUserByIndex(i);
            Transaction[] userTxs = user.getTransactions().toArray();

            for (Transaction tx : userTxs) {
                allTxList.addTransaction(tx);
            }
        }
        
        Transaction[] allTxArray = allTxList.toArray();
        TransactionsList orphans = new TransactionsLinkedList();
        
        for (int i = 0; i < allTxArray.length; i++) {
            int count = 0;
            for (int j = 0; j < allTxArray.length; j++) {
                if (allTxArray[i].getId().equals(allTxArray[j].getId())) {
                    count++;
                }
            }

            if (count == 1) {
                orphans.addTransaction(allTxArray[i]);
            }
        }
        
        return orphans.toArray();
    }

}
