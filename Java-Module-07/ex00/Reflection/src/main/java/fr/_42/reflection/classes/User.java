package fr._42.reflection.classes;

import java.util.StringJoiner;

// User
// khalil
// sohail
// 22
// firstName
// ksohail
// grow(int)

public class User {
    private String  firstName;
    private String  lastName;
    private int     height;

    public User() {
        this.firstName = "Default first name";
        this.lastName = "Default last name";
        this.height = 0;
    }

    public User(String firstName, String lastName, int height) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
    }

    public void printFirstName() {
        System.out.println(firstName);
    }

    public void printTwoStrings(String str1, String str2) {
        System.out.println(str1 + ", " + str2);
    }

    public int grow(int value) {
        this.height += value;
        return height;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                    .add("firstName='" + firstName + "'")
                    .add("lastName='" + lastName + "'")
                    .add("height=" + height)
                    .toString();
    }

}
