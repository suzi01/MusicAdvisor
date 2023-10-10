package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Media<T> {

    public Media( )
            throws
            IOException,
            InterruptedException
    {
    }

    public JsonArray extractData(String response, String itemName, String urlName, String type) throws
            IOException, InterruptedException {
        JsonObject responseObj = JsonParser.parseString(response).getAsJsonObject();
        JsonObject data = responseObj.getAsJsonObject(itemName);
        JsonArray items = data.getAsJsonArray(Constants.items);
        JsonArray extractedItems = new JsonArray();
        T itemData = null;

        for (int i = 0; i < items.size(); i++) {
            JsonObject itemObject = items.get(i).getAsJsonObject();
            switch (type) {
                case "extractItemData" -> itemData = extractItemData(itemObject, urlName);
                case "extractNameData" -> itemData = extractNameData(itemObject);
                case "extractItemDataNew" -> itemData = extractItemDataNew(itemObject, urlName);
            }
            extractedItems.add((String) itemData);
        }

        return extractedItems;
    }

    private T extractItemData(JsonObject itemObject, String urlName) {
        String itemName = itemObject.get(Constants.name).getAsString();
        JsonObject externalUrlsObj = itemObject.getAsJsonObject(Constants.externalUrl);
        String url = externalUrlsObj.get(urlName).getAsString();
        return (T)(itemName + "\n" + url);
    }

    private T extractNameData(JsonObject itemObject) {
        String itemName = itemObject.get(Constants.name).getAsString();
        return (T)(itemName);
    }

    private T extractItemDataNew(JsonObject itemObject, String urlName) {
        String itemName = itemObject.get(Constants.name).getAsString();
        JsonObject externalUrlsObj = itemObject.getAsJsonObject(Constants.externalUrl);
        String url = externalUrlsObj.get(urlName).getAsString();
        JsonArray artistsObj = itemObject.getAsJsonArray(Constants.artists);
        List<String> artistName = new ArrayList<>();
        for (JsonElement jsonArray : artistsObj) {
            JsonObject jsonObject = jsonArray.getAsJsonObject();
            artistName.add(jsonObject.get(Constants.name).getAsString());
        }
        return (T) (itemName + "\n" + artistName + "\n" + url);
    }
}