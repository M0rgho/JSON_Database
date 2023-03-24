package client;

import com.google.gson.JsonElement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements AutoCloseable {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;


    Client() throws IOException{
        System.out.println("Client started!");
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }


    public void send(JsonElement request) throws IOException {
        output.writeUTF(request.toString());
        System.out.println("Send:" + request);
    }

    public void receive() throws IOException {
        String response = input.readUTF();
        System.out.println("Received: " + response);
    }


    @Override
    public void close() throws IOException {
        socket.close();
    }
}
