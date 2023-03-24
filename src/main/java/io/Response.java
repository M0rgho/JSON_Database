package io;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Response {
    public final String result;
    public final String value;
    public final String reason;

    public Response(String result) {
        this(result, null, null);
    }

    public Response(String result, String value, String reason) {
        this.result = result;
        this.value = value;
        this.reason = reason;
    }

    public JsonObject toJSON() {
        JsonObject response = new JsonObject();
        response.addProperty("response",  result);
        if (value != null)
            response.add("value", JsonParser.parseString(value));
        if (reason != null)
            response.addProperty("reason",  reason);
        return response;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
