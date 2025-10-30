package models;
public class Person {
    private int id;
    private String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person(String name) { // for inserting new person
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
