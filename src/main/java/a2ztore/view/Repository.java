package a2ztore.view;

import a2ztore.model.Person;

public interface Repository {

    void add(Person person);

    Person get(String personName);

}
