
public class Program {
    public static void main(String[] args) {
        User user1 = new User("Khalil", 1000);
        User user2 = new User("Sohail", 500);
        User user3 = new User("Charlie", 250);

        System.out.println("--- User Autoincrement Test ---");
        System.out.println("User: " + user1.getName() + " | Automatically Assigned ID: " + user1.getId());
        System.out.println("User: " + user2.getName() + " | Automatically Assigned ID: " + user2.getId());
        System.out.println("User: " + user3.getName() + " | Automatically Assigned ID: " + user3.getId());

        System.out.println("\n--- Singleton Identity Test ---");
        UserIdsGenerator gen1 = UserIdsGenerator.getInstance();
        UserIdsGenerator gen2 = UserIdsGenerator.getInstance();
        
        System.out.println("Do gen1 and gen2 point to the exact same object instance? " + (gen1 == gen2 ? "Yes" : "No"));
    }
}
