package core.models;

import org.json.JSONObject;

/**
 * Response object used by controllers to communicate results back to the view.
 * Views must NOT process business logic — only display the response.
 */
public class Response {

    // HTTP-style status codes
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int INTERNAL_ERROR = 500;

    private final int status;
    private final String message;
    private final JSONObject data;

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = new JSONObject();
    }

    public Response(int status, String message, JSONObject data) {
        this.status = status;
        this.message = message;
        this.data = data != null ? data : new JSONObject();
    }

    /** Returns true if the operation was successful (2xx). */
    public boolean isSuccess() {
        return status >= 200 && status < 300;
    }

    public int getStatus()    { return status;  }
    public String getMessage(){ return message; }
    public JSONObject getData(){ return data;   }

    @Override
    public String toString() {
        return "Response{status=" + status + ", message='" + message + "'}";
    }
}
