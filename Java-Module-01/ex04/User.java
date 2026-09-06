
public class User {
    private final Integer       id; 
    private String              name;
    private Integer             balance;
    private TransactionsList    transactions;

    public User(String name, Integer balance) {
        this.id = UserIdsGenerator.getInstance().generateId();
        this.name = name;

        setBalance(balance);
        this.transactions = new TransactionsLinkedList();
    }

    public Integer          getId() { return id; }
    public String           getName() { return name; }
    public void             setName(String name) { this.name = name; }
    public Integer          getBalance() { return balance; }
    public TransactionsList getTransactions() { return transactions; }
    
    public void setBalance(Integer balance) {
        if (balance < 0) {
            System.out.println("Error: Balance cannot be negative.");
            this.balance = 0;
            return;
        }
        
        this.balance = balance;
    }
}
