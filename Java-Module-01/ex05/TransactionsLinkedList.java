
public class TransactionsLinkedList implements TransactionsList {
    private Transaction head;
    private int         size;

    public TransactionsLinkedList() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            return;
        }

        transaction.setNext(head);
        head = transaction;
        size++;
    }

    @Override
    public void removeTransactionById(String id) throws TransactionNotFoundException {
        Transaction previous = null;
        Transaction current = head;

        while (current != null) {
            if (current.getId().equals(id)) {
                if (previous == null) {
                    head = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }

                size--;
                current.setNext(null);
                return;
            }

            previous = current;
            current = current.getNext();
        }

        throw new TransactionNotFoundException("Transaction with UUID " + id + " not found.");
    }

    @Override
    public Transaction[] toArray() {
        Transaction[] array = new Transaction[size];
        Transaction current = head;
        int i = 0;
        
        while (current != null) {
            array[i] = current;
            current = current.getNext();
            i++;
        }

        return array;
    }
}
