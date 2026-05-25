package core.models.repository;

import core.models.User;

/**
 * Repository abstraction for user lookup and uniqueness checks.
 * Controllers depend on this interface (DIP), not on DataStore directly.
 */
public interface IUserRepository {
    User findByUsername(String username);
    boolean usernameExists(String username);
    boolean idExists(long id);
}
