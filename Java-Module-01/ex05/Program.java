public class Program {
    public static void main(String[] args) {
        boolean isDevMode = false;
        
        if (args.length > 0 && args[0].equals("--profile=dev")) {
            isDevMode = true;
        }

        TransactionsService service = new TransactionsService();
        Menu menu = new Menu(service, isDevMode);
        
        menu.display();
    }
}
