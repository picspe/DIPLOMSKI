package main.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ObjectMapper {
    private JsonParser parser;
    private Gson gson;

    public ObjectMapper() {
        this.parser = new JsonParser();
        this.gson = new Gson();
    }

    public <T> T mapFromJson(String object, Class<T> type) {
        JsonObject json = this.parser.parse(object).getAsJsonObject();
        return this.gson.fromJson(json, type);
    }

    public String mapToJson(Object object) {
        return this.gson.toJson(object);
    }
}
