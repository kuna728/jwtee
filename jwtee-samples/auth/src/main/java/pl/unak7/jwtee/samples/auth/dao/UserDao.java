package pl.unak7.jwtee.samples.auth.dao;

import pl.unak7.jwtee.samples.auth.domain.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao {

    private final List<User> users = new ArrayList<>(Arrays.asList(
            new User("test", "qaz123", "Robert", 0),
            new User("user", "wcyWAT", "Andrij", -3)
    ));

    public Optional<User> getByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findAny();
    }

    public void save(User user) {
        if(user.getUsername() == null)
            throw new IllegalStateException("Username (primary key) can not be null");
        deleteByUsername(user.getUsername());
        users.add(user);
    }

    public void deleteByUsername(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
    }
}
