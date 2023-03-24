package server;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try (Server server = new Server()) {
        } catch (Exception e){
            System.err.println("Server error has occurred: " + e.getMessage());
        }
    }
}
