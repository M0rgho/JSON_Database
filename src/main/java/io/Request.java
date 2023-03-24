package io;

import com.google.gson.*;

import java.lang.reflect.Type;



public class Request {
    public final RequestType type;
    public final String key;
    public final String value;
    public enum RequestType {
        GET,
        SET,
        DELETE,
        EXIT;

        public static RequestType getRequestType(String str) {
            return switch (str) {
                case "get" -> RequestType.GET;
                case "set" -> RequestType.SET;
                case "delete" -> RequestType.DELETE;
                case "exit" -> RequestType.EXIT;
                default -> throw new IllegalArgumentException("Can't process this RequestType: " + str);

            };
        }
    }

    public static class RequestGsonDeserializer implements JsonDeserializer<Request> {

        @Override
        public Request deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            jsonObject.get("type").getAsString();
            String key = jsonObject.has("key") ? jsonObject.get("key").toString() : null;
            String value = jsonObject.has("value") ? jsonObject.get("value").toString() : null;
            return new Request(RequestType.getRequestType(jsonObject.get("type").getAsString()), key, value);
        }
    }

    Request(RequestType type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }
}

