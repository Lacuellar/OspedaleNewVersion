package core.controllers;

import core.models.Response;

/**
 * Interface for login controller (SOLID - Interface Segregation Principle).
 * Depends on abstraction, not on concrete implementation (Dependency Inversion).
 */
public interface ILoginController {
    Response login(String username, String password);
}
