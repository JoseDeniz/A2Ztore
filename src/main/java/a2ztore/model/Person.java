package a2ztore.model;

public class Person {

    private final String name;
    private final String fullname;

    public Person(String name, String fullname) {
        this.name = name;
        this.fullname = fullname;
    }

    public String name() {
        return name;
    }

    public String fullname() {
        return fullname;
    }
}
