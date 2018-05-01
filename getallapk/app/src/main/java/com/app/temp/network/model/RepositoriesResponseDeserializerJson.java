package com.app.temp.network.model;

import com.app.temp.pojo.Repository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by nguyen_van_cuong on 23/11/2017.
 */

public class RepositoriesResponseDeserializerJson implements JsonDeserializer<RepositoriesResponse> {

    @Override
    public RepositoriesResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        RepositoriesResponse repositoriesResponse = new RepositoriesResponse();
        JsonArray jsonArray = je.getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : jsonArray) {
            Repository repository = gson.fromJson(element.getAsJsonObject(), Repository.class);
            repositoriesResponse.add(repository);
        }
        return repositoriesResponse;
    }
}
