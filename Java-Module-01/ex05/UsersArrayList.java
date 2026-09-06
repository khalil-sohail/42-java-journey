
public class UsersArrayList implements UsersList {
    private User[]  users;
    private int     size;

    public UsersArrayList() {
        this.users = new User[10]; // 10 as Default
        this.size = 0;
    }

    @Override
    public void addUser(User user) {
        if (user == null) {
            return;
        } else if (size == users.length) {
            growArray();
        }

        users[size] = user;
        size++;
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        for (int i = 0; i < size; i++) {
            if (users[i].getId() == id) {
                return users[i];
            }
        }

        throw new UserNotFoundException("User with ID " + id + " not found.");
    }

    @Override
    public User getUserByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }

        return users[index];
    }

    @Override
    public int getNumberOfUsers() {
        return size;
    }

    private void growArray() {
        int oldCapacity = users.length;
        int newCapacity = oldCapacity + (oldCapacity / 2);
        User[] newArray = new User[newCapacity];
        
        for (int i = 0; i < oldCapacity; i++) {
            newArray[i] = users[i];
        }

        users = newArray;
    }
}
