
public class UserIdsGenerator {
    private static UserIdsGenerator instance;
    private int                     lastId;

    private UserIdsGenerator() {
        this.lastId = 0;
    }

    public static UserIdsGenerator getInstance() {
        if (instance == null) {
            instance = new UserIdsGenerator();
        }

        return instance;
    }

    public int generateId() {
        lastId++;
        return lastId;
    }
}
