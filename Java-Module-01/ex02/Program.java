public class Program {
    public static void main(String[] args) {
        UsersList myUsersList = new UsersArrayList();

        System.out.println("--- Adding 12 Users ---");
        for (int i = 1; i <= 12; i++) {
            myUsersList.addUser(new User("User_" + i, 100 * i));
        }
        System.out.println("registered users: " + myUsersList.getNumberOfUsers());

        User thirdUser = myUsersList.getUserByIndex(2);
        System.out.println("\n--- get by Index ---");
        System.out.println("Index 2 maps to: " + thirdUser.getName() + " (ID: " + thirdUser.getId() + ")");

        System.out.println("\n--- get by ID ---");
        User userById = myUsersList.getUserById(5);
        System.out.println("User with ID 5: " + userById.getName() + " with Balance: " + userById.getBalance());

        System.out.println("\n--- exception ---");
        try {
            myUsersList.getUserById(999);
        } catch (UserNotFoundException e) {
            System.out.println("Caught Expected Error: " + e.getMessage());
        }
    }
}
