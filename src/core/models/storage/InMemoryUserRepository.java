package core.models.storage;

import core.models.DataStore;
import core.models.User;
import core.models.repository.IUserRepository;

/**
 * In-memory implementation of IUserRepository backed by the DataStore singleton.
 * Satisfies the Dependency Inversion Principle: controllers depend on IUserRepository,
 * not on the concrete DataStore class.
 */
public class InMemoryUserRepository implements IUserRepository {

    private final DataStore dataStore;

    public InMemoryUserRepository() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public User findByUsername(String username) {
        return dataStore.getUserByUsername(username);
    }

    @Override
    public boolean usernameExists(String username) {
        return dataStore.usernameExists(username);
    }

    @Override
    public boolean idExists(long id) {
        return dataStore.userIdExists(id);
    }
}
