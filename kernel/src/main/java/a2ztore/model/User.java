package a2ztore.model;

public class User {

    private final String name;
    private final String surname;

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String name() {
        return name;
    }

    public String surname() {
        return surname;
    }
}
