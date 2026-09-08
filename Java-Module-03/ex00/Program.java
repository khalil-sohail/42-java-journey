
public class Program {
    public static void main(String[] args) {
        int count = -1;

        if (args.length > 0 && args[0].startsWith("--count=")) {
            String countStr = args[0].substring("--count=".length());

            try {
                count = Integer.parseInt(countStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid count value: " + countStr);
                System.exit(1);
            }
            
            try {
                Thread hen = new Thread(new DisputeRunnable("Hen", count));
                Thread egg = new Thread(new DisputeRunnable("Egg", count));
                
                hen.start();
                egg.start();
                
                hen.join();
                egg.join();
                
                for (int i = 0; i < count; i++) {
                    System.out.println("Human");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.err.println("Missing or invalid --count argument");
            System.exit(1);
        }
    }
}