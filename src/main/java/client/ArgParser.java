package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ArgParser {
    @Parameter(names = {"-in"}, description = "Read request from the file")
    public String fileName;
    @Parameter(names = {"-t"}, description = "Type of request to be preform")
    public String requestType;
    @Parameter(names = {"-k"}, description = "Key to store")
    public String key;
    @Parameter(names = "-v", description = "Value of the key")
    public String value;

    public ArgParser(String ... args) {
        JCommander.newBuilder().addObject(this).build().parse(args);
    }

    public JsonObject getRequest() throws FileNotFoundException {
        if (fileName != null) {
            File file = new File(System.getProperty("user.dir"),"\\src\\test\\resources\\clientRequests\\" + fileName);
            return (JsonObject) JsonParser.parseReader(new FileReader(file));
        }
        JsonObject request = new JsonObject();
        request.addProperty("type", requestType);
        if(!requestType.equals("exit")){
            request.addProperty("key", key);
            if(requestType.equals("set"))
                request.addProperty("value", value);
        }
        return request;
    }

    @Override
    public String toString() {
        return requestType + " " + key + " " + value;
    }
}
