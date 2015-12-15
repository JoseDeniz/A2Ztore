package a2ztore.view;

import a2ztore.model.User;

public interface Repository {

    void add(User user);

    User get(String username);

    void update(String oldUsername, String newUsername);

    void delete(String username);

}
