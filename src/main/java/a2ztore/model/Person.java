package a2ztore.model;

public class Person {

    private final String name;
    private final String surname;

    public Person(String name, String surname) {
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
