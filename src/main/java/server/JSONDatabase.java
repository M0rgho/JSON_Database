package server;

import com.google.gson.*;
import io.Response;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JSONDatabase {
    private final File databaseFile = new File("src\\main\\java\\server\\data\\db.json");
    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    public Response get(String key) {

        JsonObject data = getJSONFromDatabase();
        try {
            String[] path = JsonToStringArr(JsonParser.parseString(key));
            String res = getJsonParent(data, path, false).get(path[path.length - 1]).toString();
            return new Response("OK", res, null);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new Response("ERROR", null, "No such key");
        }
    }

    private String[] JsonToStringArr(JsonElement obj) {
        if(obj.isJsonPrimitive()) {
            return new String[]{obj.getAsString()};
        }
        JsonArray arr = (JsonArray) obj;
        final int n = arr.size();
        String[] strings = new String[n];
        for(int i = 0; i < n; ++i) {
            if(!(arr.get(i) instanceof JsonPrimitive))
                throw new IllegalArgumentException("Invalid JSON Key");
            strings[i] = arr.get(i).getAsString();
        }
        return strings;
    }


    public Response set(String key, String value) {
        JsonObject data = getJSONFromDatabase();
        try {
            String[] path = JsonToStringArr(JsonParser.parseString(key));
            JsonObject parent = getJsonParent(data, path, true);
            parent.add(path[path.length - 1], JsonParser.parseString(value));
            updateDatabase(data);
            return new Response("OK");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Response("ERROR", null, "Invalid key");
        }

    }

    private JsonObject getJsonParent(JsonObject data, String[] path, boolean createNew) {
        JsonObject cur = data;
        for(int i = 0; i < path.length - 1; ++ i) {
            if(!cur.isJsonObject() || !cur.has(path[i])) {
                if (createNew) {
                    cur.add(path[i], new JsonObject());
                } else {
                    throw new IllegalArgumentException("Invalid Json Path");
                }
            }
            cur = (JsonObject) cur.get(path[i]);
        }
        return cur;
    }

    public Response delete(String key) {
        JsonObject data = getJSONFromDatabase();
        try {
            String[] path = JsonToStringArr(JsonParser.parseString(key));
            JsonObject parent = getJsonParent(data, path, true);
            parent.remove(path[path.length - 1]);
            updateDatabase(data);
            return new Response("OK");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new Response("ERROR", null, "No such key");
        }
    }

    private JsonObject getJSONFromDatabase() {
        readLock.lock();
        JsonElement element = null;
        try {
            element = JsonParser.parseString(Files.readString(databaseFile.toPath()));
            if (!(element instanceof JsonObject))
                element = new JsonObject();
        } catch (Exception e) {
            System.err.println("Can't create JSON object from databaseFile - " + e.getMessage());
            e.printStackTrace();
        }
        readLock.unlock();
        return (JsonObject) element;
    }

    private void updateDatabase(JsonObject newDatabase) {
        writeLock.lock();
        try (FileWriter writer = new FileWriter(databaseFile)) {
            writer.write(newDatabase.toString());
        } catch (Exception e) {
            System.out.println("Can't update databaseFile - " + e.getMessage());
            e.printStackTrace();
        }
        writeLock.unlock();
    }
}