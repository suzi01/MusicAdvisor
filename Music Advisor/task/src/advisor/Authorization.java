package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Authorization {

    public static String auth() throws IOException, InterruptedException {

        URI url = URI.create(Constants.createUrl);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(url);
        }
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);


        System.out.println(Constants.useLink);

        System.out.println(Constants.linkUrl);
        System.out.println("waiting for code...");

        int timeout = 20;
        String authCode = null;
        while (authCode == null && timeout > 0) {
            authCode = getAuthCodeFromServer(server);
            timeout--;
        }


        return authCode.substring(authCode.indexOf("code=") + 5);
    }

    public static String accessToken(String authCode, String accessServer) throws IOException, InterruptedException {
        String accessToken = null;
        if (authCode != null) {
            System.out.println(Constants.codeReceived);
            System.out.println(Constants.makeRequest);
            String postRequest = createPostRequest(authCode, accessServer);
            JsonObject response = JsonParser.parseString(postRequest).getAsJsonObject();
            accessToken = response.get("access_token").getAsString();
            System.out.println(Constants.success);
        }
        return accessToken;
    }

    public static String getAuthCodeFromServer(HttpServer server) throws InterruptedException {
        final String[] queryHolder = {null};

        server.createContext("/",
                exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    queryHolder[0] = query;
                    String response = "";

                    if(query != null && query.contains("code=")){
                        response = Constants.successResponse ;
                    } else {
                        response = Constants.failedResponse ;
                    }

                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                }
        );

        server.start();

        while(queryHolder[0] == null){
            Thread.sleep(100);
        }

        server.stop(1);
        return queryHolder[0];
    }


    public static String createPostRequest(String authorizationCode, String accessServer) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String token = accessServer + "/api/token";

        String body = "grant_type=authorization_code" +
                "&code=" + authorizationCode +
                "&redirect_uri=http://localhost:8080";

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(token))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
