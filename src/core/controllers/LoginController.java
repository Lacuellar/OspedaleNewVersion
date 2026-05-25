package core.controllers;

import core.models.DataStore;
import core.models.Response;
import core.models.User;
import org.json.JSONObject;

/**
 * Controller for authentication logic.
 * Views must NOT validate credentials — delegate everything to this controller.
 */
public class LoginController {

    private final DataStore dataStore;

    public LoginController() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Authenticates a user by username and password.
     *
     * @return Response(OK, ..., data{userId, username})  on success
     *         Response(BAD_REQUEST)                      if fields are empty
     *         Response(NOT_FOUND)                        if username does not exist
     *         Response(UNAUTHORIZED)                     if password is wrong
     */
    public Response login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return new Response(Response.BAD_REQUEST, "Username cannot be empty.");
        }
        if (password == null || password.isEmpty()) {
            return new Response(Response.BAD_REQUEST, "Password cannot be empty.");
        }

        User user = dataStore.getUserByUsername(username.trim());
        if (user == null) {
            return new Response(Response.NOT_FOUND, "User '" + username + "' not found.");
        }
        if (!user.getPassword().equals(password)) {
            return new Response(Response.UNAUTHORIZED, "Incorrect password.");
        }

        JSONObject data = new JSONObject();
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        return new Response(Response.OK,
                "Login successful. Welcome, " + user.getFirstname() + " " + user.getLastname() + "!", data);
    }
}
