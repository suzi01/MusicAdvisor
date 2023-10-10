package advisor.View;

import advisor.Constants;
import advisor.Media;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class MusicView {
    public void incorrectAccess() {
            System.out.println(Constants.provideAccess);
    }

    public void userAccessExit(){
        System.out.println(Constants.provideAccess);
        System.exit(0);
    }

    //    To get new releases, use https://api.spotify.com/v1/browse/new-releases
    public void newList(String token, String serverPath, int pageNumber) throws IOException, InterruptedException {
        String response =  createGetRequest(token, serverPath, Constants.newReleasePath);
        Media<String>  musicExtractor = new Media<>();
        JsonArray newSongs = musicExtractor.extractData(response,Constants.albums, Constants.spotify, "extractItemDataNew");
        paginatedResult(newSongs, pageNumber, token,serverPath);
    }

    //    To get featured playlists, use https://api.spotify.com/v1/browse/featured-playlists
    public void featured(String token, String serverPath, int pageNumber) throws IOException, InterruptedException {
        String response =  createGetRequest(token, serverPath, Constants.playlistPath);
        Media<String>  musicExtractor = new Media<>();
        JsonArray featuredSongs = musicExtractor.extractData(response,Constants.playlists, Constants.spotify, "extractItemData");
        paginatedResult(featuredSongs, pageNumber, token,serverPath);
    }

    //    To get all categories, use https://api.spotify.com/v1/browse/categories
    public void categories(String token, String serverPath, int pageNumber) throws IOException, InterruptedException {
        String response = createGetRequest(token, serverPath, Constants.categoriesPath);
        Media<String>  musicExtractor = new Media<>();
        JsonArray categorySongs = musicExtractor.extractData(response,Constants.categories, Constants.spotify, "extractNameData");
        paginatedResult(categorySongs, pageNumber, token,serverPath);
    }

//    To get a playlist, use https://api.spotify.com/v1/browse/categories/{category_id}/playlists
//    Sleep
//    https://open.spotify.com/playlist/37i9dQZF1DWZd79rJ6a7lp

    public void playLists(String word,String token, String serverPath, int pageNumber) throws IOException, InterruptedException {
        String categoryId = getCategoryId(token,word, serverPath);
        if (categoryId != null){
            String api = "/v1/browse/categories/" + categoryId + "/playlists";
            String response =  createGetRequest(token, serverPath, api);
            if (!response.contains("error")){
                Media<String>  musicExtractor = new Media<>();
                JsonArray playlistSongs = musicExtractor.extractData(response,Constants.playlists, Constants.spotify, "extractItemData");
                paginatedResult(playlistSongs, pageNumber, token,serverPath);
            } else{
                System.out.println(response);
            }
        } else {
            System.out.println(Constants.unknownCategory);
        }
    }

    public void exit() {
        System.out.println(Constants.goodbye);
    }




    private String createGetRequest(String accessToken, String resource, String apiPath) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(resource + apiPath))
                .GET()
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }


    private String getCategoryId(String accessToken, String word, String serverPath) throws IOException, InterruptedException {
        String categoryResponse = createGetRequest(accessToken, serverPath, Constants.categoriesPath);
        JsonObject catReObj = JsonParser.parseString(categoryResponse).getAsJsonObject();
        JsonObject catObj = catReObj.getAsJsonObject(Constants.categories);
        JsonArray items = catObj.getAsJsonArray(Constants.items);
        String catId = null;
        for (int i = 0; i < items.size(); i++) {
            JsonObject itemObject = items.get(i).getAsJsonObject();
            String foundName = itemObject.get(Constants.name).getAsString();
            if (foundName.equals(word)){
                catId = itemObject.get("id").getAsString();
            }
        }
        return catId;
    }

    public void paginatedResult(JsonArray extractedItems, int pageNumber, String accessToken, String serverPath) throws IOException, InterruptedException {

        int numberOfPages = extractedItems.size()/pageNumber;
        int currentPage = 1;

        var extractedItemsList = extractedItems.asList();
        var start = extractedItemsList.subList(0, pageNumber);
        getPage(start,currentPage,numberOfPages );


        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        do{
            int calcNextPage = currentPage * pageNumber;
            int calcPrevPage = (currentPage - 1) * pageNumber;
            if(userInput.equals("prev") && currentPage > 1){
                currentPage--;
                var prevSlice = extractedItemsList.subList(calcPrevPage - pageNumber, calcPrevPage);
                getPage(prevSlice, currentPage,numberOfPages  );
            }
            else if(userInput.equals("next") && currentPage < numberOfPages){
                currentPage++;
                var nextSlice = extractedItemsList.subList(calcNextPage, calcNextPage + pageNumber);
                System.out.println(currentPage * pageNumber);
                System.out.println(calcNextPage);
                System.out.println((currentPage * pageNumber) + pageNumber);
                getPage(nextSlice,currentPage,numberOfPages );
            } else {
                System.out.println("No more pages.");
            }
            userInput = scanner.nextLine();

        } while(userInput.equals("prev") ||  userInput.equals("next") );

        userSelection(userInput, accessToken,serverPath,pageNumber);


    }

    private void userSelection(String userInput,String accessToken, String serverPath, int pageNumber) throws IOException, InterruptedException {
        String[] words = userInput.split(" ",2);
        switch(words.length){
            case 1:
                switch (userInput){
                    case "new" -> newList(accessToken, serverPath, pageNumber);
                    case "featured" -> featured(accessToken, serverPath,pageNumber);
                    case "categories" -> categories(accessToken, serverPath, pageNumber);
                }
            case 2:
                if(words[0].equals("playlists")){
                    String secondWord = words[1];
                    playLists(secondWord,accessToken, serverPath, pageNumber);
                }
        }
    }

    private void getPage(List<JsonElement> slice, int currentPage, int numberOfPages){
        for(JsonElement item: slice){
            String[] parts = item.getAsString().split("\n");
            for (String part : parts) {
                System.out.println(part);
            }
        }
        System.out.println("---PAGE " + currentPage  + " OF " + numberOfPages  + "---");
    }
}
