package server;

import com.google.gson.JsonParser;
import io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Server implements AutoCloseable{

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    Gson gson = new GsonBuilder().registerTypeAdapter(Request.class, new Request.RequestGsonDeserializer()).create();

    JSONDatabase database = new JSONDatabase();


    class SocketThread implements Runnable {
        private final Socket socket;

        SocketThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
                String clientJSON = input.readUTF();
                Request request = gson.fromJson(clientJSON, Request.class);
                Response response = processRequest(request);
                output.writeUTF(response.toString());
                input.close();
                output.close();
                if (request.type == Request.RequestType.EXIT) {
                    close();
                }
            } catch (Exception e) {
                System.err.println("Thread failed " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    ExecutorService executor = Executors.newFixedThreadPool(10);
    ServerSocket server;

    public Server() {
        try {
            server = new ServerSocket(SERVER_PORT, 50);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Server started!");

        try {
            while(true) {
                executor.submit(new SocketThread(server.accept()));
            }
        } catch (SocketException e) {
            System.out.println("Server Terminated successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response processRequest(Request request) {

        switch (request.type) {
            case GET -> {
                return database.get(request.key);
            }
            case SET -> {
                return database.set(request.key, request.value);
            }
            case DELETE -> {
                return database.delete(request.key);
            }
            case EXIT -> {
                return new Response("OK");
            }
            default -> {
                return new Response("ERROR");
            }
        }
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
        server.close();
    }
}
