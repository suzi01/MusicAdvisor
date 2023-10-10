package advisor;

public class Constants {

    public static final String accessServer =  "https://accounts.spotify.com";
    public static final String serverPath = "https://api.spotify.com";
    public static final String newReleasePath = "/v1/browse/new-releases";
    public static final String playlistPath = "/v1/browse/featured-playlists";
    public static final String categoriesPath = "/v1/browse/categories";
    public static final String successResponse = "Got the code. Return back to your program.";
    public static final String failedResponse = "Authorization code not found. Try again.";
    public static final String unknownCategory = "Unknown category name.";
    public static final String albums = "albums";
    public static final String items = "items";
    public static final String externalUrl = "external_urls";
    public static final String artists = "artists";
    public static final String name = "name";
    public static final String spotify = "spotify";
    public static final String playlists = "playlists";
    public static final String categories = "categories";
    public static final String provideAccess = "Please, provide access for application.";
    public static final String exit = "exit";
    public static final String goodbye = "---GOODBYE!---";
    public static final String success = "---SUCCESS---";



    public static final String codeReceived = "code received";
    public static final String makeRequest = "making http request for access_token...";

    public static final String useLink = "use this link to request the access code:";

    public static final String createUrl = "https://accounts.spotify.com/authorize?" +
            "client_id=0beffa8a2eaa4f02994426961addd171" +
            "&redirect_uri=http://localhost:8080" +
            "&response_type=code";

    public static final String linkUrl ="https://accounts.spotify.com/authorize?client_id=0beffa8a2eaa4f02994426961addd171&redirect_uri=http://localhost:8080&response_type=code";


}
