package client;


import com.google.gson.JsonElement;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try(Client client = new Client()) {
            JsonElement request = new ArgParser(args).getRequest();
            client.send(request);
            client.receive();
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }
}
